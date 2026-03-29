package com.devestudo.projeto_financas.entities.dtos.response;

import com.devestudo.projeto_financas.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponseDto(Long id,
                                 String name,
                                 BigDecimal value,
                                 LocalDate date,
                                 String description,
                                 PaymentMethod paymentMethod,
                                 String nameCard,
                                 Long categoryId,
                                 String cateroryName,
                                 Long userId,
                                 String userName
                                 ) {
}
