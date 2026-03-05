package com.devestudo.projeto_financas.services;

import com.devestudo.projeto_financas.entities.Expense;
import com.devestudo.projeto_financas.entities.dtos.response.CategorySummaryDto;
import com.devestudo.projeto_financas.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/*Busca todos os gastos do usuário
-Soma o TOTAL dos gastos
-AGRUPA os gastos por categoria (do sistema e as que foram criadas pelo usuário)
-Calcular quanto cada categoria representa em % do total */


@Service
public class ExpenseSummaryService {

    private ExpenseRepository expenseRepository;

    public ExpenseSummaryService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;

    }

    public List<CategorySummaryDto> getSummaryByUser(Long userId) {

        //Busca todos os gastos do usuário
        List<Expense> expenseList = expenseRepository.findByUserId(userId);

        //Para cada gasto da lista, vamos somar o valor e aguarda na variavel -> "totalSpent"
        BigDecimal totalExpense = expenseList.stream()
                .map(Expense::getValue) // Tranforma o Expense em BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add); //Soma os valores: Começa com ZERO, e vamos add e somando os valores

        if (totalExpense.compareTo(BigDecimal.ZERO) == 0) {

            return List.of(); //retorna uma lista vazia
        }


        //Agrupa os gastos por categoria e realizar a soma dos valores de cada categoria
        Map<String, BigDecimal> totalByCategory = expenseList.stream() //Para cada gasto na minha lista de gastos
                .collect(Collectors.groupingBy(         //Vamos agrupar os gastos por uma chave
                        e -> e.getCategory().getName(), //"e" -> Representa um gasto/expense -> pega a categoria e o nome dela
                        Collectors.reducing(            //Define o que vamos fazer com cada elemento
                                BigDecimal.ZERO,
                                Expense::getValue,      //Para cada gasto pegue o valor
                                BigDecimal::add         //Soma os valores de cada categoria, tomando como base o atributo "Value"
                        )
                ));


        //Calculo do percentual
         return totalByCategory.entrySet().stream()     //entrySet - transforma o Map em um conjunto de pares (chave e valor)
                .map(entry -> {
                    BigDecimal percentage = entry.getValue();   //pega o total de gasto de cada categoria

                    if (percentage.compareTo(BigDecimal.ZERO) > 0) {
                        percentage = entry.getValue()
                         .multiply(BigDecimal.valueOf(100))  //multiplica por 100 para vira percentual
                        .divide(totalExpense, 2, RoundingMode.HALF_UP);  //divide pelo total de gasto "totalExpense"
                    }

                    return new CategorySummaryDto(
                            entry.getKey(),      //nome (chave)
                            entry.getValue(),   //valor
                            percentage          //porcentagem
                    );
                })

                .toList();    //retornando uma lista

    }

}
