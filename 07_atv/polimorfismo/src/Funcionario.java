package com.example.demo;

public abstract class Funcionario {
    String telefone;
    String senha;
    String nome;

    // Método abstrato que força as subclasses a implementarem suas próprias regras de acesso
    public abstract void acessar();

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}