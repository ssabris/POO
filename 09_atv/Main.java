package com.fatec.poo;

import com.fatec.poo.bean.Medico;
import com.fatec.poo.bean.Recepcionista;
import com.fatec.poo.dao.MedicoDAO;
import com.fatec.poo.dao.RecepcionistaDAO;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        // =====================================================================
        //  MÉDICO
        // =====================================================================
        MedicoDAO medicoDAO = new MedicoDAO();

        // CREATE
        Medico m1 = new Medico();
        m1.setNome("Dr. Carlos Silva");
        m1.setEmail("carlos@clinica.com");
        m1.setSenha("senha123");
        m1.setTelefone("11999990001");
        m1.setDocumento("111.222.333-44");
        m1.setCrm("CRM/SP 12345");
        m1.setEspecialidade("Cardiologia");
        medicoDAO.create(m1);
        System.out.println("Médico inserido com sucesso.");

        // READ ALL
        List<Medico> medicos = medicoDAO.readAll();
        System.out.println("\n--- Lista de Médicos ---");
        for (Medico m : medicos) {
            System.out.printf("Cód: %d | %s | %s | %s%n",
                    m.getCodigo(), m.getNome(), m.getCrm(), m.getEspecialidade());
        }

        // READ por código
        Medico encontrado = medicoDAO.read(1);
        if (encontrado != null) {
            System.out.println("\nMédico encontrado: " + encontrado.getNome());

            // UPDATE
            encontrado.setEspecialidade("Neurologia");
            medicoDAO.update(encontrado);
            System.out.println("Médico atualizado para especialidade: " + encontrado.getEspecialidade());
        }

        // DELETE
        // medicoDAO.delete(1); // descomente para testar exclusão

        // =====================================================================
        //  RECEPCIONISTA
        // =====================================================================
        RecepcionistaDAO recepDAO = new RecepcionistaDAO();

        // CREATE
        Recepcionista r1 = new Recepcionista();
        r1.setNome("Ana Paula");
        r1.setEmail("ana@clinica.com");
        r1.setSenha("senha456");
        r1.setTelefone("11988880002");
        r1.setDocumento("555.666.777-88");
        r1.setTurno("MANHÃ");
        recepDAO.create(r1);
        System.out.println("\nRecepcionista inserida com sucesso.");

        // READ ALL
        List<Recepcionista> recepcionistas = recepDAO.readAll();
        System.out.println("\n--- Lista de Recepcionistas ---");
        for (Recepcionista r : recepcionistas) {
            System.out.printf("Cód: %d | %s | Turno: %s%n",
                    r.getCodigo(), r.getNome(), r.getTurno());
        }

        // READ por código
        Recepcionista recepEncontrada = recepDAO.read(1);
        if (recepEncontrada != null) {
            System.out.println("\nRecepcionista encontrada: " + recepEncontrada.getNome());

            // UPDATE
            recepEncontrada.setTurno("TARDE");
            recepDAO.update(recepEncontrada);
            System.out.println("Turno atualizado para: " + recepEncontrada.getTurno());
        }

        // DELETE
        // recepDAO.delete(1); // descomente para testar exclusão
    }
}
