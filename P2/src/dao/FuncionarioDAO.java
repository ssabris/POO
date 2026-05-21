package dao;

import model.Funcionario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public void salvar(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionario (nome, cargo) VALUES (?, ?)";
        try (Connection conn = ConexaoDAO.obterConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.executeUpdate();
        }
    }

    public List<Funcionario> listarTodos() throws SQLException {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT id, nome, cargo FROM funcionario ORDER BY nome";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cargo")
                ));
            }
        }
        return lista;
    }

    public Funcionario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, cargo FROM funcionario WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Funcionario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cargo")
                    );
                }
            }
        }
        return null;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
