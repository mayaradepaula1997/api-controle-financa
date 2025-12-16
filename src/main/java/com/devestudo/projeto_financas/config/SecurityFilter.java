package com.devestudo.projeto_financas.config;

import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.repository.UserRepository;
import com.devestudo.projeto_financas.services.AutenticacaoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Classe que vai interceptar, vai extrair ot otken, validar e lê o usuário que está dentro do token
//SE TUDO ESTIVER OK O SPRING SECURITY CRIA UM OBJETO "AUTHENTICATION"

@Component     //Componente da nossa aplicação
public class SecurityFilter  extends OncePerRequestFilter { //A cada requisição eu quero que passe aqui antes

    private AutenticacaoService autenticacaoService;

    private UserRepository userRepository;

    public SecurityFilter(AutenticacaoService autenticacaoService, UserRepository userRepository) {
        this.autenticacaoService = autenticacaoService;
        this.userRepository = userRepository;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extrairTokenHeader(request);// Chama o método que vai extrair o token

        if (token != null) {

            // Recebo o token, valido o token, e dps busco o usuário no bd pelo email
            String login =  autenticacaoService.validarTokenJwt(token);
            User user = userRepository.findByEmail(login);

            var autentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            //Armazenar o token dentro da sessão / sessão é mais ou menos o tempo dele na aplicação
            SecurityContextHolder.getContext().setAuthentication(autentication);

        }

        filterChain.doFilter(request, response);  //Chama o proximo, autorizou o request

    }

    //Método que vai extrair o token do nosso HEADER
    public String extrairTokenHeader(HttpServletRequest request) {

        var authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return null;
        }

        if (!authHeader.split(" ")[0].equals("Bearer")) {
            return null;
        }

        return authHeader.split(" ")[1];    //Se encontrar o token, devolve esse token

    }

}
