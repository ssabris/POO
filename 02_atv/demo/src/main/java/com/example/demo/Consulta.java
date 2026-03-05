package com.example.demo;

public class Consulta {
    String hora;
    String data;
    String medico;
    String paciente;
    String motivo;
    String historico;

    public void marcar(){};
    public void cancelar(){};
    public void consultar(){};
    public void realizar(){};
    public void atualizar(){};

    public void mostrar() {
        System.out.println("---- Dados: Consulta ----");
        System.out.println("Data: " + data);
        System.out.println("Hora: " + hora);
        // Como médico e paciente são objetos, pegamos o nome deles
        System.out.println("Médico: " + (medico != null ? medico.nome : "Não definido"));
        System.out.println("Paciente: " + (paciente != null ? paciente.getNome() : "Não definido"));
        System.out.println("Motivo: " + motivo);
        System.out.println("Histórico: " + historico);
    }
}
