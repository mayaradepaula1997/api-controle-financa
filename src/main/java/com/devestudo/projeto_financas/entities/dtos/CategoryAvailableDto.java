package com.devestudo.projeto_financas.entities.dtos;

import com.devestudo.projeto_financas.enums.CategoryType;

public record CategoryAvailableDto(Long id, String name, CategoryType categoryType) {
}
