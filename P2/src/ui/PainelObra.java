package ui;

import dao.ObraDAO;
import model.Obra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.List;

/**
 * ALTERAÇÕES v2:
 *   - Campos codigoBarras e quantidadeTotal adicionados (substituem PainelCopia)
 *   - Tabela exibe disponibilidade e quantidade total
 *   - Filtro em tempo real com TableRowSorter
 *   - Edição: clique na tabela carrega dados; Salvar detecta INSERT vs UPDATE
 *   - Tratamento visual de código de barras duplicado
 */
public class PainelObra extends JPanel {

    private final ObraDAO dao = new ObraDAO();

    private final JTextField txtTitulo       = new JTextField();
    private final JTextField txtAutor        = new JTextField();
    private final JTextField txtCodigoBarras = new JTextField(); // NOVO
    private final JSpinner   spnQuantidade   = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1)); // NOVO
    private final JTextField txtFiltro       = new JTextField(); // NOVO

    private final JButton btnSalvar    = new JButton("Salvar");
    private final JButton btnCancelar  = new JButton("Cancelar Edição");
    private final JButton btnExcluir   = new JButton("Excluir Selecionada");
    private final JButton btnAtualizar = new JButton("Atualizar Lista");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Título", "Autor", "Cód. Barras", "Total", "Disponível"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);
    private TableRowSorter<DefaultTableModel> sorter;

    private int idEmEdicao = -1;

    public PainelObra() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(new JLabel("Título:"));          form.add(txtTitulo);
        form.add(new JLabel("Autor:"));           form.add(txtAutor);
        form.add(new JLabel("Código de Barras:")); form.add(txtCodigoBarras);
        form.add(new JLabel("Qtd. Exemplares:")); form.add(spnQuantidade);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnSalvar);
        botoes.add(btnCancelar);
        botoes.add(btnExcluir);
        botoes.add(btnAtualizar);

        JPanel painelFiltro = new JPanel(new BorderLayout(5, 0));
        painelFiltro.add(new JLabel("Filtrar: "), BorderLayout.WEST);
        painelFiltro.add(txtFiltro, BorderLayout.CENTER);

        JPanel topo = new JPanel(new BorderLayout(0, 6));
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        JPanel topoCompleto = new JPanel(new BorderLayout(0, 6));
        topoCompleto.add(topo, BorderLayout.NORTH);
        topoCompleto.add(painelFiltro, BorderLayout.SOUTH);

        add(topoCompleto, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        sorter = new TableRowSorter<>(modeloTabela);
        tabela.setRowSorter(sorter);
        txtFiltro.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarParaEdicao();
        });

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> cancelarEdicao());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarTabela());

        cancelarEdicao();
        carregarTabela();
    }

    private void filtrar() {
        String t = txtFiltro.getText().trim();
        sorter.setRowFilter(t.isEmpty() ? null : RowFilter.regexFilter("(?i)" + t));
    }

    private void carregarParaEdicao() {
        int linhaView = tabela.getSelectedRow();
        if (linhaView < 0) return;
        int linhaModel = tabela.convertRowIndexToModel(linhaView);

        idEmEdicao = (int) modeloTabela.getValueAt(linhaModel, 0);
        txtTitulo.setText((String) modeloTabela.getValueAt(linhaModel, 1));
        txtAutor.setText((String) modeloTabela.getValueAt(linhaModel, 2));
        txtCodigoBarras.setText((String) modeloTabela.getValueAt(linhaModel, 3));
        spnQuantidade.setValue(modeloTabela.getValueAt(linhaModel, 4));
        btnSalvar.setText("Atualizar");
        btnCancelar.setEnabled(true);
    }

    private void cancelarEdicao() {
        idEmEdicao = -1;
        txtTitulo.setText(""); txtAutor.setText(""); txtCodigoBarras.setText("");
        spnQuantidade.setValue(1);
        btnSalvar.setText("Salvar");
        btnCancelar.setEnabled(false);
        tabela.clearSelection();
    }

    private void salvar() {
        try {
            if (txtTitulo.getText().isBlank() || txtAutor.getText().isBlank()
                    || txtCodigoBarras.getText().isBlank()) {
                throw new IllegalArgumentException("Preencha Título, Autor e Código de Barras.");
            }
            int qtd = (int) spnQuantidade.getValue();
            Obra obra = new Obra(
                    idEmEdicao > 0 ? idEmEdicao : 0,
                    txtTitulo.getText().trim(),
                    txtAutor.getText().trim(),
                    txtCodigoBarras.getText().trim(),
                    qtd
            );

            if (idEmEdicao > 0) {
                dao.atualizar(obra);
                JOptionPane.showMessageDialog(this, "Obra atualizada com sucesso!");
            } else {
                dao.salvar(obra);
                JOptionPane.showMessageDialog(this, "Obra salva com sucesso!");
            }
            cancelarEdicao();
            carregarTabela();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this,
                    "Código de barras já cadastrado. Utilize um código diferente.",
                    "Duplicado", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int linhaView = tabela.getSelectedRow();
        if (linhaView < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma obra.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int linhaModel = tabela.convertRowIndexToModel(linhaView);
        int id = (int) modeloTabela.getValueAt(linhaModel, 0);
        String titulo = (String) modeloTabela.getValueAt(linhaModel, 1);

        if (JOptionPane.showConfirmDialog(this, "Excluir \"" + titulo + "\" (ID " + id + ")?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                dao.excluir(id);
                cancelarEdicao();
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
                modeloTabela.addRow(new Object[]{
                        o.getId(), o.getTitulo(), o.getAutor(),
                        o.getCodigoBarras(), o.getQuantidadeTotal(), o.getQuantidadeDisponivel()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
