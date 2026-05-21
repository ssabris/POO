package ui;

import dao.FuncionarioDAO;
import model.Funcionario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PainelFuncionario extends JPanel {

    private final FuncionarioDAO dao = new FuncionarioDAO();

    private final JTextField txtNome  = new JTextField();
    private final JTextField txtCargo = new JTextField();
    private final JButton btnSalvar   = new JButton("Salvar");
    private final JButton btnExcluir  = new JButton("Excluir Selecionado");
    private final JButton btnAtualizar= new JButton("Atualizar Lista");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Nome", "Cargo", "Identificação"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelFuncionario() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Nome:")); form.add(txtNome);
        form.add(new JLabel("Cargo:")); form.add(txtCargo);

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
            if (txtNome.getText().isBlank() || txtCargo.getText().isBlank()) {
                throw new IllegalArgumentException("Preencha Nome e Cargo.");
            }
            Funcionario f = new Funcionario(0, txtNome.getText().trim(), txtCargo.getText().trim());
            dao.salvar(f);
            JOptionPane.showMessageDialog(this, "Funcionário salvo com sucesso!");
            txtNome.setText(""); txtCargo.setText("");
            carregarTabela();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        int confirma = JOptionPane.showConfirmDialog(this,
                "Excluir funcionário ID " + id + "?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
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
            List<Funcionario> lista = dao.listarTodos();
            for (Funcionario f : lista) {
                modeloTabela.addRow(new Object[]{f.getId(), f.getNome(), f.getCargo(), f.getIdentificacao()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
