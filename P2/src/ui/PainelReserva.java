package ui;

import dao.LeitorDAO;
import dao.ObraDAO;
import dao.ReservaDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PainelReserva extends JPanel {

    private final ReservaDAO reservaDAO = new ReservaDAO();
    private final LeitorDAO  leitorDAO  = new LeitorDAO();
    private final ObraDAO    obraDAO    = new ObraDAO();

    private final JComboBox<Leitor> cbLeitor = new JComboBox<>();
    private final JComboBox<Obra>   cbObra   = new JComboBox<>();
    private final JButton btnRegistrar = new JButton("Registrar Reserva");
    private final JButton btnCancelar  = new JButton("Cancelar Selecionada");
    private final JButton btnAtualizar = new JButton("Atualizar Lista");

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Obra", "Leitor", "Data Reserva"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public PainelReserva() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Leitor:")); form.add(cbLeitor);
        form.add(new JLabel("Obra:"));  form.add(cbObra);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnRegistrar); botoes.add(btnCancelar); botoes.add(btnAtualizar);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> cancelar());
        btnAtualizar.addActionListener(e -> carregarTudo());

        carregarTudo();
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
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        if (JOptionPane.showConfirmDialog(this, "Cancelar reserva ID " + id + "?",
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
