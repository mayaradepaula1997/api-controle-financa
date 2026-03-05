package com.devestudo.projeto_financas.services;
import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.entities.Expense;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.request.CreateExpenseDto;
import com.devestudo.projeto_financas.entities.dtos.response.ExpenseResponseDto;
import com.devestudo.projeto_financas.entities.dtos.request.UpdateExpenseDto;
import com.devestudo.projeto_financas.enums.CategoryType;
import com.devestudo.projeto_financas.exception.BusinessException;
import com.devestudo.projeto_financas.exception.ResourceNotFoundException;
import com.devestudo.projeto_financas.filter.ExpenseSpecification;
import com.devestudo.projeto_financas.repository.CategoryRepository;
import com.devestudo.projeto_financas.repository.ExpenseRepository;
import com.devestudo.projeto_financas.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;


@Service
public class ExpenseService {

    private  ExpenseRepository expenseRepository;
    private  UserRepository userRepository;
    private  CategoryRepository categoryRepository;


    //Injeção de dependencia
    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    //Criação de categoria
    public Expense createExpense(CreateExpenseDto dto, Long userId){

        //Busca o usuário no BD
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Usuário não encontrado"));

        if (dto.categoryId() == null){
            throw new BusinessException("Categoria é obrigatória");
        }

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Categoria não encontrada"));

        if (category.getCategoryType() == CategoryType.USER &&
        !category.getUser().getId().equals(user.getId())){

            throw new BusinessException("Categoria não pertence ao usuário");
        }

        Expense expense = new Expense(
                dto.name(),
                dto.value(),
                dto.localDate(),
                dto.description(),
                category,
                user
        );

        return expenseRepository.save(expense);

    }

    //Listar por Id
    public Expense findByExpense(Long expenseId, Long userId){

        //Verificar se o gasto existe
      Expense expense = expenseRepository.findById(expenseId)
              .orElseThrow(()-> new ResourceNotFoundException("Gasto não encontrado"));

      //Verificar se aquele gasto pertence a aquele determinado usuario
        if(!expense.getUser().getId().equals(userId)){ //Pega o id do usuario vinculado ao gasto e compara com o id passado no parametro
            throw new BusinessException("Esse gasto não pertence ao usuário informado");

        }

      return expense;
    }

    //Todos os gastos de um determinado usuario, por PAGINAÇÃO e FILTER SPECIFICATION
    public Page<ExpenseResponseDto> listExpensesWithFilter(
            Long userId,
            BigDecimal minValue,
            BigDecimal maxValue,
            Long categoryId,
            String text,
            LocalDate dateStart,
            LocalDate endDate,
            int page,
            int size
    ){

        //VALIDAÇÕES DE DATA
        LocalDate now = LocalDate.now();

        //Verifica se a data inicial é maior que a data de hoje(now)
        if (dateStart != null && dateStart.isAfter(now)){  //isAfter = "É depois"
            throw new BusinessException("A data inicial não pode ser futura");
        }

        //Verifica se a data final não é uma data futura - futuro não existe ainda
        if (endDate != null && endDate.isAfter(now)){
            throw  new BusinessException("A data final não pode ser futura");
        }

        //Verifica se a data final não é menor que a data inicial
        if (dateStart != null && endDate != null && endDate.isBefore(dateStart)){ //isBefore = "É antes"
            throw new BusinessException("A data final não pode ser anterior á data inicial");
        }

        //PERÍODO PADRÃO - SE O USUÁRIO NÃO INFORMAR O PERÍODO, A SISTEMA ASSUME O MêS ATUAL
        if (dateStart == null || endDate == null){
            YearMonth currentMonth = YearMonth.now(); //recupera o mês atual
            dateStart = currentMonth.atDay(1); //recupera o primeiro dia do mês
            endDate = currentMonth.atEndOfMonth();  //recupera o ultimo dia do mês
        }

        //Limite de 30 dias
        long days = ChronoUnit.DAYS.between(dateStart, endDate); //calcula quantos dias existem entre duas datas

        if (days > 30){
            throw new BusinessException("O intervalo máximo permitido é de 30 dias");
        }


        Pageable pageable = PageRequest.of(page, size, Sort.by("localDate").descending()); //Ordenação por data, do gasto mais novo para o mais antigo

        //Aplicação de filtros dinâmicos - Regra de busca na entidade Expense
        Specification<Expense> spec =
                ExpenseSpecification.byUser(userId)  //Busca gastos apenas do usuário logado
                        .and(ExpenseSpecification.minValue(minValue))
                        .and(ExpenseSpecification.maxValue(maxValue))
                        .and(ExpenseSpecification.category(categoryId))
                        .and(ExpenseSpecification.nameOrDescriptionContains(text)) //text, vai vim do controller, vai ser capturado o valor que o usuário digitar
                        .and(ExpenseSpecification.byPeriod(dateStart, endDate));


        return expenseRepository.findAll(spec, pageable) //se ele existir, retornamos a lista de categorias vinculadas
                .map(expense -> {

                    Long catId = null;
                    String catName = null;

                    if (expense.getCategory() != null){
                        catId = expense.getCategory().getId();
                        catName = expense.getCategory().getName();
                    }

                    return new ExpenseResponseDto(

                            expense.getId(),
                            expense.getName(),
                            expense.getValue(),
                            expense.getLocalDate(),
                            expense.getDescription(),
                            catId,
                            catName,
                            expense.getUser().getId(),
                            expense.getUser().getName()
                    );
                });
    }


    //Método para atualizar o gasto - passando o id do gasto, o id do usuario
    public Expense updateExpense (Long idExpense, Long idUser, UpdateExpenseDto updateExpenseDto){

        Expense expense = expenseRepository.findById(idExpense)
                .orElseThrow(()-> new ResourceNotFoundException("Gasto não encontrado"));

        //Verificar se o gasto pertence ao usuario
        if (!expense.getUser().getId().equals(idUser)){
            throw new BusinessException("Gasto não encontrado");
        }

        if(updateExpenseDto.name() != null)expense.setName(updateExpenseDto.name());

        if(updateExpenseDto.value() != null)expense.setValue(updateExpenseDto.value());

        if (updateExpenseDto.localDate() != null) expense.setLocalDate(updateExpenseDto.localDate());

        if (updateExpenseDto.description() != null)expense.setDescription(updateExpenseDto.description());

        //É NECESSARIA BUSCAR A CATEGORIA, PORQUE O DTO ESPERA UM LONG E O SETCATEGORY UMA CATEGORIA
        //NESSE CASO VOU TRAZER A CATEGORIA ATRAVES DO SEU ID
        if(updateExpenseDto.categoryId() != null){
            Category category = categoryRepository.findById(updateExpenseDto.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não existe"));

            //O usuário só deve ser bloqueado se a categoria for do tipo USER e não pertencer a ele
            //SÓ verifique o usuário se a categoria for do tipo USER
            if (category.getCategoryType() == CategoryType.USER &&
            !category.getUser().getId().equals(idUser)){

                throw new BusinessException("Você não pode usar essa categoria");
            }

            expense.setCategory(category);

            }
        return expenseRepository.save(expense);
    }


    //Método para deletar um gasto
    public void delete(Long idExpense, Long idUser){

        //Verificar se o GASTO e o USURIO existe no banco de dados
        Expense expense = expenseRepository.findById(idExpense)
                .orElseThrow(()-> new ResourceNotFoundException("Gasto não encontrado"));

        //Verificar se o gasto pertence ao usuario
        if (!expense.getUser().getId().equals(idUser)){
            throw new BusinessException("Gasto não pertense ao usuario");
        }

        expenseRepository.delete(expense);
    }
}
