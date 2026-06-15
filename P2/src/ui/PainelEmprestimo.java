package ui;

import dao.EmprestimoDAO;
import dao.FuncionarioDAO;
import dao.LeitorDAO;
import dao.ObraDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * ALTERAÇÕES v2:
 *   - ComboBox de obras usa listarDisponiveis() → só exibe obras com estoque > 0
 *   - Campo "Prazo (dias)" para definir a data_prevista_devolucao
 *   - Tabela exibe 3 datas; empréstimos atrasados aparecem em vermelho
 *   - Tratamento visual de IllegalStateException (obra indisponível)
 *   - Filtro em tempo real na tabela de empréstimos
 *   - A referência a `Copia` foi eliminada; agora usa `Obra` diretamente
 */
public class PainelEmprestimo extends JPanel {

    private final EmprestimoDAO  emprestimoDAO  = new EmprestimoDAO();
    private final LeitorDAO      leitorDAO      = new LeitorDAO();
    private final ObraDAO        obraDAO        = new ObraDAO();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    private final JComboBox<Leitor>      cbLeitor      = new JComboBox<>();
    private final JComboBox<Obra>        cbObra        = new JComboBox<>(); // só disponíveis
    private final JComboBox<Funcionario> cbFuncionario = new JComboBox<>();
    private final JSpinner spnPrazo = new JSpinner(new SpinnerNumberModel(14, 1, 90, 1)); // dias

    private final JButton btnRegistrar = new JButton("Registrar Empréstimo");
    private final JButton btnAtualizar = new JButton("Atualizar Listas");
    private final JTextField txtFiltro = new JTextField(); // NOVO

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Obra", "Leitor", "Funcionário",
                         "Data Empr.", "Prazo Dev.", "Dev. Real", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);
    private TableRowSorter<DefaultTableModel> sorter;

    public PainelEmprestimo() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(new JLabel("Leitor:"));          form.add(cbLeitor);
        form.add(new JLabel("Obra disponível:")); form.add(cbObra);
        form.add(new JLabel("Funcionário:"));     form.add(cbFuncionario);
        form.add(new JLabel("Prazo (dias):"));    form.add(spnPrazo);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnRegistrar); botoes.add(btnAtualizar);

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

        btnRegistrar.addActionListener(e -> registrar());
        btnAtualizar.addActionListener(e -> carregarTudo());

        carregarTudo();
    }

    private void filtrar() {
        String t = txtFiltro.getText().trim();
        sorter.setRowFilter(t.isEmpty() ? null : RowFilter.regexFilter("(?i)" + t));
    }

    private void registrar() {
        try {
            Leitor leitor       = (Leitor)      cbLeitor.getSelectedItem();
            Obra obra           = (Obra)         cbObra.getSelectedItem();
            Funcionario func    = (Funcionario)  cbFuncionario.getSelectedItem();

            if (leitor == null || obra == null || func == null) {
                throw new IllegalArgumentException(
                        "Preencha todos os campos. Verifique se há leitores, obras e funcionários cadastrados.");
            }

            int prazo = (int) spnPrazo.getValue();
            LocalDate hoje   = LocalDate.now();
            LocalDate prevista = hoje.plusDays(prazo);

            Emprestimo emp = new Emprestimo(0, hoje, prevista, null, leitor, obra, func);
            emprestimoDAO.registrar(emp);
            JOptionPane.showMessageDialog(this, "Empréstimo registrado! Devolução prevista: " + prevista);
            carregarTudo();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException ex) {
            // Regra de negócio: obra sem estoque (lançada pelo EmprestimoDAO)
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Indisponível", JOptionPane.WARNING_MESSAGE);
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

            // Apenas obras com quantidade_disponivel > 0
            cbObra.removeAllItems();
            for (Obra o : obraDAO.listarDisponiveis()) cbObra.addItem(o);

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
                String devReal = e.getDataDevolucaoReal() != null
                        ? e.getDataDevolucaoReal().toString() : "—";
                String status;
                if (e.isDevolvido()) {
                    status = "Devolvido";
                } else if (e.isAtrasado()) {
                    status = "⚠ ATRASADO";
                } else {
                    status = "Em aberto";
                }
                modeloTabela.addRow(new Object[]{
                        e.getId(),
                        e.getObra().getTitulo(),
                        e.getLeitor().getNome(),
                        e.getFuncionario().getNome(),
                        e.getDataEmprestimo(),
                        e.getDataPrevistaDevolucao(),
                        devReal,
                        status
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar empréstimos: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
