package com.example.demo; 

public class Exame {
    private Consulta consulta; // Mudado para objeto para refletir a Composição
    private String data;
    private String descritivo;

    public void solicitar(){}
    public void consultar(){}

    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }

    public void setDescritivo(String descritivo) throws Exception {
        if(descritivo==null || descritivo.length()<=0) throw new Exception("Informe o descritivo");
        this.descritivo = descritivo;
    }

    public Exame(Consulta consulta, String data, String descritivo) {
        this.consulta = consulta;
        this.data = data;
        this.descritivo = descritivo;
    }
    public Exame() {}
}