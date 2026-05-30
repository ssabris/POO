package com.fatec.poo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.fatec.poo.bean.Consulta;

public class ConsultaDAO {

    private Connection conexao;

    public ConsultaDAO() throws Exception {
        conexao = ConexaoMySQL.getConexao();
        String sql = """
                CREATE TABLE IF NOT EXISTS consulta (
                    codigo          INT PRIMARY KEY AUTO_INCREMENT,
                    codigo_medico   INT NOT NULL,
                    codigo_paciente INT NOT NULL,
                    data            VARCHAR(10) NOT NULL,
                    hora            VARCHAR(5)  NOT NULL,
                    status          VARCHAR(20) DEFAULT 'AGENDADA',
                    observacao      TEXT,
                    CONSTRAINT fk_consulta_medico   FOREIGN KEY (codigo_medico)   REFERENCES medico(codigo),
                    CONSTRAINT fk_consulta_paciente FOREIGN KEY (codigo_paciente) REFERENCES paciente(codigo)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""";
        conexao.createStatement().execute(sql);
    }

    // CREATE
    public void create(Consulta obj) throws Exception {
        String sql = """
                INSERT INTO consulta (codigo_medico, codigo_paciente, data, hora, status, observacao)
                VALUES (?, ?, ?, ?, ?, ?)""";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, obj.getCodigoMedico());
        stmt.setInt(2, obj.getCodigoPaciente());
        stmt.setString(3, obj.getData());
        stmt.setString(4, obj.getHora());
        stmt.setString(5, obj.getStatus());
        stmt.setString(6, obj.getObservacao());
        stmt.executeUpdate();
    }

    // READ por código (com JOIN para pegar nomes)
    public Consulta read(int codigo) throws Exception {
        String sql = """
                SELECT c.*, m.nome AS nome_medico, p.nome AS nome_paciente
                  FROM consulta c
                  JOIN medico   m ON m.codigo = c.codigo_medico
                  JOIN paciente p ON p.codigo = c.codigo_paciente
                 WHERE c.codigo = ?""";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, codigo);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return mapear(rs);
        return null;
    }

    // READ ALL com JOIN
    public List<Consulta> readAll() throws Exception {
        List<Consulta> lista = new ArrayList<>();
        String sql = """
                SELECT c.*, m.nome AS nome_medico, p.nome AS nome_paciente
                  FROM consulta c
                  JOIN medico   m ON m.codigo = c.codigo_medico
                  JOIN paciente p ON p.codigo = c.codigo_paciente
                 ORDER BY c.data DESC, c.hora DESC""";
        ResultSet rs = conexao.createStatement().executeQuery(sql);
        while (rs.next()) lista.add(mapear(rs));
        return lista;
    }

    // UPDATE
    public void update(Consulta obj) throws Exception {
        String sql = """
                UPDATE consulta
                   SET codigo_medico = ?, codigo_paciente = ?,
                       data = ?, hora = ?, status = ?, observacao = ?
                 WHERE codigo = ?""";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, obj.getCodigoMedico());
        stmt.setInt(2, obj.getCodigoPaciente());
        stmt.setString(3, obj.getData());
        stmt.setString(4, obj.getHora());
        stmt.setString(5, obj.getStatus());
        stmt.setString(6, obj.getObservacao());
        stmt.setInt(7, obj.getCodigo());
        stmt.executeUpdate();
    }

    // DELETE
    public void delete(int codigo) throws Exception {
        PreparedStatement stmt = conexao.prepareStatement("DELETE FROM consulta WHERE codigo = ?");
        stmt.setInt(1, codigo);
        stmt.executeUpdate();
    }

    public void close() throws Exception {
        if (conexao != null && !conexao.isClosed()) conexao.close();
    }

    private Consulta mapear(ResultSet rs) throws Exception {
        Consulta obj = new Consulta();
        obj.setCodigo(rs.getInt("codigo"));
        obj.setCodigoMedico(rs.getInt("codigo_medico"));
        obj.setCodigoPaciente(rs.getInt("codigo_paciente"));
        obj.setData(rs.getString("data"));
        obj.setHora(rs.getString("hora"));
        obj.setStatus(rs.getString("status"));
        obj.setObservacao(rs.getString("observacao"));
        obj.setNomeMedico(rs.getString("nome_medico"));
        obj.setNomePaciente(rs.getString("nome_paciente"));
        return obj;
    }
}
