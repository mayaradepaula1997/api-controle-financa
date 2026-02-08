package com.devestudo.projeto_financas.controller;

import com.devestudo.projeto_financas.entities.Expense;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.CreateExpenseDto;
import com.devestudo.projeto_financas.entities.dtos.ExpenseResponseDto;
import com.devestudo.projeto_financas.entities.dtos.UpdateExpenseDto;
import com.devestudo.projeto_financas.services.ExpenseService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    //Cria uma gasto e associa ao usuário autenticado
    public ResponseEntity<ExpenseResponseDto> create(@RequestBody CreateExpenseDto expenseDto, @AuthenticationPrincipal User user){

        Expense expense = expenseService.createExpense(expenseDto, user.getId());

        //Variaveis começa com null, só serão preenchidas se existir a categoria
        Long categoryId = null;
        String categoryName = null;

        if (expense.getCategory() != null){  //Se tiver categoria as variaveis que antes eram null, passam a receber os valores preenchidos
            categoryId = expense.getCategory().getId();
            categoryName = expense.getCategory().getName();

        }

        ExpenseResponseDto response = new ExpenseResponseDto(
                expense.getId(),
                expense.getName(),
                expense.getValue(),
                expense.getLocalDate(),
                expense.getDescription(),
                categoryId,
                categoryName,
                user.getId(),
                user.getName()

        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    //Método que vai trazer UM gastos daquele usuario
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> findByExpense(@PathVariable Long expenseId, @AuthenticationPrincipal User user){

        Expense expense = expenseService.findByExpense(expenseId, user.getId());

        //Variaveis começa com null, só serão preenchidas se existir a categoria
        Long categoryId = null;
        String categoryName = null;

        if (expense.getCategory() != null){  //Se tiver categoria as variaveis que antes eram null, passam a receber os valores preenchidos
            categoryId = expense.getCategory().getId();
            categoryName = expense.getCategory().getName();

        }

        ExpenseResponseDto responseDto = new ExpenseResponseDto(
                expense.getId(),
                expense.getName(),
                expense.getValue(),
                expense.getLocalDate(),
                expense.getDescription(),
                categoryId,
                categoryName,
                user.getId(),
                user.getName()
        );

        return ResponseEntity.ok(responseDto);

    }

    //Método que vai  trazer todos os gastos daquele usuario - PAGINAÇÃO
    @GetMapping
    public ResponseEntity<Page<ExpenseResponseDto>> listExpenseUser(
            @AuthenticationPrincipal User user, //usuário autenticado, que vem do token

            @RequestParam(defaultValue = "0") int page, //paginação
            @RequestParam(defaultValue = "5") int size,

            @RequestParam(required = false) String text, //parametro que pode vim da url, required = false = pametro opcional
            @RequestParam(required = false) BigDecimal minValue,
            @RequestParam (required = false) BigDecimal maxValue,
            @RequestParam(required = false) Long categoryId,

            @RequestParam (required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) //converte o parametro da url, para esse formato de data
            LocalDate dateStart,

            @RequestParam (required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {


        Page<ExpenseResponseDto> expenseList = expenseService.listExpensesWithFilter(
                        user.getId(),
                        minValue,
                        maxValue,
                        categoryId,
                        text,
                        dateStart,
                        endDate,
                        page,
                        size
                        );


        return ResponseEntity.ok().body(expenseList);
    }



    //Método para atualizar os gastos
    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long expenseId, @AuthenticationPrincipal User user, @RequestBody UpdateExpenseDto updateExpenseDto){

        Expense expenseUpdate = expenseService.updateExpense(expenseId, user.getId(), updateExpenseDto);

        //Variaveis começa com null, só serão preenchidas se existir a categoria
        Long categoryId = null;
        String categoryName = null;

        if (expenseUpdate.getCategory() != null){  //Se tiver categoria as variaveis que antes eram null, passam a receber os valores preenchidos
            categoryId = expenseUpdate.getCategory().getId();
            categoryName = expenseUpdate.getCategory().getName();

        }

        ExpenseResponseDto response = new ExpenseResponseDto(
                expenseUpdate.getId(),
                expenseUpdate.getName(),
                expenseUpdate.getValue(),
                expenseUpdate.getLocalDate(),
                expenseUpdate.getDescription(),
                categoryId,
                categoryName,
                user.getId(),
                user.getName()
        );

        return ResponseEntity.ok().body(response);
    }




    //Método para deletar o gasto
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId, @AuthenticationPrincipal User user){

        expenseService.delete(expenseId, user.getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


}
