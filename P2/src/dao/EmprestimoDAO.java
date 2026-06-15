package dao;

import model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ALTERAÇÕES v2:
 *   - registrar() usa transação para garantir atomicidade:
 *       1. valida disponibilidade da obra
 *       2. insere o empréstimo (3 datas, FK para obra em vez de copia)
 *       3. decrementa quantidade_disponivel
 *   - registrarDevolucao() agora preenche data_devolucao_real e incrementa estoque
 *   - listarTodos() e listarEmAberto() adaptados para 3 datas e JOIN com obra
 */
public class EmprestimoDAO {

    private final ObraDAO obraDAO = new ObraDAO();

    /**
     * Registra um empréstimo com validação e atualização de estoque em transação única.
     *
     * Regra de negócio (item 02 de "Itens a revisar"):
     *   Se quantidade_disponivel == 0, lança IllegalStateException.
     *
     * @throws IllegalStateException se a obra não tiver exemplares disponíveis
     */
    public void registrar(Emprestimo emprestimo) throws SQLException {
        // Valida disponibilidade antes de abrir a transação
        Obra obra = obraDAO.buscarPorId(emprestimo.getObra().getId());
        if (obra == null || obra.getQuantidadeDisponivel() <= 0) {
            throw new IllegalStateException(
                    "Este livro não está disponível para empréstimo. Faça uma reserva demonstrando interesse.");
        }

        String sql = """
                INSERT INTO emprestimo
                    (id_leitor, id_obra, id_funcionario,
                     data_emprestimo, data_prevista_devolucao, data_devolucao_real)
                VALUES (?, ?, ?, ?, ?, NULL)
                """;

        // Transação garante que o INSERT e o UPDATE no estoque são atômicos
        try (Connection conn = ConexaoDAO.obterConexao()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, emprestimo.getLeitor().getId());
                    stmt.setInt(2, emprestimo.getObra().getId());
                    stmt.setInt(3, emprestimo.getFuncionario().getId());
                    stmt.setDate(4, Date.valueOf(emprestimo.getDataEmprestimo()));
                    stmt.setDate(5, Date.valueOf(emprestimo.getDataPrevistaDevolucao()));
                    stmt.executeUpdate();
                }
                // Decrementa estoque da obra usando a mesma conexão (mesma transação)
                obraDAO.decrementarDisponivel(emprestimo.getObra().getId(), conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Registra a devolução: preenche data_devolucao_real e incrementa estoque.
     * Também realizado em transação.
     */
    public void registrarDevolucao(int idEmprestimo, LocalDate dataDevolucaoReal) throws SQLException {
        // Busca o id_obra do empréstimo para poder incrementar o estoque
        String sqlBuscaObra = "SELECT id_obra FROM emprestimo WHERE id = ?";
        int idObra;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sqlBuscaObra)) {
            stmt.setInt(1, idEmprestimo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) throw new SQLException("Empréstimo não encontrado: id=" + idEmprestimo);
                idObra = rs.getInt("id_obra");
            }
        }

        String sqlDevolucao = """
                UPDATE emprestimo SET data_devolucao_real = ? WHERE id = ?
                """;
        try (Connection conn = ConexaoDAO.obterConexao()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = conn.prepareStatement(sqlDevolucao)) {
                    stmt.setDate(1, Date.valueOf(dataDevolucaoReal));
                    stmt.setInt(2, idEmprestimo);
                    stmt.executeUpdate();
                }
                obraDAO.incrementarDisponivel(idObra, conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = """
                SELECT e.id, e.data_emprestimo, e.data_prevista_devolucao, e.data_devolucao_real,
                       l.id AS l_id, l.nome AS l_nome, l.matricula,
                       o.id AS o_id, o.titulo, o.autor, o.codigo_barras,
                       o.quantidade_total, o.quantidade_disponivel,
                       f.id AS f_id, f.nome AS f_nome, f.cargo, f.matricula_func
                FROM emprestimo e
                JOIN leitor      l ON l.id = e.id_leitor
                JOIN obra        o ON o.id = e.id_obra
                JOIN funcionario f ON f.id = e.id_funcionario
                ORDER BY e.data_emprestimo DESC
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Emprestimo> listarEmAberto() throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = """
                SELECT e.id, e.data_emprestimo, e.data_prevista_devolucao, e.data_devolucao_real,
                       l.id AS l_id, l.nome AS l_nome, l.matricula,
                       o.id AS o_id, o.titulo, o.autor, o.codigo_barras,
                       o.quantidade_total, o.quantidade_disponivel,
                       f.id AS f_id, f.nome AS f_nome, f.cargo, f.matricula_func
                FROM emprestimo e
                JOIN leitor      l ON l.id = e.id_leitor
                JOIN obra        o ON o.id = e.id_obra
                JOIN funcionario f ON f.id = e.id_funcionario
                WHERE e.data_devolucao_real IS NULL
                ORDER BY e.data_emprestimo DESC
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Emprestimo mapear(ResultSet rs) throws SQLException {
        Leitor leitor = new Leitor(rs.getInt("l_id"), rs.getString("l_nome"), rs.getString("matricula"));
        Obra obra = new Obra(
                rs.getInt("o_id"), rs.getString("titulo"), rs.getString("autor"),
                rs.getString("codigo_barras"), rs.getInt("quantidade_total"), rs.getInt("quantidade_disponivel")
        );
        Funcionario func = new Funcionario(
                rs.getInt("f_id"), rs.getString("f_nome"), rs.getString("cargo"), rs.getString("matricula_func")
        );

        Date dataDev = rs.getDate("data_devolucao_real");
        LocalDate dataDevolucaoReal = (dataDev != null) ? dataDev.toLocalDate() : null;

        return new Emprestimo(
                rs.getInt("id"),
                rs.getDate("data_emprestimo").toLocalDate(),
                rs.getDate("data_prevista_devolucao").toLocalDate(),
                dataDevolucaoReal,
                leitor, obra, func
        );
    }
}
