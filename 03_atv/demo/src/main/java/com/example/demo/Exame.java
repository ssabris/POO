package com.example.demo;

public class Exame {
    private String consulta;
    private String data;
    private String descritivo;

    public Exame() {
        this.consulta = "";
        this.data = "";
        this.descritivo = "";
    }

    public Exame(String consulta, String data, String descritivo) {
        this.consulta = consulta;
        this.data = data;
        this.descritivo = descritivo;
    }

    public String getConsulta() { return consulta; }
    public void setConsulta(String consulta) { this.consulta = consulta; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getDescritivo() { return descritivo; }
    public void setDescritivo(String descritivo) { this.descritivo = descritivo; }

    public void mostrar() {
        System.out.println("---- Dados: Exame ----");
        System.out.println("Consulta Ref: " + getConsulta());
        System.out.println("Data: " + getData());
        System.out.println("Descrição: " + getDescritivo());
    }
}