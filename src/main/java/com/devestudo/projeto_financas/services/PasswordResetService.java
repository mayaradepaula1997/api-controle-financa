package com.devestudo.projeto_financas.services;

import com.devestudo.projeto_financas.entities.PasswordResetToken;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.request.ResetPasswordRequestDto;
import com.devestudo.projeto_financas.repository.TokenRepository;
import com.devestudo.projeto_financas.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

//Classe de serviço responsavel por gerar o token
@Service
public class PasswordResetService {

    private UserRepository userRepository;

    private TokenRepository passwordResetTokenRepository;

    private EmailService emailService;

    private PasswordEncoder passwordEncoder;

    public PasswordResetService(UserRepository userRepository, TokenRepository passwordResetTokenRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    //Método responsavel pela criação do token
    public void createResetToken(String email) {

        //Busca o usuário no banco de dados pelo e-mail
        userRepository.findByEmail(email).ifPresent(user -> {

            //Gera o token unico e aleátorio
            String token = UUID.randomUUID().toString();

            //Instância a classe de Token
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpirationDate(LocalDateTime.now().plusMinutes(30));
            resetToken.setUsed(false);

            //Salva o token no Banco de Dados
            passwordResetTokenRepository.save(resetToken);

            //Chama o serviço de email, para enviar o email
            emailService.sendResetPasswordEmail(user.getEmail(), token);

        });

    }

    //Método que faz a validação do token e troca a senha
    public void resetPassword(ResetPasswordRequestDto requestDto) {

        //Busca o token no banco de dados
        PasswordResetToken token = passwordResetTokenRepository.findByToken(requestDto.token())
                .orElseThrow(()-> new RuntimeException("Token inválido"));


        //Verifica se o token já foi utilizado
        if (token.isUsed()){
            throw new RuntimeException("Token já utilizado");
        }

        //Verifica se o token expirou - isBefore: verifica se passo o prazo
        if (token.getExpirationDate().isBefore(LocalDateTime.now())){
            throw  new RuntimeException("Token expirado");
        }

        //Recupera o usuário associado ao token, quem terá a senha alterada
        User user = token.getUser();
        //Faz a criptografia da nova senha
        user.setPasswordUser(passwordEncoder.encode(requestDto.newPassword()));

        //Salva a senha nova, no banco de dados
        userRepository.save(user);

        token.setUsed(true); //marca o token como usado
        passwordResetTokenRepository.save(token); //Salva as alterações do token no banco de dados

    }

}

