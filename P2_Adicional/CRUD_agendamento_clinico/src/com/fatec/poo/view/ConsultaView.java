package com.fatec.poo.view;

import com.fatec.poo.bean.Consulta;
import com.fatec.poo.bean.Medico;
import com.fatec.poo.bean.Paciente;
import com.fatec.poo.dao.ConsultaDAO;
import com.fatec.poo.dao.MedicoDAO;
import com.fatec.poo.dao.PacienteDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
public class ConsultaView extends JFrame {

    private JTextField txtCodigo, txtData, txtHora, txtObservacao;
    private JComboBox<String> cboStatus;
    private JComboBox<Medico>   cboMedico;
    private JComboBox<Paciente> cboPaciente;
    private JButton btnNovo, btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private ConsultaDAO consultaDAO;
    private MedicoDAO   medicoDAO;
    private PacienteDAO pacienteDAO;

    private static final String[] STATUS = {"AGENDADA", "REALIZADA", "CANCELADA"};

    public ConsultaView() {
        try {
            consultaDAO = new ConsultaDAO();
            medicoDAO   = new MedicoDAO();
            pacienteDAO = new PacienteDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
        initComponents();
        preencherCombos();
        carregarTabela();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Agendamento de Consultas — Clínica");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 620);
        setLayout(new BorderLayout(8, 8));

        // ── Formulário ────────────────────────────────────────────────────────
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Dados da Consulta"),
                new EmptyBorder(4, 8, 4, 8)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Código, Data, Hora
        g.gridx = 0; g.gridy = 0; g.weightx = 0; pnlForm.add(new JLabel("Código:"), g);
        g.gridx = 1; g.weightx = 0.3;
        txtCodigo = new JTextField(6); txtCodigo.setEditable(false);
        pnlForm.add(txtCodigo, g);

        g.gridx = 2; g.weightx = 0; pnlForm.add(new JLabel("Data (aaaa-mm-dd):"), g);
        g.gridx = 3; g.weightx = 0.5; txtData = new JTextField(12); pnlForm.add(txtData, g);

        g.gridx = 4; g.weightx = 0; pnlForm.add(new JLabel("Hora (HH:mm):"), g);
        g.gridx = 5; g.weightx = 0.2; txtHora = new JTextField(6); pnlForm.add(txtHora, g);

        // Linha 1: Médico
        g.gridx = 0; g.gridy = 1; g.weightx = 0; pnlForm.add(new JLabel("Médico:"), g);
        g.gridx = 1; g.gridwidth = 2; g.weightx = 1;
        cboMedico = new JComboBox<>();
        pnlForm.add(cboMedico, g);
        g.gridwidth = 1;

        // Linha 1 cont: Status
        g.gridx = 3; g.weightx = 0; pnlForm.add(new JLabel("Status:"), g);
        g.gridx = 4; g.gridwidth = 2; g.weightx = 0.5;
        cboStatus = new JComboBox<>(STATUS);
        pnlForm.add(cboStatus, g);
        g.gridwidth = 1;

        // Linha 2: Paciente
        g.gridx = 0; g.gridy = 2; g.weightx = 0; pnlForm.add(new JLabel("Paciente:"), g);
        g.gridx = 1; g.gridwidth = 2; g.weightx = 1;
        cboPaciente = new JComboBox<>();
        pnlForm.add(cboPaciente, g);
        g.gridwidth = 1;

        // Linha 2 cont: Observação
        g.gridx = 3; g.weightx = 0; pnlForm.add(new JLabel("Observação:"), g);
        g.gridx = 4; g.gridwidth = 2; g.weightx = 1;
        txtObservacao = new JTextField();
        pnlForm.add(txtObservacao, g);
        g.gridwidth = 1;

        add(pnlForm, BorderLayout.NORTH);

        // ── Tabela ────────────────────────────────────────────────────────────
        String[] colunas = {"Código", "Data", "Hora", "Médico", "Paciente", "Status", "Observação"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarFormDaTabela();
        });
        // Ajustar largura de colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(100);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // ── Botões ────────────────────────────────────────────────────────────
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        btnNovo   = criarBotao("Nova Consulta",  new Color(0x2196F3), pnlBotoes);
        btnSalvar = criarBotao("Salvar",         new Color(0x4CAF50), pnlBotoes);
        btnEditar = criarBotao("Editar",         new Color(0xFF9800), pnlBotoes);
        btnExcluir= criarBotao("Excluir",        new Color(0xF44336), pnlBotoes);
        btnLimpar = criarBotao("Limpar",         new Color(0x9E9E9E), pnlBotoes);
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
        txtData.requestFocus();
    }

    private void acaoSalvar() {
        if (!validar()) return;
        try {
            Consulta c = obterDoForm();
            if (txtCodigo.getText().isBlank()) {
                consultaDAO.create(c);
                JOptionPane.showMessageDialog(this, "Consulta agendada com sucesso!");
            } else {
                c.setCodigo(Integer.parseInt(txtCodigo.getText()));
                consultaDAO.update(c);
                JOptionPane.showMessageDialog(this, "Consulta atualizada com sucesso!");
            }
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoEditar() {
        if (txtCodigo.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione uma consulta na tabela para editar.");
            return;
        }
        txtData.requestFocus();
    }

    private void acaoExcluir() {
        if (txtCodigo.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione uma consulta na tabela para excluir.");
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "Confirma a exclusão da consulta #" + txtCodigo.getText() + "?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                consultaDAO.delete(Integer.parseInt(txtCodigo.getText()));
                JOptionPane.showMessageDialog(this, "Consulta excluída com sucesso!");
                carregarTabela();
                limparCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void preencherCombos() {
        try {
            cboMedico.removeAllItems();
            for (Medico m : medicoDAO.readAll()) cboMedico.addItem(m);

            cboPaciente.removeAllItems();
            for (Paciente p : pacienteDAO.readAll()) cboPaciente.addItem(p);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar combos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            for (Consulta c : consultaDAO.readAll()) {
                modeloTabela.addRow(new Object[]{
                        c.getCodigo(), c.getData(), c.getHora(),
                        c.getNomeMedico(), c.getNomePaciente(),
                        c.getStatus(), c.getObservacao()
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
            Consulta c = consultaDAO.read(codigo);
            if (c == null) return;
            txtCodigo.setText(String.valueOf(c.getCodigo()));
            txtData.setText(c.getData());
            txtHora.setText(c.getHora());
            txtObservacao.setText(c.getObservacao());
            cboStatus.setSelectedItem(c.getStatus());

            // Selecionar médico e paciente no combo
            selecionarCombo(cboMedico, c.getCodigoMedico());
            selecionarCombo(cboPaciente, c.getCodigoPaciente());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private <T> void selecionarCombo(JComboBox<T> combo, int codigoBusca) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            int cod = (item instanceof Medico)   ? ((Medico) item).getCodigo()
                    : (item instanceof Paciente) ? ((Paciente) item).getCodigo() : -1;
            if (cod == codigoBusca) { combo.setSelectedIndex(i); break; }
        }
    }

    private Consulta obterDoForm() {
        Consulta c = new Consulta();
        c.setData(txtData.getText().trim());
        c.setHora(txtHora.getText().trim());
        c.setStatus((String) cboStatus.getSelectedItem());
        c.setObservacao(txtObservacao.getText().trim());
        if (cboMedico.getSelectedItem() instanceof Medico m)
            c.setCodigoMedico(m.getCodigo());
        if (cboPaciente.getSelectedItem() instanceof Paciente p)
            c.setCodigoPaciente(p.getCodigo());
        return c;
    }

    private boolean validar() {
        if (txtData.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Informe a Data."); return false; }
        if (txtHora.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Informe a Hora."); return false; }
        if (cboMedico.getSelectedItem() == null) { JOptionPane.showMessageDialog(this, "Selecione um Médico."); return false; }
        if (cboPaciente.getSelectedItem() == null) { JOptionPane.showMessageDialog(this, "Selecione um Paciente."); return false; }
        return true;
    }

    private void limparCampos() {
        txtCodigo.setText(""); txtData.setText(""); txtHora.setText(""); txtObservacao.setText("");
        cboStatus.setSelectedIndex(0);
        if (cboMedico.getItemCount() > 0) cboMedico.setSelectedIndex(0);
        if (cboPaciente.getItemCount() > 0) cboPaciente.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private JButton criarBotao(String texto, Color cor, JPanel painel) {
        JButton btn = new JButton(texto);
        btn.setOpaque(true);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 32));
        painel.add(btn);
        return btn;
    }
}
