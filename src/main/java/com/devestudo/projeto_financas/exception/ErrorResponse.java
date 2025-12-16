package com.devestudo.projeto_financas.exception;

//CLASSE QUE DEFINE O FORMATO DA RESPOSTA QUE O POSTMAN VAI RECEBER

import java.time.LocalDateTime;

public class ErrorResponse {

    //ATRIBUTOS
    private LocalDateTime timestamp;   //data e hora do erro

    private int status;                //status HTTP: 404, 500, 400

    private String error;              //mensagem clara para o usuário

    private  String path;              //endpoint que gerou o erro


    //CONSTRUTOR
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    //GETTER E SETTER


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
