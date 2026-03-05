package com.devestudo.projeto_financas.entities.dtos.request;

import java.math.BigDecimal;


public record UserDto(Long id, String name, String email, BigDecimal salary){
}
