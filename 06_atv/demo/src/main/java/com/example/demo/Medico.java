package com.example.demo;

public class Medico extends Funcionario {
    String especialidade;
    String crm;
    public void acessar(){
        //TODO
    }
    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) throws Exception{
        if(crm==null || crm.length()<7) throw new Exception("Crm obrigatorio !");
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }


    public Medico(String nome, String crm, String telefone, String especialidade, String senha) {
        this.nome = nome;
        this.crm = crm;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.senha = senha;
    }

    public Medico() {
    }
    public void mostrar() {
    }

    public void realizarConsulta(Agenda agenda) throws Exception{
        Consulta c1 = new Consulta();
        c1.setData(agenda.getData());
        c1.setHora(agenda.getHora());
        c1.setMedico(agenda.getMedico());
        c1.setPaciente(agenda.getPaciente());
        c1.setMotivo("Dor abdominal");
        c1.setHistorico("apresenta dores na região do estomago, possivel gastrite");
        Exame e1 = new Exame("01/04/2026", "Exame de sangue");
        c1.getExames().add(e1);
        c1.getExames().add(new Exame("01/04/2026", "Endoscopia"));
        c1.getReceitas().add(new Receita("01/04/2026", "Buscopan"));
        c1.mostrar();
    }


    
}