package com.example.demo;

public class Exame {
    String consulta;
    String data;
    String descritivo;

    public void solicitar(){}
    public void consultar(){}
    
    public void mostrar(){
        System.out.println("---- Dados: Exame ----");
        System.out.println("Consulta: " + consulta);
        System.out.println("Data: " + data);
        System.out.println("Descritivo: " + descritivo);
    }
}
