package com.devestudo.projeto_financas.entities.dtos;

import java.math.BigDecimal;

public record UpdateExpenseDto(String name,
                               BigDecimal value,
                               Long categoryId) {
}
