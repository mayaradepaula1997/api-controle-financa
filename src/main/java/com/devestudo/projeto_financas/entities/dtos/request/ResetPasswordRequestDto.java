package com.devestudo.projeto_financas.entities.dtos.request;

public record ResetPasswordRequestDto(String token, String newPassword) {
}
