package com.devestudo.projeto_financas.services;
import com.devestudo.projeto_financas.entities.Category;
import com.devestudo.projeto_financas.entities.Expense;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.CreateExpenseDto;
import com.devestudo.projeto_financas.entities.dtos.UpdateExpenseDto;
import com.devestudo.projeto_financas.exception.BusinessException;
import com.devestudo.projeto_financas.exception.ResourceNotFoundException;
import com.devestudo.projeto_financas.repository.CategoryRepository;
import com.devestudo.projeto_financas.repository.ExpenseRepository;
import com.devestudo.projeto_financas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


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

    public Expense createExpense(CreateExpenseDto dto, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Usúario não encontrado"));

        Category category = null;  //Permite criar uma gasto sem categoria

        if(dto.categoryId() != null){  //Se o usuário passou uma categoria
            category = categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

            // Verifica se a categoria pertence ao usuário, ou seja verifica se quem crio o gasto é o dona categoria
            if (!category.getUser().equals(user)){

                throw new BusinessException("Categoria não encontrada");
            }
        }
       
        Expense expense = new Expense(dto.name(), dto.value(), dto.localDate(), dto.description(), category, user);

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

    //Listar todos os gastos de um determinado usuario
    public List<Expense> expenseList(Long userId){

       return expenseRepository.findByUserId(userId); //se ele existir, retornamos a lista de categorias vinculadas
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
