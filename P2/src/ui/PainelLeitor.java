package ui;

import dao.LeitorDAO;
import model.Leitor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.List;

/**
 * ALTERAÇÕES v2:
 *   - Campo de filtro com TableRowSorter (pesquisa em tempo real)
 *   - Clique na tabela carrega dados para edição; botão Salvar detecta INSERT vs UPDATE
 *   - Tratamento visual de SQLIntegrityConstraintViolationException (matrícula duplicada)
 *   - Tratamento visual de IllegalStateException (empréstimo em aberto na exclusão)
 *   - Botão "Cancelar Edição" para limpar o modo de edição
 */
public class PainelLeitor extends JPanel {

    private final LeitorDAO dao = new LeitorDAO();

    private final JTextField txtNome      = new JTextField();
    private final JTextField txtMatricula = new JTextField();
    private final JTextField txtFiltro    = new JTextField(); // NOVO: filtro em tempo real

    private final JButton btnSalvar    = new JButton("Salvar");
    private final JButton btnCancelar  = new JButton("Cancelar Edição"); // NOVO
    private final JButton btnExcluir   = new JButton("Excluir Selecionado");
    private final JButton btnAtualizar = new JButton("Atualizar Lista");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Nome", "Matrícula", "Identificação"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);
    private TableRowSorter<DefaultTableModel> sorter; // NOVO: para o filtro

    private int idEmEdicao = -1; // -1 = modo INSERT; > 0 = modo UPDATE

    public PainelLeitor() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Formulário ──────────────────────────────────────────────────
        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Nome:"));      form.add(txtNome);
        form.add(new JLabel("Matrícula:")); form.add(txtMatricula);

        // ── Botões ──────────────────────────────────────────────────────
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnSalvar);
        botoes.add(btnCancelar);
        botoes.add(btnExcluir);
        botoes.add(btnAtualizar);

        // ── Filtro ──────────────────────────────────────────────────────
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

        // ── TableRowSorter (filtro em tempo real) ────────────────────────
        sorter = new TableRowSorter<>(modeloTabela);
        tabela.setRowSorter(sorter);
        txtFiltro.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });

        // ── Clique na tabela carrega dados para edição ───────────────────
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarParaEdicao();
        });

        // ── Ações dos botões ─────────────────────────────────────────────
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> cancelarEdicao());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarTabela());

        cancelarEdicao(); // define estado inicial dos botões
        carregarTabela();
    }

    /** Aplica o texto do filtro às colunas Nome e Matrícula. */
    private void filtrar() {
        String texto = txtFiltro.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            // RowFilter.regexFilter pesquisa em todas as colunas por padrão
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    /** Carrega a linha selecionada nos campos do formulário (modo edição). */
    private void carregarParaEdicao() {
        int linhaView = tabela.getSelectedRow();
        if (linhaView < 0) return;
        // Converte índice visual → índice do model (necessário com sorter ativo)
        int linhaModel = tabela.convertRowIndexToModel(linhaView);

        idEmEdicao = (int) modeloTabela.getValueAt(linhaModel, 0);
        txtNome.setText((String) modeloTabela.getValueAt(linhaModel, 1));
        txtMatricula.setText((String) modeloTabela.getValueAt(linhaModel, 2));
        btnSalvar.setText("Atualizar");
        btnCancelar.setEnabled(true);
    }

    /** Reseta o formulário para modo INSERT. */
    private void cancelarEdicao() {
        idEmEdicao = -1;
        txtNome.setText("");
        txtMatricula.setText("");
        btnSalvar.setText("Salvar");
        btnCancelar.setEnabled(false);
        tabela.clearSelection();
    }

    private void salvar() {
        try {
            if (txtNome.getText().isBlank() || txtMatricula.getText().isBlank()) {
                throw new IllegalArgumentException("Preencha Nome e Matrícula.");
            }
            Leitor leitor = new Leitor(
                    idEmEdicao > 0 ? idEmEdicao : 0,
                    txtNome.getText().trim(),
                    txtMatricula.getText().trim()
            );

            if (idEmEdicao > 0) {
                dao.atualizar(leitor);
                JOptionPane.showMessageDialog(this, "Leitor atualizado com sucesso!");
            } else {
                dao.salvar(leitor);
                JOptionPane.showMessageDialog(this, "Leitor salvo com sucesso!");
            }

            cancelarEdicao();
            carregarTabela();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLIntegrityConstraintViolationException ex) {
            // Captura específica para matrícula duplicada
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
            JOptionPane.showMessageDialog(this, "Selecione um leitor na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int linhaModel = tabela.convertRowIndexToModel(linhaView);
        int id   = (int) modeloTabela.getValueAt(linhaModel, 0);
        String nome = (String) modeloTabela.getValueAt(linhaModel, 1);

        int confirma = JOptionPane.showConfirmDialog(this,
                "Inativar leitor \"" + nome + "\" (ID " + id + ")?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirma != JOptionPane.YES_OPTION) return;

        try {
            dao.excluir(id);
            cancelarEdicao();
            carregarTabela();
        } catch (IllegalStateException ex) {
            // Bloqueio por empréstimo em aberto (regra de negócio do DAO)
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Ação Bloqueada", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        try {
            modeloTabela.setRowCount(0);
            List<Leitor> lista = dao.listarTodos();
            for (Leitor l : lista) {
                modeloTabela.addRow(new Object[]{l.getId(), l.getNome(), l.getMatricula(), l.getIdentificacao()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar leitores: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
