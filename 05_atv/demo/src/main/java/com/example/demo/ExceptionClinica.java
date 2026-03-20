package com.example.demo; 

public class ExceptionClinica extends Exception {
    private String codigo;
    private String mensagem;

    public ExceptionClinica(String codigo, String mensagem) {
        super(mensagem);
        this.codigo = codigo;
        this.mensagem = mensagem;
    }
}