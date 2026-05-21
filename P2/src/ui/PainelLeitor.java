package ui;

import dao.LeitorDAO;
import model.Leitor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PainelLeitor extends JPanel {

    private final LeitorDAO dao = new LeitorDAO();

    private final JTextField txtNome      = new JTextField();
    private final JTextField txtMatricula = new JTextField();
    private final JButton    btnSalvar    = new JButton("Salvar");
    private final JButton    btnExcluir   = new JButton("Excluir Selecionado");
    private final JButton    btnAtualizar = new JButton("Atualizar Lista");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Nome", "Matrícula", "Identificação"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelLeitor() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de formulário
        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Nome:"));        form.add(txtNome);
        form.add(new JLabel("Matrícula:"));   form.add(txtMatricula);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnSalvar);
        botoes.add(btnExcluir);
        botoes.add(btnAtualizar);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarTabela());

        carregarTabela();
    }

    private void salvar() {
        try {
            // Exceções de validação tratadas na UI — exigência do enunciado
            if (txtNome.getText().isBlank() || txtMatricula.getText().isBlank()) {
                throw new IllegalArgumentException("Preencha Nome e Matrícula.");
            }
            Leitor leitor = new Leitor(0, txtNome.getText().trim(), txtMatricula.getText().trim());
            dao.salvar(leitor);
            JOptionPane.showMessageDialog(this, "Leitor salvo com sucesso!");
            txtNome.setText("");
            txtMatricula.setText("");
            carregarTabela();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um leitor na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        int confirma = JOptionPane.showConfirmDialog(this,
                "Excluir leitor ID " + id + "?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                dao.excluir(id);
                carregarTabela();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarTabela() {
        try {
            modeloTabela.setRowCount(0);
            List<Leitor> lista = dao.listarTodos();
            for (Leitor l : lista) {
                // getIdentificacao() demonstra polimorfismo em ação na UI
                modeloTabela.addRow(new Object[]{l.getId(), l.getNome(), l.getMatricula(), l.getIdentificacao()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar leitores: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
