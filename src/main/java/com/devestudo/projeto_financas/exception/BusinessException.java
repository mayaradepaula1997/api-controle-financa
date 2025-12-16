package com.devestudo.projeto_financas.exception;

//CLASSE USADA QUANDO UMA REGRA DE NÉGOCIO É VIOLADA (Categoria já cadastrada, somente o dono pode cadastrar...)

public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}
