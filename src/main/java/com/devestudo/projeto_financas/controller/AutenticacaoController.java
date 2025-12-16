package com.devestudo.projeto_financas.controller;

import com.devestudo.projeto_financas.entities.dtos.AuthDTo;
import com.devestudo.projeto_financas.services.AutenticacaoService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private AuthenticationManager authenticationManager;

    private AutenticacaoService autenticacaoService;

    //Construtor para fazer a injeção de dependencia


    public AutenticacaoController(AuthenticationManager authenticationManager, AutenticacaoService autenticacaoService) {
        this.authenticationManager = authenticationManager;
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping
    public String auth(@RequestBody AuthDTo authDTo){

        //UsernamePasswordAuthenticationToken - precisamos utilizar ele que o sprink consiga fazer a autentição
        var userAutenticationToken =  new UsernamePasswordAuthenticationToken(authDTo.email(), authDTo.passwordUser());

        authenticationManager.authenticate(userAutenticationToken);

        return autenticacaoService.obterToken(authDTo);

    }
}
