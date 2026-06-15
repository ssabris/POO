package ui;

import dao.LeitorDAO;
import dao.ObraDAO;
import dao.ReservaDAO;
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
 *   - Tratamento visual de IllegalStateException (limite de 3 reservas)
 *   - Filtro em tempo real adicionado
 */
public class PainelReserva extends JPanel {

    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final LeitorDAO  leitorDAO  = new LeitorDAO();
    private final ObraDAO    obraDAO    = new ObraDAO();

    private final JComboBox<Leitor> cbLeitor = new JComboBox<>();
    private final JComboBox<Obra>   cbObra   = new JComboBox<>();
    private final JTextField txtFiltro = new JTextField(); // NOVO

    private final JButton btnRegistrar = new JButton("Registrar Reserva");
    private final JButton btnCancelar  = new JButton("Cancelar Selecionada");
    private final JButton btnAtualizar = new JButton("Atualizar Lista");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Obra", "Leitor", "Data Reserva"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);
    private TableRowSorter<DefaultTableModel> sorter;

    public PainelReserva() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Leitor:")); form.add(cbLeitor);
        form.add(new JLabel("Obra:"));  form.add(cbObra);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnRegistrar); botoes.add(btnCancelar); botoes.add(btnAtualizar);

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
        btnCancelar.addActionListener(e -> cancelar());
        btnAtualizar.addActionListener(e -> carregarTudo());

        carregarTudo();
    }

    private void filtrar() {
        String t = txtFiltro.getText().trim();
        sorter.setRowFilter(t.isEmpty() ? null : RowFilter.regexFilter("(?i)" + t));
    }

    private void registrar() {
        try {
            Leitor leitor = (Leitor) cbLeitor.getSelectedItem();
            Obra obra     = (Obra)   cbObra.getSelectedItem();

            if (leitor == null || obra == null) {
                throw new IllegalArgumentException("Selecione leitor e obra. Verifique se há cadastros existentes.");
            }

            Reserva reserva = new Reserva(0, LocalDate.now(), leitor, obra);
            reservaDAO.registrar(reserva);
            JOptionPane.showMessageDialog(this, "Reserva registrada com sucesso!");
            carregarTudo();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException ex) {
            // Regra de negócio: limite de 3 reservas (lançada pelo ReservaDAO)
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Limite Atingido", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        int linhaView = tabela.getSelectedRow();
        if (linhaView < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int linhaModel = tabela.convertRowIndexToModel(linhaView);
        int id = (int) modeloTabela.getValueAt(linhaModel, 0);
        String obra = (String) modeloTabela.getValueAt(linhaModel, 1);

        if (JOptionPane.showConfirmDialog(this, "Cancelar reserva de \"" + obra + "\" (ID " + id + ")?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                reservaDAO.excluir(id);
                carregarTudo();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cancelar: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarTudo() {
        try {
            cbLeitor.removeAllItems();
            for (Leitor l : leitorDAO.listarTodos()) cbLeitor.addItem(l);

            cbObra.removeAllItems();
            for (Obra o : obraDAO.listarTodos()) cbObra.addItem(o);

            modeloTabela.setRowCount(0);
            List<Reserva> lista = reservaDAO.listarTodos();
            for (Reserva r : lista) {
                modeloTabela.addRow(new Object[]{
                        r.getId(), r.getObra().getTitulo(),
                        r.getLeitor().getNome(), r.getDataReserva()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
