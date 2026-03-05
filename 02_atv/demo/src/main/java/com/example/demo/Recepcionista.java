package com.example.demo;

public class Recepcionista {
    String nome;
    String cpf;
    String telefone;
    String senha;

    public void acessar(){};

    public void mostrar(){
        System.out.println("---- Dados: Recepcionista ----");
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("Telefone: " + telefone);
        System.out.println("Senha: " + senha);
    }
}
