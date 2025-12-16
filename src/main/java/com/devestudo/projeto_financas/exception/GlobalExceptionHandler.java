package com.devestudo.projeto_financas.exception;

//CLASSE QUE MANIPULA E CONTROLA AS EXCEÇÕES

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice //esse anotação e como se fosse um guardião que intercepta exceções lançadas no controller
public class GlobalExceptionHandler{


    //Quando o recurso não é encontrado, esse método é chamado
    @ExceptionHandler(ResourceNotFoundException.class) //Quando for lançada uma exceção do tipo NotFound executa esse método
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException (ResourceNotFoundException ex, WebRequest request){ //ResourceNotFoundException: representa a exceção capturada, WebRequest: contém a URl que foi chamada


        ErrorResponse error = new ErrorResponse(

                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false)

        );

        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }


    //Quando violar alguma regra de negócio
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException (BusinessException ex, WebRequest request){

        ErrorResponse error = new ErrorResponse(

                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //Quando ocorrer algo inesperado
    @ExceptionHandler(Exception.class)//Captura qualquer exceção não tratada acima, qualquer erro inesperado
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request){

        ErrorResponse error = new ErrorResponse(

                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno no servidor",
                request.getDescription(false)
        );

        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);


    }

}
