package com.devestudo.projeto_financas.entities.dtos.request;

import com.devestudo.projeto_financas.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseDto(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotNull(message = "Valor é obrigatório")
        BigDecimal value,

        @NotNull(message = "Data é obrigatória")
        LocalDate localDate,

        String description,

        Long categoryId,

        @NotNull(message = "Forma de pagamento é obrigatória")
        PaymentMethod paymentMethod,

        String nameCard) {

}
