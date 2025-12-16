package com.devestudo.projeto_financas.entities.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponseDto(Long id,
                                 String name,
                                 BigDecimal value,
                                 LocalDate date,
                                 String description,
                                 Long categoryId,
                                 String cateroryName,
                                 Long userId,
                                 String userName
                                 ) {
}
