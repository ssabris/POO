package ui;

import javax.swing.*;

/**
 * Janela principal do sistema de biblioteca.
 * Cada aba delega para um painel especializado.
 */
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema de Biblioteca - P2");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Leitores",      new PainelLeitor());
        abas.addTab("Funcionários",  new PainelFuncionario());
        abas.addTab("Obras",         new PainelObra());
        abas.addTab("Cópias",        new PainelCopia());
        abas.addTab("Empréstimos",   new PainelEmprestimo());
        abas.addTab("Devoluções",    new PainelDevolucao());
        abas.addTab("Reservas",      new PainelReserva());

        add(abas);
    }
}
