package com.devestudo.projeto_financas.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//CLASSE DE CONFIGURAÇÃO DO SWAGGER

/*Essa classe apenas ensina o Swagger:
-Que sua API usa JWT
-Onde o token deve ser enviado
-Qual é o formato do token*
Como montar o botão Authorize */


@Configuration
public class SwaggerConfig {
    String schemeName = "bearerAuth";
    String bearerFormat = "JWT";
    String scheme = "bearer";
    @Bean
    public OpenAPI caseOpenAPI() { //Objeto do swagger
        return new OpenAPI() // Cria um objeto OpenAPI
                .addSecurityItem(new SecurityRequirement() // Define que a API exige segurança (SecurityRequirement diz:“Essa API usa um schame de segurança”)
                        .addList(schemeName)).components(new Components() // referencia o nome do esquema de segurança que será definido abaixo.
                        .addSecuritySchemes(
                                schemeName, new SecurityScheme()
                                        .name(schemeName) // nome que fica no balão de autenticação no swegger
                                        .type(SecurityScheme.Type.HTTP) //Define que o esquema é do tipo HTTP
                                        .bearerFormat(bearerFormat) // serve para documentar o formato tipo -> Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
                                        .in(SecurityScheme.In.HEADER) // informa que será enviado no header da requisição
                                        .scheme(scheme) // define o tipo da requisição que é "Bearer"
                        )
                )
                .info(new Info()
                        .title("API de controle de finanças")
                        .description("Contém informações das requisições")
                        .version("1.0")
                );
    }
}