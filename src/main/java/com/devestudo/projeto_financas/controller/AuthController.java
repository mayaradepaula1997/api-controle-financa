package com.devestudo.projeto_financas.controller;

import com.devestudo.projeto_financas.entities.dtos.request.ForgotPasswordRequestDto;
import com.devestudo.projeto_financas.entities.dtos.request.ResetPasswordRequestDto;
import com.devestudo.projeto_financas.services.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

//Classe de controller, onde o usuário sinaliza que esqueceu a senha e deseja redefinir
@RestController
@RequestMapping("/auth")
public class AuthController {

    private PasswordResetService passwordResetService;

    //TESTE
    private final JavaMailSender javaMailSender;

    public AuthController(PasswordResetService passwordResetService, JavaMailSender javaMailSender) {
        this.passwordResetService = passwordResetService;
        this.javaMailSender = javaMailSender;
    }

    //Esqueceu a senha
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequestDto request){
        passwordResetService.createResetToken(request.email());

        return ResponseEntity.ok().build();
    }


//    Chama o service que faz a validação do token e salva a nova senha
//    (busca o token no banco de dados, verifica se existe, verifica se expirou,
//     criptografa a nova senha, atualiza a senha do usuário , invalida o token)

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequestDto request){

        passwordResetService.resetPassword(request);
        return ResponseEntity.ok().build();
    }


    //TESTE DO ENVIO DO EMAIL:
    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail() {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cashflow <mayara.paula200@gmail.com>");
        message.setTo("mayara.paula200@gmail.com");
        message.setSubject("Teste SMTP Brevo");
        message.setText("Se você recebeu este e-mail, o SMTP está funcionando.");

        // usa diretamente o JavaMailSender
        javaMailSender.send(message);

        return ResponseEntity.ok("Email enviado");
    }
}
