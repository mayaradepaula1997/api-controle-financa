package com.devestudo.projeto_financas.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.AuthDTo;
import com.devestudo.projeto_financas.exception.BusinessException;
import com.devestudo.projeto_financas.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class AutenticacaoService implements UserDetailsService {


    private UserRepository userRepository;

    public AutenticacaoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Método para buscar o usuario no momento do login, para validar o usuário
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }


    public String obterToken (AuthDTo authDTo){

        User user = userRepository.findByEmail(authDTo.email());

        return gerarTokenJwt(user);
    }


    //Método para gerar o token JWT, recebe como parametro um USUÁRIO
    public String gerarTokenJwt(User user){

        try{
            //Cria a assinatura "Secret":É uma parte unica do nosso token, para incrementar - DEFINIR NA APLICAÇÃO
            Algorithm algorithm = Algorithm.HMAC256("my-secret"); //Garante que apenas seu servidor consiga gerar e validar o token

            //Configurações para gerar o token
            String token = JWT.create()
                    .withIssuer("projeto-financas")          //Quem gerou o token - se foi gerardo pela minha aplicação
                    .withSubject(user.getEmail())           //Quem é o dono do token
                    .withClaim("userId",user.getId())
                    .withExpiresAt(gerarDataExpiracao())     //Tempo de inspiração do token
                    .sign(algorithm);                        //Assinatura com  algoritmo da chave secreta

            return token;

        } catch(JWTCreationException e){
            throw  new BusinessException("Erro ao tentar gerar o token! " + e.getMessage());
        }
    }


    //Método que vai validar se o token passar é valido, esse método pode lançar um exceção
    public String validarTokenJwt (String token){  //Esse método retorna o login do usuario

        try {
            Algorithm algorithm = Algorithm.HMAC256("my-secret");

            return JWT.require(algorithm)
                    .withIssuer("projeto-financas")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e){
            throw new BusinessException("Token não é valido");

        }
    }

    //Método que gera o tempo de inspiração do token
    private Instant gerarDataExpiracao() {

        return LocalDateTime.now()   //Chama a hora atual
                .plusHours(8)        //Adiciona mais 8 horas
                .toInstant(ZoneOffset.of("-03:00"));  //Converte o time zone
    }
}
