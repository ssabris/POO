import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SorveteriaDAO {
    // AJUSTE: Adicione suas credenciais do MySQL aqui
    private final String URL      = "jdbc:mysql://localhost:3306/";
    private final String DB_NAME  = "sorveteria_db";
    private final String USER     = "root"; // Altere se necessário
    private final String PASS     = "root"; // Altere se necessário

    public SorveteriaDAO() {
        inicializarBanco();
    }

    // BUG CORRIGIDO: A URL agora usa parâmetro correto para criar o banco caso não exista.
    // O parâmetro "createDatabaseIfNotExist=true" é exclusivo do conector MySQL/J.
    // Adicionamos também "serverTimezone=America/Sao_Paulo" para evitar erros de fuso horário
    // que são comuns no MySQL 8+.
    private Connection conectar() throws SQLException {
        String urlCompleta = URL + DB_NAME
                + "?createDatabaseIfNotExist=true"
                + "&serverTimezone=America/Sao_Paulo"
                + "&useSSL=false"
                + "&allowPublicKeyRetrieval=true";
        return DriverManager.getConnection(urlCompleta, USER, PASS);
    }

    private void inicializarBanco() {
        // BUG CORRIGIDO: O código original chamava conectar() que já exigia o banco existir
        // para criar as tabelas. Conectamos primeiro ao servidor sem especificar o banco,
        // criamos o banco manualmente, e depois conectamos normalmente.
        String urlServidor = URL + "?serverTimezone=America/Sao_Paulo&useSSL=false&allowPublicKeyRetrieval=true";

        try (Connection connServidor = DriverManager.getConnection(urlServidor, USER, PASS);
             Statement stmtServidor = connServidor.createStatement()) {

            stmtServidor.execute("CREATE DATABASE IF NOT EXISTS " + DB_NAME);

        } catch (SQLException e) {
            System.err.println("ERRO ao criar banco de dados: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Agora que o banco existe, conecta nele para criar as tabelas
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS usuarios (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  nome VARCHAR(100) NOT NULL," +
                "  gosto_favorito VARCHAR(50) NOT NULL" +
                ")"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS produtos (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  nome VARCHAR(100) NOT NULL," +
                "  categoria VARCHAR(50) NOT NULL," +
                "  preco DECIMAL(5,2) NOT NULL" +
                ")"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS vendas (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  usuario_id INT NOT NULL," +
                "  produto_id INT NOT NULL," +
                "  data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +  // MELHORIA: guarda a hora da venda
                "  FOREIGN KEY (usuario_id) REFERENCES usuarios(id)," +
                "  FOREIGN KEY (produto_id) REFERENCES produtos(id)" +
                ")"
            );

            // Só insere os produtos se a tabela estiver vazia
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM produtos");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.execute(
                    "INSERT INTO produtos (nome, categoria, preco) VALUES " +
                    "('Casquinha de Baunilha', 'Tradicional', 5.00)," +
                    "('Sundae de Morango', 'Frutas', 10.00)," +
                    "('Milkshake de Chocolate', 'Chocolate', 15.00)," +
                    "('Picolé de Limão', 'Frutas', 4.00)," +
                    "('Taça Trufada', 'Chocolate', 20.00)," +
                    "('Casquinha de Morango', 'Frutas', 6.00)," +    // MELHORIA: mais produtos
                    "('Sorvete de Creme', 'Tradicional', 7.00)," +
                    "('Brownie com Sorvete', 'Chocolate', 18.00)"
                );
                System.out.println("Produtos iniciais inseridos com sucesso.");
            }

        } catch (SQLException e) {
            System.err.println("ERRO ao inicializar tabelas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Busca o usuário pelo nome no banco. Se não existir, cadastra com o gosto informado.
     * BUG CORRIGIDO: O código original passava null como gostoSeNovo e depois tentava
     * fazer INSERT com null, o que causaria erro de constraint NOT NULL.
     * Agora o método retorna null apenas se o usuário foi encontrado mas sem gosto (impossível
     * com a nova tabela), ou se houve erro de SQL.
     */
    public Usuario identificarUsuario(String nome, String gostoSeNovo) {
        try (Connection conn = conectar()) {

            // Tenta achar o usuário pelo nome
            PreparedStatement psBusca = conn.prepareStatement(
                "SELECT * FROM usuarios WHERE nome = ?"
            );
            psBusca.setString(1, nome);
            ResultSet rs = psBusca.executeQuery();

            if (rs.next()) {
                // Usuário já existe — retorna com os dados do banco
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("gosto_favorito")
                );
            } else {
                // Usuário novo — precisa do gosto para cadastrar
                if (gostoSeNovo == null || gostoSeNovo.isEmpty()) {
                    return null; // Sinaliza que o usuário é novo e ainda não tem gosto
                }

                PreparedStatement psInsere = conn.prepareStatement(
                    "INSERT INTO usuarios (nome, gosto_favorito) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                psInsere.setString(1, nome);
                psInsere.setString(2, gostoSeNovo);
                psInsere.executeUpdate();

                ResultSet rsId = psInsere.getGeneratedKeys();
                rsId.next();
                return new Usuario(rsId.getInt(1), nome, gostoSeNovo);
            }

        } catch (SQLException e) {
            System.err.println("ERRO ao identificar usuário: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retorna os produtos ordenados: primeiro os da categoria favorita do usuário,
     * depois os demais em ordem alfabética.
     * MELHORIA: Consulta mais clara e robusta.
     */
    public List<Produto> buscarMenuPersonalizado(String gostoFavorito) {
        List<Produto> produtos = new ArrayList<>();

        // Ordena: categoria favorita vem primeiro (DESC = 1 antes de 0), resto é alfabético
        String sql = "SELECT * FROM produtos ORDER BY (categoria = ?) DESC, nome ASC";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, gostoFavorito);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                produtos.add(new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("categoria"),
                    rs.getDouble("preco")
                ));
            }

        } catch (SQLException e) {
            System.err.println("ERRO ao buscar menu: " + e.getMessage());
            e.printStackTrace();
        }

        return produtos;
    }

    /**
     * Registra uma venda no banco.
     */
    public void registrarVenda(int usuarioId, int produtoId) {
        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO vendas (usuario_id, produto_id) VALUES (?, ?)"
             )) {

            ps.setInt(1, usuarioId);
            ps.setInt(2, produtoId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("ERRO ao registrar venda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // MELHORIA: Método novo que busca o histórico de compras do usuário
    public List<String> buscarHistoricoUsuario(int usuarioId) {
        List<String> historico = new ArrayList<>();
        String sql =
            "SELECT p.nome, p.preco, v.data_hora " +
            "FROM vendas v " +
            "JOIN produtos p ON v.produto_id = p.id " +
            "WHERE v.usuario_id = ? " +
            "ORDER BY v.data_hora DESC";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String linha = String.format("%s - R$ %.2f  (%s)",
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getString("data_hora")
                ).replace(".", ",");
                historico.add(linha);
            }

        } catch (SQLException e) {
            System.err.println("ERRO ao buscar histórico: " + e.getMessage());
            e.printStackTrace();
        }

        return historico;
    }
}
