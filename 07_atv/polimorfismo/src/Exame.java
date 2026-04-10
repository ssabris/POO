package com.example.demo; 

public class Exame extends Prodecimento {
    
    public Exame() {}

    public Exame(String data, String descritivo) {
        this.data = data;
        this.descritivo = descritivo;
    }

    // Polimorfismo: implementando o consultar
    @Override
    public void consultar() {
        System.out.println("[EXAME] Data: " + this.data + " | Tipo: " + this.descritivo);
    }

    public void solicitar(){}
    public void mostrar() {}
}