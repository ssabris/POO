package com.example.demo;

public class Recepcionista extends Funcionario {
    
    String cpf;


    public void acessar(){}


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Recepcionista(String nome, String cpf, String telefone, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.senha = senha;
    }

    public Recepcionista() {
    }
   
    public void mostrar() {
       
    };
    public Agenda marcarAgenda() throws Exception{
        Paciente p1 = new Paciente();
        p1.setCodigo(1);
        p1.setEmail("jose@norton.net.br");
        p1.setNome("Jose da silva");
    
        Medico m1 = new Medico();
        m1.setCrm("234234234");
        m1.setEspecialidade("Geriatra");
        m1.setNome("Maria Antonieta");
        m1.setTelefone("2344-2344");

        Agenda a1 = new Agenda();
        a1.setData("01/04/2026");
        a1.setHora("10:20");
        a1.setMedico(m1);
        a1.setPaciente(p1);
        return a1;
    }
    
}
