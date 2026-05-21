package ui;

import dao.EmprestimoDAO;
import model.Emprestimo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PainelDevolucao extends JPanel {

    private final EmprestimoDAO dao = new EmprestimoDAO();

    private final JButton btnDevolver   = new JButton("Registrar Devolução");
    private final JButton btnAtualizar  = new JButton("Atualizar Lista");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID Empr.", "Obra", "Cópia (CB)", "Leitor", "Data Empréstimo"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelDevolucao() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel instrucao = new JLabel("Selecione um empréstimo em aberto para registrar a devolução:");

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnDevolver); botoes.add(btnAtualizar);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(instrucao, BorderLayout.NORTH);
        topo.add(botoes, BorderLayout.SOUTH);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnDevolver.addActionListener(e -> devolver());
        btnAtualizar.addActionListener(e -> carregarTabela());

        carregarTabela();
    }

    private void devolver() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idEmprestimo = (int) modeloTabela.getValueAt(linha, 0);
        if (JOptionPane.showConfirmDialog(this,
                "Confirmar devolução do empréstimo ID " + idEmprestimo + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                dao.registrarDevolucao(idEmprestimo, LocalDate.now());
                JOptionPane.showMessageDialog(this, "Devolução registrada com sucesso!");
                carregarTabela();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao registrar devolução: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarTabela() {
        try {
            modeloTabela.setRowCount(0);
            List<Emprestimo> lista = dao.listarEmAberto();
            for (Emprestimo e : lista) {
                modeloTabela.addRow(new Object[]{
                        e.getId(),
                        e.getCopia().getObra().getTitulo(),
                        e.getCopia().getCodigoBarras(),
                        e.getLeitor().getNome(),
                        e.getDataEmprestimo()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar empréstimos: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
