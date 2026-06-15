package ui;

import dao.FuncionarioDAO;
import model.Funcionario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.List;

/**
 * ALTERAÇÕES v2:
 *   - Campo Matrícula Func. (matricula_func) no formulário — substitui CPF
 *   - Filtro em tempo real com TableRowSorter
 *   - Edição: clique na linha carrega dados; Salvar detecta INSERT vs UPDATE
 *   - Tratamento visual de matrícula duplicada (SQLIntegrityConstraintViolationException)
 */
public class PainelFuncionario extends JPanel {

    private final FuncionarioDAO dao = new FuncionarioDAO();

    private final JTextField txtNome          = new JTextField();
    private final JTextField txtCargo         = new JTextField();
    private final JTextField txtMatriculaFunc = new JTextField(); // NOVO
    private final JTextField txtFiltro        = new JTextField();

    private final JButton btnSalvar    = new JButton("Salvar");
    private final JButton btnCancelar  = new JButton("Cancelar Edição");
    private final JButton btnExcluir   = new JButton("Excluir Selecionado");
    private final JButton btnAtualizar = new JButton("Atualizar Lista");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Nome", "Cargo", "Matrícula Func.", "Identificação"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);
    private TableRowSorter<DefaultTableModel> sorter;

    private int idEmEdicao = -1;

    public PainelFuncionario() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Nome:"));             form.add(txtNome);
        form.add(new JLabel("Cargo:"));            form.add(txtCargo);
        form.add(new JLabel("Matrícula Func.:"));  form.add(txtMatriculaFunc);

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
        txtNome.setText((String) modeloTabela.getValueAt(linhaModel, 1));
        txtCargo.setText((String) modeloTabela.getValueAt(linhaModel, 2));
        txtMatriculaFunc.setText((String) modeloTabela.getValueAt(linhaModel, 3));
        btnSalvar.setText("Atualizar");
        btnCancelar.setEnabled(true);
    }

    private void cancelarEdicao() {
        idEmEdicao = -1;
        txtNome.setText(""); txtCargo.setText(""); txtMatriculaFunc.setText("");
        btnSalvar.setText("Salvar");
        btnCancelar.setEnabled(false);
        tabela.clearSelection();
    }

    private void salvar() {
        try {
            if (txtNome.getText().isBlank() || txtCargo.getText().isBlank()
                    || txtMatriculaFunc.getText().isBlank()) {
                throw new IllegalArgumentException("Preencha Nome, Cargo e Matrícula.");
            }
            Funcionario f = new Funcionario(
                    idEmEdicao > 0 ? idEmEdicao : 0,
                    txtNome.getText().trim(),
                    txtCargo.getText().trim(),
                    txtMatriculaFunc.getText().trim()
            );

            if (idEmEdicao > 0) {
                dao.atualizar(f);
                JOptionPane.showMessageDialog(this, "Funcionário atualizado!");
            } else {
                dao.salvar(f);
                JOptionPane.showMessageDialog(this, "Funcionário salvo!");
            }
            cancelarEdicao();
            carregarTabela();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this,
                    "Matrícula já cadastrada. Utilize uma matrícula diferente.",
                    "Duplicado", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int linhaView = tabela.getSelectedRow();
        if (linhaView < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int linhaModel = tabela.convertRowIndexToModel(linhaView);
        int id   = (int) modeloTabela.getValueAt(linhaModel, 0);
        String nome = (String) modeloTabela.getValueAt(linhaModel, 1);

        if (JOptionPane.showConfirmDialog(this, "Inativar \"" + nome + "\" (ID " + id + ")?",
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
            List<Funcionario> lista = dao.listarTodos();
            for (Funcionario f : lista) {
                modeloTabela.addRow(new Object[]{
                        f.getId(), f.getNome(), f.getCargo(),
                        f.getMatriculaFunc(), f.getIdentificacao()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
