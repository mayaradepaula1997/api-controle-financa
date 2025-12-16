package com.devestudo.projeto_financas.entities.dtos;

import java.math.BigDecimal;

public record UpdateUser(String name, String email, String password, BigDecimal salary) {
}
