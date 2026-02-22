package com.devestudo.projeto_financas.entities.dtos;

import java.math.BigDecimal;


//DTO DE RETORNO
public class CategorySummaryDto {

    private String category;

    private BigDecimal value;

    private BigDecimal percentage;

    public CategorySummaryDto(String category, BigDecimal value, BigDecimal percentage) {
        this.category = category;
        this.value = value;
        this.percentage = percentage;
    }
}
