package com.fatec.poo.view;

import com.fatec.poo.bean.Medico;
import com.fatec.poo.dao.MedicoDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MedicoView extends JFrame {

    // ── Campos do formulário ──────────────────────────────────────────────────
    private JTextField txtCodigo, txtNome, txtEmail, txtSenha,
            txtDocumento, txtTelefone, txtCrm;
    private JComboBox<String> cboEspecialidade;
    private JButton btnNovo, btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private MedicoDAO dao;

    // ── Especialidades disponíveis ────────────────────────────────────────────
    private static final String[] ESPECIALIDADES = {
            "Cardiologia", "Clínica Geral", "Dermatologia",
            "Ginecologia", "Neurologia", "Oftalmologia",
            "Ortopedia", "Pediatria", "Psiquiatria", "Outro"
    };

    public MedicoView() {
        try {
            dao = new MedicoDAO();
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
        setTitle("Cadastro de Médicos — Clínica");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout(8, 8));

        // ── Painel superior: formulário ───────────────────────────────────────
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Dados do Médico"),
                new EmptyBorder(4, 8, 4, 8)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo      = criarCampo(pnlForm, g, "Código:",       0, 0, false);
        txtNome        = criarCampo(pnlForm, g, "Nome:",          0, 1, true);
        txtEmail       = criarCampo(pnlForm, g, "E-mail:",        0, 2, true);
        txtSenha       = criarCampo(pnlForm, g, "Senha:",         0, 3, true);
        txtDocumento   = criarCampo(pnlForm, g, "CPF:",           2, 0, true);
        txtTelefone    = criarCampo(pnlForm, g, "Telefone:",      2, 1, true);
        txtCrm         = criarCampo(pnlForm, g, "CRM:",           2, 2, true);

        // ComboBox de especialidade
        g.gridx = 2; g.gridy = 3; g.weightx = 0;
        pnlForm.add(new JLabel("Especialidade:"), g);
        g.gridx = 3; g.weightx = 1;
        cboEspecialidade = new JComboBox<>(ESPECIALIDADES);
        pnlForm.add(cboEspecialidade, g);

        add(pnlForm, BorderLayout.NORTH);

        // ── Painel central: tabela ────────────────────────────────────────────
        String[] colunas = {"Código", "Nome", "E-mail", "Telefone", "CRM", "Especialidade"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarFormDaTabela();
        });
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // ── Painel inferior: botões ───────────────────────────────────────────
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        btnNovo   = criarBotao("Novo",   new Color(0x2196F3), pnlBotoes);
        btnSalvar = criarBotao("Salvar", new Color(0x4CAF50), pnlBotoes);
        btnEditar = criarBotao("Editar", new Color(0xFF9800), pnlBotoes);
        btnExcluir= criarBotao("Excluir",new Color(0xF44336), pnlBotoes);
        btnLimpar = criarBotao("Limpar", new Color(0x9E9E9E), pnlBotoes);
        add(pnlBotoes, BorderLayout.SOUTH);

        // ── Ações dos botões ──────────────────────────────────────────────────
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
            Medico m = obterDoForm();
            if (txtCodigo.getText().isBlank()) {
                dao.create(m);
                JOptionPane.showMessageDialog(this, "Médico cadastrado com sucesso!");
            } else {
                m.setCodigo(Integer.parseInt(txtCodigo.getText()));
                dao.update(m);
                JOptionPane.showMessageDialog(this, "Médico atualizado com sucesso!");
            }
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoEditar() {
        if (txtCodigo.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um médico na tabela para editar.");
            return;
        }
        txtNome.requestFocus();
    }

    private void acaoExcluir() {
        if (txtCodigo.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um médico na tabela para excluir.");
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "Confirma a exclusão do médico: " + txtNome.getText() + "?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                dao.delete(Integer.parseInt(txtCodigo.getText()));
                JOptionPane.showMessageDialog(this, "Médico excluído com sucesso!");
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
            List<Medico> lista = dao.readAll();
            for (Medico m : lista) {
                modeloTabela.addRow(new Object[]{
                        m.getCodigo(), m.getNome(), m.getEmail(),
                        m.getTelefone(), m.getCrm(), m.getEspecialidade()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarFormDaTabela() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        try {
            int codigo = (int) modeloTabela.getValueAt(linha, 0);
            Medico m = dao.read(codigo);
            if (m == null) return;
            txtCodigo.setText(String.valueOf(m.getCodigo()));
            txtNome.setText(m.getNome());
            txtEmail.setText(m.getEmail());
            txtSenha.setText(m.getSenha());
            txtDocumento.setText(m.getDocumento());
            txtTelefone.setText(m.getTelefone());
            txtCrm.setText(m.getCrm());
            cboEspecialidade.setSelectedItem(m.getEspecialidade());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Medico obterDoForm() {
        Medico m = new Medico();
        m.setNome(txtNome.getText().trim());
        m.setEmail(txtEmail.getText().trim());
        m.setSenha(txtSenha.getText().trim());
        m.setDocumento(txtDocumento.getText().trim());
        m.setTelefone(txtTelefone.getText().trim());
        m.setCrm(txtCrm.getText().trim());
        m.setEspecialidade((String) cboEspecialidade.getSelectedItem());
        return m;
    }

    private boolean validar() {
        if (txtNome.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Informe o Nome."); return false; }
        if (txtEmail.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Informe o E-mail."); return false; }
        if (txtCrm.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Informe o CRM."); return false; }
        return true;
    }

    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtEmail.setText("");
        txtSenha.setText("");
        txtDocumento.setText("");
        txtTelefone.setText("");
        txtCrm.setText("");
        cboEspecialidade.setSelectedIndex(0);
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
        btn.setOpaque(true);                          // força pintura do fundo
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);                  // remove borda padrão do L&F
        btn.setPreferredSize(new Dimension(110, 32));
        painel.add(btn);
        return btn;
    }
}
