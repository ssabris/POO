package ui;

import dao.CopiaDAO;
import dao.ObraDAO;
import model.Copia;
import model.Obra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class PainelCopia extends JPanel {

    private final CopiaDAO copiaDAO = new CopiaDAO();
    private final ObraDAO  obraDAO  = new ObraDAO();

    private final JTextField txtCodigoBarras = new JTextField();
    private final JComboBox<Obra> cbObra     = new JComboBox<>();
    private final JButton btnSalvar          = new JButton("Salvar");
    private final JButton btnExcluir         = new JButton("Excluir Selecionada");
    private final JButton btnAtualizar        = new JButton("Atualizar Lista");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Código de Barras", "Obra", "Autor"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelCopia() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Código de Barras:")); form.add(txtCodigoBarras);
        form.add(new JLabel("Obra:"));             form.add(cbObra);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnSalvar); botoes.add(btnExcluir); botoes.add(btnAtualizar);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarTudo());

        carregarTudo();
    }

    private void salvar() {
        try {
            if (txtCodigoBarras.getText().isBlank()) {
                throw new IllegalArgumentException("Informe o código de barras.");
            }
            Obra obraSelecionada = (Obra) cbObra.getSelectedItem();
            if (obraSelecionada == null) {
                throw new IllegalArgumentException("Selecione uma obra. Cadastre obras antes de criar cópias.");
            }
            Copia copia = new Copia(0, txtCodigoBarras.getText().trim(), obraSelecionada);
            copiaDAO.salvar(copia);
            JOptionPane.showMessageDialog(this, "Cópia salva!");
            txtCodigoBarras.setText("");
            carregarTudo();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma cópia.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        if (JOptionPane.showConfirmDialog(this, "Excluir cópia ID " + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                copiaDAO.excluir(id);
                carregarTudo();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarTudo() {
        carregarComboObras();
        carregarTabela();
    }

    private void carregarComboObras() {
        try {
            cbObra.removeAllItems();
            for (Obra o : obraDAO.listarTodos()) {
                cbObra.addItem(o); // usa toString() de Obra para exibir
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar obras: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        try {
            modeloTabela.setRowCount(0);
            for (Copia c : copiaDAO.listarTodos()) {
                modeloTabela.addRow(new Object[]{
                        c.getId(), c.getCodigoBarras(),
                        c.getObra().getTitulo(), c.getObra().getAutor()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cópias: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
