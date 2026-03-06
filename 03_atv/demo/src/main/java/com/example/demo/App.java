package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        // 1. Paciente (Usando Construtor com Parâmetros) [cite: 47, 62]
        // Ordem: codigo, nome, cpf, telefone, genero, idade, email
        Paciente p1 = new Paciente(1, "Sabrina", "123.456.789-00", "1199999", "Feminino", 25, "sabrina@email.com");
        p1.mostrar();

        // 2. Medico (Usando Setters para demonstrar Encapsulamento) [cite: 13, 26]
        Medico m1 = new Medico();
        m1.setNome("Dr. Silva");
        m1.setCrm("CRM/SP 123456");
        m1.setEspecialidade("Cardiologia");
        m1.setTelefone("1188888");
        m1.mostrar();

        // 3. Recepcionista
        Recepcionista r1 = new Recepcionista();
        r1.setNome("Ana");
        r1.mostrar();

        // 4. Agenda
        Agenda a1 = new Agenda();
        a1.setData("05/03/2026");
        a1.setHora("14:00");
        a1.setMedico(m1.getNome()); // Acessando via Getter [cite: 14, 20]
        a1.setPaciente(p1.getNome());
        a1.mostrar();

        // 5. Consulta (Passando os objetos medico e paciente)
        Consulta c1 = new Consulta();
        c1.setData("05/03/2026");
        c1.setHora("14:00");
        c1.setMedico(m1); // Define o objeto Medico inteiro
        c1.setPaciente(p1); // Define o objeto Paciente inteiro
        c1.setMotivo("Check-up anual");
        c1.mostrar();

        // 6. Receita
        Receita rec1 = new Receita("Consulta #01", "05/03/2026", "Vitamina D - 1 gota ao dia");
        rec1.mostrar();

        // 7. Exame
        Exame ex1 = new Exame();
        ex1.setData("10/03/2026");
        ex1.setDescritivo("Hemograma Completo");
        ex1.mostrar();
    }
}