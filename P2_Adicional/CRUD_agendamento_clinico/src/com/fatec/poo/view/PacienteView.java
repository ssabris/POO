package com.fatec.poo.view;

import com.fatec.poo.bean.Paciente;
import com.fatec.poo.dao.PacienteDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PacienteView extends JFrame {

    private JTextField txtCodigo, txtNome, txtEmail, txtSenha,
            txtDocumento, txtTelefone, txtDataNasc, txtPlano;
    private JButton btnNovo, btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private PacienteDAO dao;

    public PacienteView() {
        try {
            dao = new PacienteDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
        initComponents();
        carregarTabela();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Cadastro de Pacientes — Clínica");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 600);
        setLayout(new BorderLayout(8, 8));

        // ── Formulário ────────────────────────────────────────────────────────
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Dados do Paciente"),
                new EmptyBorder(4, 8, 4, 8)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo   = criarCampo(pnlForm, g, "Código:",          0, 0, false);
        txtNome     = criarCampo(pnlForm, g, "Nome:",             0, 1, true);
        txtEmail    = criarCampo(pnlForm, g, "E-mail:",           0, 2, true);
        txtSenha    = criarCampo(pnlForm, g, "Senha:",            0, 3, true);
        txtDocumento= criarCampo(pnlForm, g, "CPF:",              2, 0, true);
        txtTelefone = criarCampo(pnlForm, g, "Telefone:",         2, 1, true);
        txtDataNasc = criarCampo(pnlForm, g, "Nasc. (aaaa-mm-dd):", 2, 2, true);
        txtPlano    = criarCampo(pnlForm, g, "Plano de Saúde:",   2, 3, true);

        add(pnlForm, BorderLayout.NORTH);

        // ── Tabela ────────────────────────────────────────────────────────────
        String[] colunas = {"Código", "Nome", "E-mail", "CPF", "Telefone", "Nascimento", "Plano"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarFormDaTabela();
        });
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // ── Botões ────────────────────────────────────────────────────────────
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        btnNovo   = criarBotao("Novo",   new Color(0x2196F3), pnlBotoes);
        btnSalvar = criarBotao("Salvar", new Color(0x4CAF50), pnlBotoes);
        btnEditar = criarBotao("Editar", new Color(0xFF9800), pnlBotoes);
        btnExcluir= criarBotao("Excluir",new Color(0xF44336), pnlBotoes);
        btnLimpar = criarBotao("Limpar", new Color(0x9E9E9E), pnlBotoes);
        add(pnlBotoes, BorderLayout.SOUTH);

        btnNovo.addActionListener(e -> acaoNovo());
        btnSalvar.addActionListener(e -> acaoSalvar());
        btnEditar.addActionListener(e -> acaoEditar());
        btnExcluir.addActionListener(e -> acaoExcluir());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    // ── Ações ─────────────────────────────────────────────────────────────────

    private void acaoNovo() {
        limparCampos();
        txtNome.requestFocus();
    }

    private void acaoSalvar() {
        if (!validar()) return;
        try {
            Paciente p = obterDoForm();
            if (txtCodigo.getText().isBlank()) {
                dao.create(p);
                JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
            } else {
                p.setCodigo(Integer.parseInt(txtCodigo.getText()));
                dao.update(p);
                JOptionPane.showMessageDialog(this, "Paciente atualizado com sucesso!");
            }
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoEditar() {
        if (txtCodigo.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente na tabela para editar.");
            return;
        }
        txtNome.requestFocus();
    }

    private void acaoExcluir() {
        if (txtCodigo.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente na tabela para excluir.");
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "Confirma a exclusão do paciente: " + txtNome.getText() + "?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                dao.delete(Integer.parseInt(txtCodigo.getText()));
                JOptionPane.showMessageDialog(this, "Paciente excluído com sucesso!");
                carregarTabela();
                limparCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Paciente> lista = dao.readAll();
            for (Paciente p : lista) {
                modeloTabela.addRow(new Object[]{
                        p.getCodigo(), p.getNome(), p.getEmail(),
                        p.getDocumento(), p.getTelefone(),
                        p.getDataNascimento(), p.getPlanoSaude()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarFormDaTabela() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        try {
            int codigo = (int) modeloTabela.getValueAt(linha, 0);
            Paciente p = dao.read(codigo);
            if (p == null) return;
            txtCodigo.setText(String.valueOf(p.getCodigo()));
            txtNome.setText(p.getNome());
            txtEmail.setText(p.getEmail());
            txtSenha.setText(p.getSenha());
            txtDocumento.setText(p.getDocumento());
            txtTelefone.setText(p.getTelefone());
            txtDataNasc.setText(p.getDataNascimento());
            txtPlano.setText(p.getPlanoSaude());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Paciente obterDoForm() {
        Paciente p = new Paciente();
        p.setNome(txtNome.getText().trim());
        p.setEmail(txtEmail.getText().trim());
        p.setSenha(txtSenha.getText().trim());
        p.setDocumento(txtDocumento.getText().trim());
        p.setTelefone(txtTelefone.getText().trim());
        p.setDataNascimento(txtDataNasc.getText().trim());
        p.setPlanoSaude(txtPlano.getText().trim());
        return p;
    }

    private boolean validar() {
        if (txtNome.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Informe o Nome."); return false; }
        if (txtEmail.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Informe o E-mail."); return false; }
        return true;
    }

    private void limparCampos() {
        txtCodigo.setText(""); txtNome.setText(""); txtEmail.setText("");
        txtSenha.setText(""); txtDocumento.setText(""); txtTelefone.setText("");
        txtDataNasc.setText(""); txtPlano.setText("");
        tabela.clearSelection();
    }

    private JTextField criarCampo(JPanel painel, GridBagConstraints g,
                                   String label, int col, int linha, boolean editavel) {
        g.gridx = col; g.gridy = linha; g.weightx = 0;
        painel.add(new JLabel(label), g);
        g.gridx = col + 1; g.weightx = 1;
        JTextField campo = new JTextField(15);
        campo.setEditable(editavel);
        painel.add(campo, g);
        return campo;
    }

    private JButton criarBotao(String texto, Color cor, JPanel painel) {
        JButton btn = new JButton(texto);
        btn.setOpaque(true);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 32));
        painel.add(btn);
        return btn;
    }
}
