package com.example.demo; 
import java.util.List;
import java.util.ArrayList;

public class Agenda {
    private String data;
    private String hora;
    // Agregação de Consultas conforme o diagrama
    private List<Consulta> consultas = new ArrayList<>();

    public void consultar(){}

    public String getData() { return data; }
    public void setData(String data) throws ExceptionClinica {
        if(data==null) throw new ExceptionClinica("AS2345234", "A data nao pode ser nula!");
        this.data = data;
    }

    public void adicionarConsulta(Consulta c) {
        this.consultas.add(c);
    }

    public Agenda(String data, String hora) {
        this.data = data;
        this.hora = hora;
    }
    public Agenda() {}
}