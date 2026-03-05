package com.devestudo.projeto_financas.entities.dtos.request;

import java.math.BigDecimal;


public record CreateUserDto(String name, String email, String password, BigDecimal salary){

}
