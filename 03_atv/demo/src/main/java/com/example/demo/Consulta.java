package com.example.demo;

public class Consulta {
    private String hora;
    private String data;
    private Medico medico;
    private Paciente paciente;
    private String motivo;
    private String historico;

    public Consulta() {
        this.hora = "";
        this.data = "";
        this.medico = new Medico();
        this.paciente = new Paciente();
        this.motivo = "";
        this.historico = "";
    }

    public Consulta(String hora, String data, Medico medico, Paciente paciente, String motivo, String historico) {
        this.hora = hora;
        this.data = data;
        this.medico = medico;
        this.paciente = paciente;
        this.motivo = motivo;
        this.historico = historico;
    }

    // Getters e Setters [cite: 12]
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getHistorico() { return historico; }
    public void setHistorico(String historico) { this.historico = historico; }

    public void marcar() {}
    public void cancelar() {}
    public void realizar() {}

    public void mostrar() {
        System.out.println("---- Dados: Consulta ----");
        System.out.println("Data: " + getData() + " às " + getHora());
        System.out.println("Médico: " + (getMedico() != null ? getMedico().getNome() : "N/A"));
        System.out.println("Paciente: " + (getPaciente() != null ? getPaciente().getNome() : "N/A"));
        System.out.println("Motivo: " + getMotivo());
    }
}