package com.example.demo; 

import java.util.ArrayList;
import java.util.List;

public class Consulta extends Agenda {
    String motivo;
    String historico;
    List<Exame> exames = new ArrayList<>();
    List<Receita> receitas = new ArrayList<>();

    public List<Exame> getExames() {
        return exames;
    }
    public void setExames(List<Exame> exames) {
        this.exames = exames;
    }
    public List<Receita> getReceitas() {
        return receitas;
    }
    public void setReceitas(List<Receita> receitas) {
        this.receitas = receitas;
    }
    public void marcar(){};
    public void cancelar(){};
  
    public void realizar(){};
    public void atualizar(){}
   
    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) throws Exception  {
        if(motivo==null || motivo.length() <= 0 ) 
            throw  new Exception("Motivo da consulta e obrigatorio !!");
        this.motivo = motivo;
    }
    public String getHistorico() {
        return historico;
    }
    public void setHistorico(String historico) {
        this.historico = historico;
    }
    
    public Consulta(String hora, String data, Medico medico, Paciente paciente, String motivo, String historico, List<Receita> r, List<Exame> e){
        this.hora = hora;
        this.data = data;
        this.medico = medico;
        this.paciente = paciente;
        this.motivo = motivo;
        this.historico = historico;
        this.receitas = r;
        this.exames = e;
    }
    public Consulta() {
    }

    

}
