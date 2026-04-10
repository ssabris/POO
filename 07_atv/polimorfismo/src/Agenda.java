package com.example.demo; 

public class Agenda {
    String data;
    String hora;
    Medico medico;
    Paciente paciente;

    public Agenda() {}

    public Agenda(String data, String hora, Medico medico, Paciente paciente) {
        this.data = data;
        this.hora = hora;
        this.medico = medico;
        this.paciente = paciente;
    }

    // Método que será sobrescrito
    public void consultar() {
        System.out.println("Consultando registro na Agenda para o dia: " + this.data + " às " + this.hora);
    }

    public String getData() { return data; }
    public void setData(String data) throws ExceptionClinica {
        if(data==null){ throw new ExceptionClinica("AS2345234", "A data não pode ser nula!"); } 
        else { this.data = data; }
    }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public void mostrar() {
       System.out.println("Agenda [getData()=" + getData() + ", getHora()=" + getHora() + "]"); 
       if(getMedico() != null) getMedico().mostrar();
       if(getPaciente() != null) getPaciente().mostrar();
    }
}