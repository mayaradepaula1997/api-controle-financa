package com.devestudo.projeto_financas.entities.dtos;

import java.math.BigDecimal;


//DTO CRIADO PARA MELHORAR O RETORNO NO MOMENTO QUE CRIAR O USUÁRIO
public record UserResponseDto(
        Long id,
        String name,
        String email,
        BigDecimal salary,
        String role,
        String password
) {
}
