package com.devestudo.projeto_financas.entities.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseDto(String name,
                               BigDecimal value,
                               LocalDate localDate,
                               String description,
                               Long categoryId) {
}
