package com.devestudo.projeto_financas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//SERVIÇO DE E-MAIL
@Service
public class EmailService {

    @Autowired
    private final JavaMailSender javaMailSender; //interface do spring usada para envio de e-mail

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    //Recebe como parametro o email e o token
    //O serviço de e-mail precisa saber para quem vai enviar o e-mail
    //O token serve para identificar com segurança o pedido de redefinição de senha e qual link de recuperação montar
    public void sendResetPasswordEmail(String email, String token){

        //Link de redefinição de senha, token vai na url como parametro
        String link =  "https://app-cash-flow.netlify.app/reset-password?token=" + token;

        try {
            //SimpleMailMessage: Usado para e-mails de textos puros
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("cashflow <mayara.paula200@gmail.com>");
            message.setTo(email); //Define o destinatario do e-mail
            message.setSubject("Redefinição de senha");  //Titulo do e-mail
            message.setText("Clique no link para redefinir sua senha:\n" + link);

            javaMailSender.send(message);  //Envio de fato do e-mail
            System.out.println("E-mail enviado com sucesso");


        } catch (Exception e){
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
