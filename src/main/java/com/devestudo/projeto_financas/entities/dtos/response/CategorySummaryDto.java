package com.devestudo.projeto_financas.entities.dtos.response;

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

    public String getCategory() {
        return category;
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }
}
