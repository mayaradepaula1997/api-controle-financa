package com.devestudo.projeto_financas.repository;

import com.devestudo.projeto_financas.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken (String token);  //métodp para buscar o token no banco de dados
}
