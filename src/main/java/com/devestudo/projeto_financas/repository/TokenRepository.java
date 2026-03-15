package com.devestudo.projeto_financas.repository;

import com.devestudo.projeto_financas.entities.PasswordResetToken;
import com.devestudo.projeto_financas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken (String token);  //método para buscar o token no banco de dados


    //Buscar o usuário dentro do banco de dados na tabela de token de recuperação de senha
    Optional<PasswordResetToken> findByUser(User user);
}
