package com.devestudo.projeto_financas.exception;


//CLASSE USADA QUANDO UM RECURSO NÃO É ENCONTRADO, HERDA DE RUNTIMEEXCEPTION E ASSIM NÃO PRECISA SE TRATADA COM TRY/CATCH

//CLASSE USADA QUANDO UM RECURSO (USUÁRIO, CATEGORIA...) NÃO É ENCONTRADO

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
