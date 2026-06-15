package dao;

import model.Funcionario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ALTERAÇÕES v2:
 *   - salvar() e atualizar() incluem `matricula_func`
 *   - excluir() implementa Soft Delete (campo `ativo`)
 */
public class FuncionarioDAO {

    public void salvar(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionario (nome, cargo, matricula_func) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.setString(3, funcionario.getMatriculaFunc());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE funcionario SET nome = ?, cargo = ?, matricula_func = ? WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.setString(3, funcionario.getMatriculaFunc());
            stmt.setInt(4, funcionario.getId());
            stmt.executeUpdate();
        }
    }

    public List<Funcionario> listarTodos() throws SQLException {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT id, nome, cargo, matricula_func, ativo FROM funcionario WHERE ativo = TRUE ORDER BY nome";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cargo"),
                        rs.getString("matricula_func"),
                        rs.getBoolean("ativo")
                ));
            }
        }
        return lista;
    }

    public Funcionario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, cargo, matricula_func, ativo FROM funcionario WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Funcionario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cargo"),
                            rs.getString("matricula_func"),
                            rs.getBoolean("ativo")
                    );
                }
            }
        }
        return null;
    }

    public void excluir(int id) throws SQLException {
        String sql = "UPDATE funcionario SET ativo = FALSE WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
