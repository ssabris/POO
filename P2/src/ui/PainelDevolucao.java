package ui;

import dao.EmprestimoDAO;
import model.Emprestimo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * ALTERAÇÕES v2:
 *   - Tabela exibe data_emprestimo E data_prevista_devolucao
 *   - Coluna "Atrasado?" calculada a partir de LocalDate.now()
 *   - registrarDevolucao() agora também incrementa o estoque (via EmprestimoDAO)
 *   - Filtro em tempo real adicionado
 */
public class PainelDevolucao extends JPanel {

    private final EmprestimoDAO dao = new EmprestimoDAO();

    private final JButton btnDevolver  = new JButton("Registrar Devolução");
    private final JButton btnAtualizar = new JButton("Atualizar Lista");
    private final JTextField txtFiltro = new JTextField(); // NOVO

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Obra", "Leitor", "Data Empr.", "Prazo Devolução", "Atrasado?"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);
    private TableRowSorter<DefaultTableModel> sorter;

    public PainelDevolucao() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel instrucao = new JLabel("Selecione um empréstimo em aberto para registrar a devolução:");

        JPanel painelFiltro = new JPanel(new BorderLayout(5, 0));
        painelFiltro.add(new JLabel("Filtrar: "), BorderLayout.WEST);
        painelFiltro.add(txtFiltro, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnDevolver); botoes.add(btnAtualizar);

        JPanel topo = new JPanel(new BorderLayout(0, 6));
        topo.add(instrucao, BorderLayout.NORTH);
        topo.add(painelFiltro, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        sorter = new TableRowSorter<>(modeloTabela);
        tabela.setRowSorter(sorter);
        txtFiltro.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });

        btnDevolver.addActionListener(e -> devolver());
        btnAtualizar.addActionListener(e -> carregarTabela());

        carregarTabela();
    }

    private void filtrar() {
        String t = txtFiltro.getText().trim();
        sorter.setRowFilter(t.isEmpty() ? null : RowFilter.regexFilter("(?i)" + t));
    }

    private void devolver() {
        int linhaView = tabela.getSelectedRow();
        if (linhaView < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int linhaModel = tabela.convertRowIndexToModel(linhaView);
        int idEmprestimo = (int) modeloTabela.getValueAt(linhaModel, 0);
        String obra  = (String) modeloTabela.getValueAt(linhaModel, 1);

        if (JOptionPane.showConfirmDialog(this,
                "Confirmar devolução de \"" + obra + "\" (empréstimo ID " + idEmprestimo + ")?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                dao.registrarDevolucao(idEmprestimo, LocalDate.now());
                JOptionPane.showMessageDialog(this, "Devolução registrada! Estoque atualizado.");
                carregarTabela();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarTabela() {
        try {
            modeloTabela.setRowCount(0);
            List<Emprestimo> lista = dao.listarEmAberto();
            for (Emprestimo e : lista) {
                String atrasado = e.isAtrasado() ? "⚠ SIM" : "Não";
                modeloTabela.addRow(new Object[]{
                        e.getId(),
                        e.getObra().getTitulo(),
                        e.getLeitor().getNome(),
                        e.getDataEmprestimo(),
                        e.getDataPrevistaDevolucao(),
                        atrasado
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
