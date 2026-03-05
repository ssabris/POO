package com.example.demo;

public class Receita {
    String consulta;
    String data;
    String descritivo;

    public void preescrever(){};
    public void consultar(){};

    public void mostrar(){
        System.out.println("---- Dados: Receita ----");
        System.out.println("Consulta: " + consulta);
        System.out.println("Data: " + data);
        System.out.println("Descritivo: " + descritivo);
    }
}
