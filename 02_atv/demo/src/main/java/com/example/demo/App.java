package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		// 1. Paciente
        Paciente p1 = new Paciente();
        p1.setNome("Sabrina");
        p1.setCpf("123.456.789-00");
        p1.setIdade(25);
        p1.mostrar();

        // 2. Medico
        Medico m1 = new Medico();
        m1.nome = "Dr. Silva";
        m1.crm = "CRM/SP 123456";
        m1.especialidade = "Cardiologia";
        m1.mostrar();

        // 3. Recepcionista
        Recepcionista r1 = new Recepcionista();
        r1.nome = "Ana";
        r1.mostrar();

        // 4. Agenda
        Agenda a1 = new Agenda();
        a1.data = "05/03/2026";
        a1.hora = "14:00";
        a1.medico = m1.nome;
        a1.mostrar();

        // 5. Consulta
        Consulta c1 = new Consulta();
        c1.paciente = p1.getNome();
        c1.medico = m1.nome;
        c1.motivo = "Check-up anual";
        c1.mostrar();

        // 6. Receita
        Receita rec1 = new Receita();
        rec1.data = "05/03/2026";
        rec1.descritivo = "Vitamina D - 1 gota ao dia";
        rec1.mostrar();

        // 7. Exame
        Exame ex1 = new Exame();
        ex1.data = "10/03/2026";
        ex1.descritivo = "Hemograma Completo";
        ex1.mostrar();
	}
}



