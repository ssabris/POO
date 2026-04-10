package com.example.demo;

public abstract class Prodecimento {
    String data;
    String descritivo;

    // Método abstrato
    public abstract void consultar();

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getDescritivo() { return descritivo; }
    public void setDescritivo(String descritivo) throws Exception {
         if(descritivo==null || descritivo.length()<=0) throw new Exception("Informe o descritivo");
         this.descritivo = descritivo;
    }
}