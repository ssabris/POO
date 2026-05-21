package ui;

import dao.CopiaDAO;
import dao.EmprestimoDAO;
import dao.FuncionarioDAO;
import dao.LeitorDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PainelEmprestimo extends JPanel {

    private final EmprestimoDAO  emprestimoDAO  = new EmprestimoDAO();
    private final LeitorDAO      leitorDAO      = new LeitorDAO();
    private final CopiaDAO       copiaDAO       = new CopiaDAO();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    // ComboBoxes carregados do banco — sem IDs digitados a mão
    private final JComboBox<Leitor>      cbLeitor     = new JComboBox<>();
    private final JComboBox<Copia>       cbCopia      = new JComboBox<>();
    private final JComboBox<Funcionario> cbFuncionario= new JComboBox<>();
    private final JButton btnRegistrar  = new JButton("Registrar Empréstimo");
    private final JButton btnAtualizar  = new JButton("Atualizar Listas");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Obra", "Cópia (CB)", "Leitor", "Funcionário", "Data", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelEmprestimo() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Leitor:"));       form.add(cbLeitor);
        form.add(new JLabel("Cópia:"));        form.add(cbCopia);
        form.add(new JLabel("Funcionário:"));  form.add(cbFuncionario);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnRegistrar); botoes.add(btnAtualizar);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrar());
        btnAtualizar.addActionListener(e -> carregarTudo());

        carregarTudo();
    }

    private void registrar() {
        try {
            Leitor leitor = (Leitor) cbLeitor.getSelectedItem();
            Copia copia   = (Copia)  cbCopia.getSelectedItem();
            Funcionario func = (Funcionario) cbFuncionario.getSelectedItem();

            if (leitor == null || copia == null || func == null) {
                throw new IllegalArgumentException("Preencha todos os campos. Verifique se há leitores, cópias e funcionários cadastrados.");
            }

            Emprestimo emp = new Emprestimo(0, LocalDate.now(), null, leitor, copia, func);
            emprestimoDAO.registrar(emp);
            JOptionPane.showMessageDialog(this, "Empréstimo registrado com sucesso!");
            carregarTudo();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTudo() {
        carregarCombos();
        carregarTabela();
    }

    private void carregarCombos() {
        try {
            cbLeitor.removeAllItems();
            for (Leitor l : leitorDAO.listarTodos()) cbLeitor.addItem(l);

            cbCopia.removeAllItems();
            for (Copia c : copiaDAO.listarTodos()) cbCopia.addItem(c);

            cbFuncionario.removeAllItems();
            for (Funcionario f : funcionarioDAO.listarTodos()) cbFuncionario.addItem(f);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar combos: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        try {
            modeloTabela.setRowCount(0);
            List<Emprestimo> lista = emprestimoDAO.listarTodos();
            for (Emprestimo e : lista) {
                String status = e.isDevolvido() ? "Devolvido em " + e.getDataDevolucao() : "Em aberto";
                modeloTabela.addRow(new Object[]{
                        e.getId(),
                        e.getCopia().getObra().getTitulo(),
                        e.getCopia().getCodigoBarras(),
                        e.getLeitor().getNome(),
                        e.getFuncionario().getNome(),
                        e.getDataEmprestimo(),
                        status
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar empréstimos: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
