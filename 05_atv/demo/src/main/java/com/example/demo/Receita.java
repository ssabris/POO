package com.example.demo;

public class Receita {
    private Consulta consulta; // Composição
    private String data;
    private String descritivo;

    public void preescrever(){}
    public void consultar(){}

    public Receita(Consulta consulta, String data, String descritivo) {
        this.consulta = consulta;
        this.data = data;
        this.descritivo = descritivo;
    }
    public Receita() {}
}