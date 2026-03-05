package com.devestudo.projeto_financas.entities;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component  //Cria uma instancia dessa classe, assim que a aplicação rodar
public class PasswordConfig {

    //MÉTODO PARA GERAR E CRIPTOGRAFAR A SENHA DO ADMIN
    private final PasswordEncoder passwordEncoder;

    public PasswordConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct //Executar esse método automaticamente, quando a aplicação rodar
    public void gerarSenhaAdmin() {
        System.out.println(
                "SENHA ADMIN (BCrypt): " + passwordEncoder.encode("root1526")
        );
    }


}
