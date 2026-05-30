package com.fatec.poo;

import com.fatec.poo.view.ConsultaView;
import com.fatec.poo.view.MedicoView;
import com.fatec.poo.view.PacienteView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MenuPrincipal();
        });
    }
}

class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Sistema Clínico — Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // ── Cabeçalho ─────────────────────────────────────────────────────────
        JLabel titulo = new JLabel("🏥  Sistema Clínico", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // ── Botões de navegação ───────────────────────────────────────────────
        JPanel pnl = new JPanel(new GridLayout(3, 1, 10, 10));
        pnl.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnMedico   = botao("👨‍⚕️  Cadastro de Médicos",   new Color(0x1976D2));
        JButton btnPaciente = botao("🧑  Cadastro de Pacientes", new Color(0x388E3C));
        JButton btnConsulta = botao("📅  Agendamento de Consultas", new Color(0xF57C00));

        pnl.add(btnMedico);
        pnl.add(btnPaciente);
        pnl.add(btnConsulta);
        add(pnl, BorderLayout.CENTER);

        // ── Rodapé ────────────────────────────────────────────────────────────
        JLabel rodape = new JLabel("FATEC — POO  |  MySQL + Swing", SwingConstants.CENTER);
        rodape.setFont(new Font("SansSerif", Font.ITALIC, 11));
        rodape.setForeground(Color.GRAY);
        rodape.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(rodape, BorderLayout.SOUTH);

        btnMedico.addActionListener(e -> new MedicoView());
        btnPaciente.addActionListener(e -> new PacienteView());
        btnConsulta.addActionListener(e -> new ConsultaView());

        setVisible(true);
    }

    private JButton botao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setOpaque(true);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
