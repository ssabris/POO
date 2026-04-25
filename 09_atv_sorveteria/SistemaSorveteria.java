import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SistemaSorveteria extends JFrame {

    private SorveteriaDAO dao;
    private Usuario usuarioLogado;
    private DefaultListModel<Produto> listModel;
    private JList<Produto> listaProdutos;

    public SistemaSorveteria() {
        dao = new SorveteriaDAO();
        setTitle("Sorveteria Inteligente 🍦");
        setSize(480, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        iniciarFluxoLogin();
    }

    private void iniciarFluxoLogin() {
        // Pede o nome do usuário
        String nome = JOptionPane.showInputDialog(this, "Bem-vindo à Sorveteria! 🍦\nQual o seu nome?", "Login", JOptionPane.QUESTION_MESSAGE);

        // BUG CORRIGIDO: trim() antes de checar isEmpty() para evitar nomes só com espaços
        if (nome == null || nome.trim().isEmpty()) {
            System.exit(0);
        }
        nome = nome.trim();

        // Primeira tentativa: busca o usuário sem passar gosto (pode ser que já exista)
        usuarioLogado = dao.identificarUsuario(nome, null);

        // BUG CORRIGIDO: No código original, ao receber null o sistema pedia o gosto
        // mas chamava identificarUsuario(nome, null) de novo (sem gosto), causando loop
        // ou NullPointerException. Agora o fluxo está correto:
        // - null significa usuário novo → pede o gosto → cadastra
        // - objeto Usuario retornado → usuário já existe com gosto salvo
        if (usuarioLogado == null) {
            // Usuário novo: pergunta qual categoria prefere
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

            // BUG CORRIGIDO: se o usuário fechar a janela, escolha retorna -1
            if (escolha == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            }

            usuarioLogado = dao.identificarUsuario(nome, opcoes[escolha]);

            if (usuarioLogado == null) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao conectar ao banco de dados.\nVerifique as configurações em SorveteriaDAO.java",
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
        // === PAINEL SUPERIOR - Informações do Cliente ===
        JPanel painelSuperior = new JPanel(new GridLayout(2, 1, 0, 2));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        painelSuperior.setBackground(new Color(255, 220, 100)); // Amarelo sorvete

        JLabel lblCliente = new JLabel("👤 Cliente: " + usuarioLogado.getNome(), JLabel.LEFT);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel lblGosto = new JLabel("⭐ Recomendado para você: " + usuarioLogado.getGostoFavorito(), JLabel.LEFT);
        lblGosto.setFont(new Font("Arial", Font.PLAIN, 12));

        painelSuperior.add(lblCliente);
        painelSuperior.add(lblGosto);
        add(painelSuperior, BorderLayout.NORTH);

        // === PAINEL CENTRAL - Lista de Produtos ===
        JPanel painelCentro = new JPanel(new BorderLayout());

        JLabel lblTitulo = new JLabel("🍦 Cardápio personalizado para você:", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
        painelCentro.add(lblTitulo, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        List<Produto> menu = dao.buscarMenuPersonalizado(usuarioLogado.getGostoFavorito());
        for (Produto p : menu) {
            listModel.addElement(p);
        }

        listaProdutos = new JList<>(listModel);
        listaProdutos.setFont(new Font("Monospaced", Font.PLAIN, 13));
        listaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaProdutos.setFixedCellHeight(28);

        // MELHORIA: Destaca visualmente os produtos da categoria favorita do usuário
        listaProdutos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Produto) {
                    Produto p = (Produto) value;
                    if (p.getCategoria().equals(usuarioLogado.getGostoFavorito())) {
                        // Cor de fundo para a categoria favorita
                        if (!isSelected) {
                            setBackground(new Color(220, 255, 220)); // Verde claro = favorito
                        }
                        setText("⭐ " + p.toString()); // Adiciona estrela nos favoritos
                    }
                }
                return this;
            }
        });

        painelCentro.add(new JScrollPane(listaProdutos), BorderLayout.CENTER);
        add(painelCentro, BorderLayout.CENTER);

        // === PAINEL INFERIOR - Botões ===
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));

        JButton btnComprar = new JButton("🛒 Realizar Pedido");
        btnComprar.setFont(new Font("Arial", Font.BOLD, 13));
        btnComprar.setBackground(new Color(100, 200, 100));
        btnComprar.addActionListener(e -> realizarPedido());

        // MELHORIA: Botão de histórico de compras
        JButton btnHistorico = new JButton("📋 Meu Histórico");
        btnHistorico.setFont(new Font("Arial", Font.PLAIN, 13));
        btnHistorico.addActionListener(e -> verHistorico());

        painelBotoes.add(btnComprar);
        painelBotoes.add(btnHistorico);
        add(painelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void realizarPedido() {
        Produto produtoSelecionado = listaProdutos.getSelectedValue();

        // BUG CORRIGIDO: O código original não avisava o usuário se nada fosse selecionado.
        // Agora exibe uma mensagem amigável.
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
            JOptionPane.showMessageDialog(this,
                "✅ Pedido de " + produtoSelecionado.getNome() + " registrado!\nBom apetite! 😋",
                "Pedido Confirmado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // MELHORIA: Método novo para mostrar o histórico de compras
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
        // Garante que a interface roda na thread correta (boa prática Swing)
        SwingUtilities.invokeLater(() -> new SistemaSorveteria());
    }
}
