package ui;

import javax.swing.*;

/**
 * Ponto de entrada da aplicação.
 * Separado de TelaPrincipal para respeitar responsabilidade única.
 */
public class Main {
    public static void main(String[] args) {
        // Usa o Look and Feel do sistema operacional para parecer mais nativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}
