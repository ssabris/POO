package com.example.demo;

public class Receita extends Prodecimento {
    
    public Receita() {}

    public Receita(String data, String descritivo) {
        this.data = data;
        this.descritivo = descritivo;
    }

    // Polimorfismo: implementando o consultar
    @Override
    public void consultar() {
        System.out.println("[RECEITA] Data: " + this.data + " | Medicamento: " + this.descritivo);
    }
    
    public void preescrever() {
        System.out.println("Receita prescrita com sucesso!");
    }
}