package com.example.demo; 
import java.util.ArrayList;
import java.util.List;

public class Consulta {
    private String hora;
    private String data;
    private String motivo;
    private String historico;

    // Associações/Agregações do diagrama
    private Medico medico;
    private Paciente paciente;

    // Composições do diagrama (Receita e Exame pertencem à Consulta)
    private List<Receita> receitas = new ArrayList<>();
    private List<Exame> exames = new ArrayList<>();

    public void marcar(){}
    public void cancelar(){}
    public void consultar(){}
    public void realizar(){}
    public void atualizar(){}

    // Getters e Setters
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) throws Exception {
        if(motivo==null || motivo.length() <= 0 ) throw new Exception("Motivo obrigatorio!");
        this.motivo = motivo;
    }

    public Consulta(String hora, String data, Medico medico, Paciente paciente, String motivo, String historico) {
        this.hora = hora;
        this.data = data;
        this.medico = medico;
        this.paciente = paciente;
        this.motivo = motivo;
        this.historico = historico;
    }
    public Consulta() {}
}