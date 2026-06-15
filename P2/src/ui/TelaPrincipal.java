package ui;

import javax.swing.*;

/**
 * ALTERAÇÕES v2:
 *   - Aba "Cópias" removida (Copia foi unificada com Obra)
 *   - Obras agora gerencia código de barras e quantidades diretamente
 */
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema de Biblioteca - P2");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Leitores",     new PainelLeitor());
        abas.addTab("Funcionários", new PainelFuncionario());
        abas.addTab("Obras",        new PainelObra());
        // "Cópias" removida — gerenciado agora dentro de Obras
        abas.addTab("Empréstimos",  new PainelEmprestimo());
        abas.addTab("Devoluções",   new PainelDevolucao());
        abas.addTab("Reservas",     new PainelReserva());

        add(abas);
    }
}
