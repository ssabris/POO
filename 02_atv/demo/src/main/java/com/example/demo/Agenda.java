package com.example.demo;

public class Agenda {
    String data;
    String hora;
    String medico;
    String paciente;

    public void consultar(){}

    public void mostrar(){
        System.out.println("---- Dados: Agenda ----");
        System.out.println("Data: " + data);
        System.out.println("Hora: " + hora);
        System.out.println("Médico: " + medico);
        System.out.println("Paciente: " + paciente);
    }
}
