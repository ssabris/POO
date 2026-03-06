package com.example.demo;

public class Receita {
    private String consulta;
    private String data;
    private String descritivo;

    // Construtor sem parâmetros [cite: 64]
    public Receita() {
        this.consulta = "";
        this.data = "";
        this.descritivo = "";
    }

    // Construtor com parâmetros para inicialização [cite: 53, 59]
    public Receita(String consulta, String data, String descritivo) {
        this.consulta = consulta;
        this.data = data;
        this.descritivo = descritivo;
    }

    // Getters e Setters [cite: 13, 14, 20, 23]
    public String getConsulta() { return consulta; }
    public void setConsulta(String consulta) { this.consulta = consulta; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getDescritivo() { return descritivo; }
    public void setDescritivo(String descritivo) { this.descritivo = descritivo; }

    public void preescrever() {}
    public void consultar() {}

    public void mostrar() {
        System.out.println("---- Dados: Receita ----");
        System.out.println("Consulta: " + getConsulta());
        System.out.println("Data: " + getData());
        System.out.println("Descritivo: " + getDescritivo());
    }
}