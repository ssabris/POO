package com.example.demo; 

import java.util.ArrayList;
import java.util.List;

public class Consulta extends Agenda {
    String motivo;
    String historico;
    List<Exame> exames = new ArrayList<>();
    List<Receita> receitas = new ArrayList<>();

    public Consulta() {}

    public Consulta(String hora, String data, Medico medico, Paciente paciente, String motivo, String historico, List<Receita> r, List<Exame> e){
        super(data, hora, medico, paciente);
        this.motivo = motivo;
        this.historico = historico;
        this.receitas = r;
        this.exames = e;
    }

    // Polimorfismo: sobrescrevendo o consultar da Agenda
    @Override
    public void consultar() {
        System.out.println("=== DETALHES DA CONSULTA ===");
        System.out.println("Data: " + this.data + " | Hora: " + this.hora);
        String nomeMedico = (this.medico != null) ? this.medico.getNome() : "Não atribuído";
        String nomePaciente = (this.paciente != null) ? this.paciente.getNome() : "Não atribuído";
        System.out.println("Médico: " + nomeMedico + " | Paciente: " + nomePaciente);
        System.out.println("Motivo: " + this.motivo);
        System.out.println("Histórico: " + this.historico);
    }

    public void marcar(){}
    public void cancelar(){}
    public void realizar(){}
    public void atualizar(){}
   
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) throws Exception  {
        if(motivo==null || motivo.length() <= 0 ) throw new Exception("Motivo da consulta é obrigatório!!");
        this.motivo = motivo;
    }
    public String getHistorico() { return historico; }
    public void setHistorico(String historico) { this.historico = historico; }
    public List<Exame> getExames() { return exames; }
    public void setExames(List<Exame> exames) { this.exames = exames; }
    public List<Receita> getReceitas() { return receitas; }
    public void setReceitas(List<Receita> receitas) { this.receitas = receitas; }
}