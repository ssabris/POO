package com.example.demo;

public class Medico {
    private String nome;
    private String crm;
    private String telefone;
    private String especialidade;
    private String senha;

    // Construtor sem parâmetros
    public Medico() {
        this.nome = "";
        this.crm = "";
        this.telefone = "";
        this.especialidade = "";
        this.senha = "";
    }

    // Construtor para inicialização de cada atributo
    public Medico(String nome, String crm, String telefone, String especialidade, String senha) {
        this.nome = nome;
        this.crm = crm;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.senha = senha;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public void mostrar() {
        System.out.println("---- Dados: Médico ----");
        System.out.println("Nome: " + getNome());
        System.out.println("CRM: " + getCrm());
        System.out.println("Especialidade: " + getEspecialidade());
        System.out.println("Telefone: " + getTelefone());
    }
}