import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SistemaSorveteria extends JFrame {

    private SorveteriaFloquinho dao;
    private Usuario usuarioLogado;
    private JPanel painelCardapio;
    private JLabel lblRecomendacao;
    private Produto produtoSelecionado = null;

    public SistemaSorveteria() {
        dao = new SorveteriaFloquinho();
        setTitle("Sorveteria Floquinho 🍦");
        setSize(480, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        iniciarFluxoLogin();
    }

    private void iniciarFluxoLogin() {
        String nome = JOptionPane.showInputDialog(this, "Bem-vindo à Sorveteria Floquinho! 🍦\nQual o seu nome?", "Login", JOptionPane.QUESTION_MESSAGE);

        if (nome == null || nome.trim().isEmpty()) {
            System.exit(0);
        }
        nome = nome.trim();

        usuarioLogado = dao.identificarUsuario(nome, null);

        if (usuarioLogado == null) {
            String[] opcoes = {"Tradicional", "Frutas", "Chocolate"};
            int escolha = JOptionPane.showOptionDialog(
                this,
                "Olá, " + nome + "! Qual tipo de sorvete você prefere?",
                "Cadastro - Gosto Favorito",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
            );

            if (escolha == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            }

            usuarioLogado = dao.identificarUsuario(nome, opcoes[escolha]);

            if (usuarioLogado == null) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao conectar ao banco de dados.\nVerifique as configurações em SorveteriaFloquinho.java",
                    "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            JOptionPane.showMessageDialog(this,
                "Cadastro realizado! Bem-vindo, " + nome + "! 😊",
                "Novo Usuário", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Olá de novo, " + usuarioLogado.getNome() + "! 😊\nJá te conheço! Preferência: " + usuarioLogado.getGostoFavorito(),
                "Bem-vindo de volta!", JOptionPane.INFORMATION_MESSAGE);
        }

        montarInterfaceVendas();
    }

    private void montarInterfaceVendas() {
        JPanel painelSuperior = new JPanel(new GridLayout(2, 1, 0, 2));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        painelSuperior.setBackground(new Color(255, 220, 100));

        JLabel lblCliente = new JLabel("👤 Cliente: " + usuarioLogado.getNome(), JLabel.LEFT);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel lblGosto = new JLabel("⭐ Recomendado para você: " + usuarioLogado.getGostoFavorito(), JLabel.LEFT);
        lblRecomendacao = lblGosto;
        lblGosto.setFont(new Font("Arial", Font.PLAIN, 12));

        painelSuperior.add(lblCliente);
        painelSuperior.add(lblGosto);
        add(painelSuperior, BorderLayout.NORTH);

        JPanel painelCentro = new JPanel(new BorderLayout());

        JLabel lblTitulo = new JLabel("🍦 Cardápio personalizado para você:", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
        painelCentro.add(lblTitulo, BorderLayout.NORTH);

        painelCardapio = new JPanel();
        painelCardapio.setLayout(new BoxLayout(painelCardapio, BoxLayout.Y_AXIS));

        preencherCardapio(dao.buscarMenuPersonalizado(usuarioLogado.getGostoFavorito()));

        JScrollPane scroll = new JScrollPane(painelCardapio);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        painelCentro.add(scroll, BorderLayout.CENTER);
        add(painelCentro, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));

        JButton btnComprar = new JButton("🛒 Realizar Pedido");
        btnComprar.setFont(new Font("Arial", Font.BOLD, 13));
        btnComprar.setBackground(new Color(100, 200, 100));
        btnComprar.addActionListener(e -> realizarPedido());

        JButton btnHistorico = new JButton("📋 Meu Histórico");
        btnHistorico.setFont(new Font("Arial", Font.PLAIN, 13));
        btnHistorico.addActionListener(e -> verHistorico());

        painelBotoes.add(btnComprar);
        painelBotoes.add(btnHistorico);
        add(painelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void realizarPedido() {
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this,
                "Por favor, selecione um produto na lista primeiro!",
                "Nenhum produto selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Confirmar pedido:\n\n" + produtoSelecionado + "\n\nDeseja continuar?",
            "Confirmar Pedido", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            dao.registrarVenda(usuarioLogado.getId(), produtoSelecionado.getId());

            String categoriaAtualizada = dao.calcularCategoriaFavorita(usuarioLogado.getId());
            if (categoriaAtualizada == null) {
                categoriaAtualizada = usuarioLogado.getGostoFavorito();
            }

            boolean recomendacaoMudou = !categoriaAtualizada.equals(usuarioLogado.getGostoFavorito());

            usuarioLogado = new Usuario(
                usuarioLogado.getId(),
                usuarioLogado.getNome(),
                categoriaAtualizada
            );

            produtoSelecionado = null;
            atualizarCardapio();

            String mensagem = "✅ Pedido registrado!\nBom apetite! 😋";
            if (recomendacaoMudou) {
                mensagem += "\n\n🔄 Sua recomendação foi atualizada para: " + categoriaAtualizada;
            }

            JOptionPane.showMessageDialog(this, mensagem, "Pedido Confirmado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void preencherCardapio(List<Produto> produtos) {
        painelCardapio.removeAll();

        String categoriaFavorita = usuarioLogado.getGostoFavorito();

        painelCardapio.add(criarCabecalhoSecao("⭐  Recomendações  —  " + categoriaFavorita));

        for (Produto p : produtos) {
            if (p.getCategoria().equals(categoriaFavorita)) {
                painelCardapio.add(criarLinhaProduto(p));
            }
        }

        painelCardapio.add(criarCabecalhoSecao("Outros"));

        String categoriaAtual = "";
        for (Produto p : produtos) {
            if (!p.getCategoria().equals(categoriaFavorita)) {
                if (!p.getCategoria().equals(categoriaAtual)) {
                    categoriaAtual = p.getCategoria();
                    painelCardapio.add(criarSubtituloCategoria(categoriaAtual));
                }
                painelCardapio.add(criarLinhaProduto(p));
            }
        }

        painelCardapio.revalidate();
        painelCardapio.repaint();
    }

    private JLabel criarCabecalhoSecao(String texto) {
        JLabel label = new JLabel("  " + texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        label.setBackground(new Color(80, 120, 200));
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return label;
    }

    private JLabel criarSubtituloCategoria(String categoria) {
        JLabel label = new JLabel("    " + categoria);
        label.setFont(new Font("Arial", Font.ITALIC, 11));
        label.setForeground(new Color(100, 100, 100));
        label.setBackground(new Color(240, 240, 240));
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        return label;
    }

    private JPanel criarLinhaProduto(Produto p) {
        JPanel linha = new JPanel(new BorderLayout());
        linha.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        linha.setBackground(Color.WHITE);

        JLabel lblNome = new JLabel("   " + p.getNome());
        lblNome.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblPreco = new JLabel(String.format("R$ %.2f   ", p.getPreco()).replace(".", ","));
        lblPreco.setFont(new Font("Arial", Font.BOLD, 13));
        lblPreco.setForeground(new Color(40, 140, 40));

        linha.add(lblNome, BorderLayout.CENTER);
        linha.add(lblPreco, BorderLayout.EAST);

        linha.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                for (Component c : painelCardapio.getComponents()) {
                    if (c instanceof JPanel) c.setBackground(Color.WHITE);
                }
                linha.setBackground(new Color(200, 220, 255));
                produtoSelecionado = p;
            }
        });

        return linha;
    }

    private void atualizarCardapio() {
        lblRecomendacao.setText("⭐ Recomendado para você: " + usuarioLogado.getGostoFavorito());
        preencherCardapio(dao.buscarMenuPersonalizado(usuarioLogado.getGostoFavorito()));
    }

    private void verHistorico() {
        List<String> historico = dao.buscarHistoricoUsuario(usuarioLogado.getId());

        if (historico.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Você ainda não fez nenhum pedido!",
                "Histórico", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Seus últimos pedidos:\n\n");
        for (String linha : historico) {
            sb.append("• ").append(linha).append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JOptionPane.showMessageDialog(this,
            new JScrollPane(textArea),
            "📋 Histórico de " + usuarioLogado.getNome(),
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaSorveteria());
    }
}
