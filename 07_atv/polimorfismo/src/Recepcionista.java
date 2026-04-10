package com.example.demo;

public class Recepcionista extends Funcionario {
    String cpf;

    // Polimorfismo: implementando o acessar da Recepcionista
    @Override
    public void acessar() {
        System.out.println("=== ACESSO RECEPÇÃO ===");
        System.out.println("Acesso liberado para recepcionista: " + this.nome);
        System.out.println("CPF registrado: " + this.cpf);
    }
    
    public void cadastrar() {}
    
    public Agenda marcarAgenda() { 
        Agenda agenda = new Agenda();
        try {
            agenda.setData("01/04/2026");
            agenda.setHora("14:30");
            Paciente p = new Paciente(1, "Carlos Mendes", "carlos@email.com");
            agenda.setPaciente(p);
        } catch (Exception e) {
            System.out.println("Erro ao marcar agenda: " + e.getMessage());
        }
        return agenda; 
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}