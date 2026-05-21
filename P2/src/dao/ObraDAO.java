package dao;

import model.Obra;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObraDAO {

    public void salvar(Obra obra) throws SQLException {
        String sql = "INSERT INTO obra (titulo, autor) VALUES (?, ?)";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, obra.getTitulo());
            stmt.setString(2, obra.getAutor());
            stmt.executeUpdate();
        }
    }

    public List<Obra> listarTodos() throws SQLException {
        List<Obra> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, autor FROM obra ORDER BY titulo";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Obra(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor")
                ));
            }
        }
        return lista;
    }

    public Obra buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, titulo, autor FROM obra WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Obra(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor")
                    );
                }
            }
        }
        return null;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM obra WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
