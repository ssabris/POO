package dao;

import model.Obra;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ALTERAÇÕES v2:
 *   - Métodos salvar/atualizar lidam com codigoBarras, quantidadeTotal, quantidadeDisponivel
 *   - Adicionado listarDisponiveis() para o ComboBox de empréstimos
 *   - Adicionado decrementarDisponivel() e incrementarDisponivel() (chamados pelo EmprestimoDAO)
 *   - Adicionado atualizar() para edição de registos
 */
public class ObraDAO {

    public void salvar(Obra obra) throws SQLException {
        String sql = """
                INSERT INTO obra (titulo, autor, codigo_barras, quantidade_total, quantidade_disponivel)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, obra.getTitulo());
            stmt.setString(2, obra.getAutor());
            stmt.setString(3, obra.getCodigoBarras());
            stmt.setInt(4, obra.getQuantidadeTotal());
            stmt.setInt(5, obra.getQuantidadeTotal()); // disponivel = total no cadastro
            stmt.executeUpdate();
        }
    }

    /** Atualiza os dados cadastrais de uma obra existente. */
    public void atualizar(Obra obra) throws SQLException {
        String sql = """
                UPDATE obra
                SET titulo = ?, autor = ?, codigo_barras = ?, quantidade_total = ?
                WHERE id = ?
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, obra.getTitulo());
            stmt.setString(2, obra.getAutor());
            stmt.setString(3, obra.getCodigoBarras());
            stmt.setInt(4, obra.getQuantidadeTotal());
            stmt.setInt(5, obra.getId());
            stmt.executeUpdate();
        }
    }

    public List<Obra> listarTodos() throws SQLException {
        List<Obra> lista = new ArrayList<>();
        String sql = """
                SELECT id, titulo, autor, codigo_barras, quantidade_total, quantidade_disponivel
                FROM obra ORDER BY titulo
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /**
     * Retorna apenas obras com quantidade_disponivel > 0.
     * Usado no ComboBox de empréstimos para evitar selecionar obras indisponíveis.
     */
    public List<Obra> listarDisponiveis() throws SQLException {
        List<Obra> lista = new ArrayList<>();
        String sql = """
                SELECT id, titulo, autor, codigo_barras, quantidade_total, quantidade_disponivel
                FROM obra
                WHERE quantidade_disponivel > 0
                ORDER BY titulo
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Obra buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT id, titulo, autor, codigo_barras, quantidade_total, quantidade_disponivel
                FROM obra WHERE id = ?
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    /**
     * Decrementa quantidade_disponivel em 1 (chamado ao registrar empréstimo).
     * A verificação de disponibilidade já foi feita antes no EmprestimoDAO.
     */
    public void decrementarDisponivel(int idObra, Connection conn) throws SQLException {
        String sql = "UPDATE obra SET quantidade_disponivel = quantidade_disponivel - 1 WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idObra);
            stmt.executeUpdate();
        }
    }

    /**
     * Incrementa quantidade_disponivel em 1 (chamado ao registrar devolução).
     */
    public void incrementarDisponivel(int idObra, Connection conn) throws SQLException {
        String sql = "UPDATE obra SET quantidade_disponivel = quantidade_disponivel + 1 WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idObra);
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM obra WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Obra mapear(ResultSet rs) throws SQLException {
        return new Obra(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("codigo_barras"),
                rs.getInt("quantidade_total"),
                rs.getInt("quantidade_disponivel")
        );
    }
}
