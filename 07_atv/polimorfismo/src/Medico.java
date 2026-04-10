package com.example.demo;

public class Medico extends Funcionario {
    String especialidade;
    String crm;

    public Medico() {}

    public Medico(String nome, String crm, String telefone, String especialidade, String senha) {
        this.nome = nome;
        this.crm = crm;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.senha = senha;
    }

    // Polimorfismo: implementando o acessar do Médico
    @Override
    public void acessar() {
        System.out.println("=== ACESSO MÉDICO ===");
        System.out.println("Bem-vindo(a) Dr(a). " + this.nome);
        System.out.println("Especialidade: " + this.especialidade + " | CRM: " + this.crm);
    }

    public void realizarConsulta(Agenda agenda) throws Exception {
        Consulta c1 = new Consulta();
        c1.setData(agenda.getData());
        c1.setHora(agenda.getHora());
        c1.setMedico(this); // O médico da consulta é o próprio objeto instanciado
        c1.setPaciente(agenda.getPaciente());
        c1.setMotivo("Dor abdominal");
        c1.setHistorico("Apresenta dores na região do estômago, possível gastrite");
        
        c1.getExames().add(new Exame("01/04/2026", "Exame de sangue"));
        c1.getExames().add(new Exame("01/04/2026", "Endoscopia"));
        c1.getReceitas().add(new Receita("01/04/2026", "Buscopan"));
        
        // Testando o polimorfismo da Consulta
        c1.consultar();
        
        System.out.println("\n--- Procedimentos Solicitados ---");
        for(Exame e : c1.getExames()) { e.consultar(); }
        for(Receita r : c1.getReceitas()) { r.consultar(); }
    }

    public String getCrm() { return crm; }
    public void setCrm(String crm) throws Exception {
        if(crm==null || crm.length()<7) throw new Exception("CRM obrigatório!");
        this.crm = crm;
    }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public void mostrar() {}
}