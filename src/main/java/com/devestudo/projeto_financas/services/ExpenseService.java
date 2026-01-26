package com.devestudo.projeto_financas.services;
import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.entities.Expense;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.CreateExpenseDto;
import com.devestudo.projeto_financas.entities.dtos.ExpenseResponseDto;
import com.devestudo.projeto_financas.entities.dtos.UpdateExpenseDto;
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

    //Todos os gastos de um determinado usuario, por PAGINAÇÃO
    public Page<ExpenseResponseDto> listExpensesWithFilter(
            Long userId,
            BigDecimal minValue,
            BigDecimal maxValue,
            Long categoryId,
            int page,
            int size
    ){

        Pageable pageable = PageRequest.of(page, size, Sort.by("localDate").descending()); //Ordenação por data, do gasto mais novo para o mais antigo

        Specification<Expense> spec =
                ExpenseSpecification.byUser(userId)
                        .and(ExpenseSpecification.minValue(minValue))
                        .and(ExpenseSpecification.maxValue(maxValue))
                        .and(ExpenseSpecification.category(categoryId));


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

            if (!category.getUser().getId().equals(idUser)){
                throw new BusinessException(" Você não pode usar essa categoria");
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
