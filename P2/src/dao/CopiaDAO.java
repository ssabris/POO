package dao;

import model.Copia;
import model.Obra;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CopiaDAO {

    public void salvar(Copia copia) throws SQLException {
        String sql = "INSERT INTO copia (id_obra, codigo_barras) VALUES (?, ?)";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, copia.getObra().getId());
            stmt.setString(2, copia.getCodigoBarras());
            stmt.executeUpdate();
        }
    }

    public List<Copia> listarTodos() throws SQLException {
        List<Copia> lista = new ArrayList<>();
        // JOIN para trazer os dados da Obra junto — sem "objetos falsos"
        String sql = """
                SELECT c.id, c.codigo_barras,
                       o.id AS obra_id, o.titulo, o.autor
                FROM copia c
                JOIN obra o ON o.id = c.id_obra
                ORDER BY o.titulo
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Obra obra = new Obra(rs.getInt("obra_id"), rs.getString("titulo"), rs.getString("autor"));
                lista.add(new Copia(rs.getInt("id"), rs.getString("codigo_barras"), obra));
            }
        }
        return lista;
    }

    public Copia buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT c.id, c.codigo_barras,
                       o.id AS obra_id, o.titulo, o.autor
                FROM copia c
                JOIN obra o ON o.id = c.id_obra
                WHERE c.id = ?
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Obra obra = new Obra(rs.getInt("obra_id"), rs.getString("titulo"), rs.getString("autor"));
                    return new Copia(rs.getInt("id"), rs.getString("codigo_barras"), obra);
                }
            }
        }
        return null;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM copia WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
