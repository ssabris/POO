package com.example.demo;

public class Recepcionista {
    String nome;
    String cpf;
    String telefone;
    String senha;

    public void acessar(){}

    public String getNome() { return nome; }
    public void setNome(String nome) throws Exception {
         if(nome==null || nome.length()<=0) throw new Exception("Informe o nome!");
        this.nome = nome;
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Recepcionista(String nome, String cpf, String telefone, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.senha = senha;
    }

    public Recepcionista() {}

    public void mostrar() {
        var s = "Recepcionista [getNome()=" + getNome() + ", getCpf()=" + getCpf() + "]";
        System.out.println(s);
    }
}