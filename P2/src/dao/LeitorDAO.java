package dao;

import model.Leitor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ALTERAÇÕES v2:
 *   - salvar() e listarTodos() lidam com o campo `ativo`
 *   - atualizar() adicionado para edição de registos
 *   - excluir() verifica empréstimos em aberto antes de agir (corrige bug item 01)
 *     e implementa Soft Delete (marca ativo=0 em vez de DELETE físico)
 */
public class LeitorDAO {

    public void salvar(Leitor leitor) throws SQLException {
        String sql = "INSERT INTO leitor (nome, matricula) VALUES (?, ?)";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, leitor.getNome());
            stmt.setString(2, leitor.getMatricula());
            stmt.executeUpdate();
        }
    }

    /** Atualiza nome e matrícula de um leitor existente. */
    public void atualizar(Leitor leitor) throws SQLException {
        String sql = "UPDATE leitor SET nome = ?, matricula = ? WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, leitor.getNome());
            stmt.setString(2, leitor.getMatricula());
            stmt.setInt(3, leitor.getId());
            stmt.executeUpdate();
        }
    }

    /** Lista apenas leitores ativos. */
    public List<Leitor> listarTodos() throws SQLException {
        List<Leitor> lista = new ArrayList<>();
        String sql = "SELECT id, nome, matricula, ativo FROM leitor WHERE ativo = 1 ORDER BY nome";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Leitor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("matricula"),
                        rs.getBoolean("ativo")
                ));
            }
        }
        return lista;
    }

    public Leitor buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, matricula, ativo FROM leitor WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Leitor(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("matricula"),
                            rs.getBoolean("ativo")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Soft Delete: marca o leitor como inativo em vez de apagar fisicamente.
     * Antes de inativar, verifica se há empréstimos em aberto (data_devolucao_real IS NULL).
     *
     * CORRIGE BUG (item 01 de "Itens a revisar"):
     *   O erro anterior disparava mesmo para empréstimos já devolvidos porque
     *   o banco lançava violação de FK ao tentar DELETE físico. Agora verificamos
     *   apenas empréstimos realmente em aberto antes de inativar.
     *
     * @throws IllegalStateException se o leitor tiver empréstimos em aberto
     */
    public void excluir(int id) throws SQLException {
        // 1. Verifica empréstimos EM ABERTO (data_devolucao_real IS NULL)
        String sqlVerifica = """
                SELECT COUNT(*) FROM emprestimo
                WHERE id_leitor = ? AND data_devolucao_real IS NULL
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sqlVerifica)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new IllegalStateException(
                            "O utilizador não pode ser excluído pois tem um empréstimo em aberto.");
                }
            }
        }

        // 2. Soft Delete: marca como inativo
        String sqlInativar = "UPDATE leitor SET ativo = 0 WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sqlInativar)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
