package com.devestudo.projeto_financas.entities.dtos;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseDto(String name,
                               BigDecimal value,
                               LocalDate localDate,
                               String description,
                               Long categoryId) {
}
