package com.example.demo;

public class App {
    public static void main(String[] args) {
        try {
            // 1. Criando instâncias e testando o polimorfismo de Funcionário
            Recepcionista maria = new Recepcionista();
            maria.setNome("Maria Silva");
            maria.setCpf("111.222.333-44");
            
            Medico joao = new Medico();
            joao.setNome("João Souza");
            joao.setEspecialidade("Gastroenterologista");
            joao.setCrm("1234567-SP");

            // O mesmo método "acessar()" responde de forma diferente!
            maria.acessar();
            System.out.println("-------------------------");
            joao.acessar();
            System.out.println("-------------------------\n");

            // 2. Simulando o fluxo
            Agenda agenda20260401 = maria.marcarAgenda();
            joao.realizarConsulta(agenda20260401);     

        } catch(Exception e) {
            System.out.println("Ocorreu um erro: " + e.getMessage());    
        }
    }
}