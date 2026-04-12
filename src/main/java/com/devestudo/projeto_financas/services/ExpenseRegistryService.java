package com.devestudo.projeto_financas.services;

import com.devestudo.projeto_financas.entities.Expense;
import com.devestudo.projeto_financas.entities.dtos.response.ExpenseTotalResponseDto;
import com.devestudo.projeto_financas.enums.PaymentMethod;
import com.devestudo.projeto_financas.exception.BusinessException;
import com.devestudo.projeto_financas.filter.ExpenseSpecification;
import com.devestudo.projeto_financas.repository.ExpenseRepository;
import com.devestudo.projeto_financas.security.SecurityUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ExpenseRegistryService { //ExpenseRegistry: Regitro de despesas

    private final ExpenseRepository expenseRepository;

    public ExpenseRegistryService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    //Método que vai retorna o valor total do gasto
    public ExpenseTotalResponseDto findExpensesByFilter(PaymentMethod paymentMethod,
                                                        LocalDate dateStart,
                                                        LocalDate dateEnd) {

        //Busca o usuário logado, valida se ele existe e retorna o ID dele
        Long userId = SecurityUtils.getUserId();

        //Definindo as regras da data
        LocalDate now = LocalDate.now();

        if (dateStart == null && dateEnd == null) {
            dateStart = now.withDayOfMonth(1);
            dateEnd = now;
        }

        //Se o usuário passar a data inicial a data final, vai receber 30 dias depois
        if (dateStart != null && dateEnd == null) {
            dateEnd = dateStart.plusDays(30);
        }

        //Se o usuério não passar a data inicial e passar a data final, data inicial recebe 30 dia a menos que a data final
        if (dateStart == null && dateEnd != null) {
            dateStart = dateEnd.minusDays(30);
        }

        if (dateEnd.isAfter(now)) {
            dateEnd = now;
        }

        //Validando o intervalo
        long days = ChronoUnit.DAYS.between(dateStart, dateEnd);
        if (days > 30) {
            throw new BusinessException("Intervalo máximo de 30 dias");
        }

        //Specification dinâmica
        Specification<Expense> spec =
                ExpenseSpecification.byUser(userId)
                        .and(ExpenseSpecification.byPeriod(dateStart, dateEnd));

        if (paymentMethod != null) {
            spec = spec.and(ExpenseSpecification.byPaymentMethod(paymentMethod));
        }

        //Busca todos os gasto, depois de aplicar todos os filtros
        List<Expense> expenses = expenseRepository.findAll(spec);

        //Depois que retornar todos os gasto, vamos fazer um MAP para pegar os valores
        BigDecimal total = expenses.stream()
                .map(Expense::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExpenseTotalResponseDto(total);

    }
}
