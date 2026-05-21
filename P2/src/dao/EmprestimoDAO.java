package dao;

import model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    public void registrar(Emprestimo emprestimo) throws SQLException {
        String sql = """
                INSERT INTO emprestimo (id_leitor, id_copia, id_funcionario, data_emprestimo, data_devolucao)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getLeitor().getId());
            stmt.setInt(2, emprestimo.getCopia().getId());
            stmt.setInt(3, emprestimo.getFuncionario().getId());
            stmt.setDate(4, Date.valueOf(emprestimo.getDataEmprestimo()));
            // data_devolucao pode ser nula na criação
            if (emprestimo.getDataDevolucao() != null) {
                stmt.setDate(5, Date.valueOf(emprestimo.getDataDevolucao()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.executeUpdate();
        }
    }

    /**
     * Registra a devolução de um empréstimo pelo ID.
     */
    public void registrarDevolucao(int idEmprestimo, LocalDate dataDevolucao) throws SQLException {
        String sql = "UPDATE emprestimo SET data_devolucao = ? WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(dataDevolucao));
            stmt.setInt(2, idEmprestimo);
            stmt.executeUpdate();
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();
        // JOIN completo: traz todos os dados reais sem objetos falsos
        String sql = """
                SELECT e.id, e.data_emprestimo, e.data_devolucao,
                       l.id AS l_id, l.nome AS l_nome, l.matricula,
                       c.id AS c_id, c.codigo_barras,
                       o.id AS o_id, o.titulo, o.autor,
                       f.id AS f_id, f.nome AS f_nome, f.cargo
                FROM emprestimo e
                JOIN leitor      l ON l.id = e.id_leitor
                JOIN copia       c ON c.id = e.id_copia
                JOIN obra        o ON o.id = c.id_obra
                JOIN funcionario f ON f.id = e.id_funcionario
                ORDER BY e.data_emprestimo DESC
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearEmprestimo(rs));
            }
        }
        return lista;
    }

    public List<Emprestimo> listarEmAberto() throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = """
                SELECT e.id, e.data_emprestimo, e.data_devolucao,
                       l.id AS l_id, l.nome AS l_nome, l.matricula,
                       c.id AS c_id, c.codigo_barras,
                       o.id AS o_id, o.titulo, o.autor,
                       f.id AS f_id, f.nome AS f_nome, f.cargo
                FROM emprestimo e
                JOIN leitor      l ON l.id = e.id_leitor
                JOIN copia       c ON c.id = e.id_copia
                JOIN obra        o ON o.id = c.id_obra
                JOIN funcionario f ON f.id = e.id_funcionario
                WHERE e.data_devolucao IS NULL
                ORDER BY e.data_emprestimo DESC
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearEmprestimo(rs));
            }
        }
        return lista;
    }

    // Método auxiliar para evitar repetição de mapeamento
    private Emprestimo mapearEmprestimo(ResultSet rs) throws SQLException {
        Leitor leitor = new Leitor(rs.getInt("l_id"), rs.getString("l_nome"), rs.getString("matricula"));
        Obra obra = new Obra(rs.getInt("o_id"), rs.getString("titulo"), rs.getString("autor"));
        Copia copia = new Copia(rs.getInt("c_id"), rs.getString("codigo_barras"), obra);
        Funcionario func = new Funcionario(rs.getInt("f_id"), rs.getString("f_nome"), rs.getString("cargo"));

        Date dataDev = rs.getDate("data_devolucao");
        LocalDate dataDevolucao = (dataDev != null) ? dataDev.toLocalDate() : null;

        return new Emprestimo(
                rs.getInt("id"),
                rs.getDate("data_emprestimo").toLocalDate(),
                dataDevolucao,
                leitor, copia, func
        );
    }
}
