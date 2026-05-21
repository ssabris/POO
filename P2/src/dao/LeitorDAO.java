package dao;

import model.Leitor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Leitor> listarTodos() throws SQLException {
        List<Leitor> lista = new ArrayList<>();
        String sql = "SELECT id, nome, matricula FROM leitor ORDER BY nome";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Leitor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("matricula")
                ));
            }
        }
        return lista;
    }

    public Leitor buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, matricula FROM leitor WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Leitor(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("matricula")
                    );
                }
            }
        }
        return null;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM leitor WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
