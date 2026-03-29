package com.devestudo.projeto_financas.entities.dtos.request;

import com.devestudo.projeto_financas.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateExpenseDto(String name,
                               BigDecimal value,
                               LocalDate localDate,
                               String description,
                               PaymentMethod paymentMethod,
                               String nameCard,
                               Long categoryId) {
}
