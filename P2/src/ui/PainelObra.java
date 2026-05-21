package ui;

import dao.ObraDAO;
import model.Obra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PainelObra extends JPanel {

    private final ObraDAO dao = new ObraDAO();

    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtAutor  = new JTextField();
    private final JButton btnSalvar    = new JButton("Salvar");
    private final JButton btnExcluir   = new JButton("Excluir Selecionada");
    private final JButton btnAtualizar = new JButton("Atualizar Lista");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Título", "Autor"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelObra() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Título:")); form.add(txtTitulo);
        form.add(new JLabel("Autor:"));  form.add(txtAutor);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnSalvar); botoes.add(btnExcluir); botoes.add(btnAtualizar);

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
            if (txtTitulo.getText().isBlank() || txtAutor.getText().isBlank()) {
                throw new IllegalArgumentException("Preencha Título e Autor.");
            }
            dao.salvar(new Obra(0, txtTitulo.getText().trim(), txtAutor.getText().trim()));
            JOptionPane.showMessageDialog(this, "Obra salva com sucesso!");
            txtTitulo.setText(""); txtAutor.setText("");
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
            JOptionPane.showMessageDialog(this, "Selecione uma obra.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        if (JOptionPane.showConfirmDialog(this, "Excluir obra ID " + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
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
            List<Obra> lista = dao.listarTodos();
            for (Obra o : lista) {
                modeloTabela.addRow(new Object[]{o.getId(), o.getTitulo(), o.getAutor()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
