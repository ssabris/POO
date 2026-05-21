package dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public void registrar(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reserva (id_leitor, id_obra, data_reserva) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reserva.getLeitor().getId());
            stmt.setInt(2, reserva.getObra().getId());
            stmt.setDate(3, Date.valueOf(reserva.getDataReserva()));
            stmt.executeUpdate();
        }
    }

    public List<Reserva> listarTodos() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = """
                SELECT r.id, r.data_reserva,
                       l.id AS l_id, l.nome AS l_nome, l.matricula,
                       o.id AS o_id, o.titulo, o.autor
                FROM reserva r
                JOIN leitor l ON l.id = r.id_leitor
                JOIN obra   o ON o.id = r.id_obra
                ORDER BY r.data_reserva DESC
                """;
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Leitor leitor = new Leitor(rs.getInt("l_id"), rs.getString("l_nome"), rs.getString("matricula"));
                Obra obra = new Obra(rs.getInt("o_id"), rs.getString("titulo"), rs.getString("autor"));
                lista.add(new Reserva(rs.getInt("id"), rs.getDate("data_reserva").toLocalDate(), leitor, obra));
            }
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM reserva WHERE id = ?";
        try (Connection conn = ConexaoDAO.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
