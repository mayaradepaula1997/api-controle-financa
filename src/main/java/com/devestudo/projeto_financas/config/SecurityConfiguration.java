package com.devestudo.projeto_financas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration  //Sinaliza para o spring que essa é uma classe de configuração / define como o sistema deve funcionar
@EnableWebSecurity //Ativa o Spring Security para requeisições HTTP
@EnableMethodSecurity  //Permitir que os métodos sejam protegidos por regra de segurança - Só o admin pode acessar
public class SecurityConfiguration {


    private SecurityFilter securityFilter;

    public SecurityConfiguration(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean //Para que o Spring possa gerenciar esse método
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //servidor não guarda status
                .authorizeHttpRequests(authoriza-> authoriza

                        //Rotas Publicas
                        .requestMatchers("/auth/**").permitAll() //rota de autenticação
                        .requestMatchers(HttpMethod.POST,"/users").permitAll() //rota para criar usuario

                        //ROTAS PROTEGIDAS CATEGORIA E GASTO
                        .requestMatchers("/categories/**").authenticated()
                        .requestMatchers("/expenses/**").authenticated()

                        //USER - Somente Admin pode atualizar, listar e deletar
                        .requestMatchers("/users/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //Antes que o filtro de cima aconteça, para verificar se o token foi passado
                .build();  // ← importante: retorna o SecurityFilterChain

    }

    //Método que vai vai fazer a criptografia da senha
    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return  authenticationConfiguration.getAuthenticationManager();
    }
}


