package com.fatec.poo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.fatec.poo.bean.Medico;

public class MedicoDAO {

    private Connection conexao;

    public MedicoDAO() throws Exception {
        conexao = ConexaoMySQL.getConexao();
        String sql = """
                CREATE TABLE IF NOT EXISTS medico (
                    codigo        INT PRIMARY KEY AUTO_INCREMENT,
                    nome          VARCHAR(100) NOT NULL,
                    email         VARCHAR(100) NOT NULL,
                    senha         VARCHAR(100),
                    telefone      VARCHAR(20),
                    documento     VARCHAR(20),
                    crm           VARCHAR(20) NOT NULL,
                    especialidade VARCHAR(60)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""";
        conexao.createStatement().execute(sql);
    }

    // CREATE
    public void create(Medico obj) throws Exception {
        String sql = """
                INSERT INTO medico (nome, email, senha, telefone, documento, crm, especialidade)
                VALUES (?, ?, ?, ?, ?, ?, ?)""";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getEmail());
        stmt.setString(3, obj.getSenha());
        stmt.setString(4, obj.getTelefone());
        stmt.setString(5, obj.getDocumento());
        stmt.setString(6, obj.getCrm());
        stmt.setString(7, obj.getEspecialidade());
        stmt.executeUpdate();
    }

    // READ por código
    public Medico read(int codigo) throws Exception {
        String sql = "SELECT * FROM medico WHERE codigo = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, codigo);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapear(rs);
        }
        return null;
    }

    // READ ALL
    public List<Medico> readAll() throws Exception {
        List<Medico> lista = new ArrayList<>();
        ResultSet rs = conexao.createStatement().executeQuery("SELECT * FROM medico ORDER BY nome");
        while (rs.next()) {
            lista.add(mapear(rs));
        }
        return lista;
    }

    // UPDATE
    public void update(Medico obj) throws Exception {
        String sql = """
                UPDATE medico
                   SET nome = ?, email = ?, senha = ?, telefone = ?,
                       documento = ?, crm = ?, especialidade = ?
                 WHERE codigo = ?""";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getEmail());
        stmt.setString(3, obj.getSenha());
        stmt.setString(4, obj.getTelefone());
        stmt.setString(5, obj.getDocumento());
        stmt.setString(6, obj.getCrm());
        stmt.setString(7, obj.getEspecialidade());
        stmt.setInt(8, obj.getCodigo());
        stmt.executeUpdate();
    }

    // DELETE
    public void delete(int codigo) throws Exception {
        PreparedStatement stmt = conexao.prepareStatement("DELETE FROM medico WHERE codigo = ?");
        stmt.setInt(1, codigo);
        stmt.executeUpdate();
    }

    public void close() throws Exception {
        if (conexao != null && !conexao.isClosed()) conexao.close();
    }

    private Medico mapear(ResultSet rs) throws Exception {
        Medico obj = new Medico();
        obj.setCodigo(rs.getInt("codigo"));
        obj.setNome(rs.getString("nome"));
        obj.setEmail(rs.getString("email"));
        obj.setSenha(rs.getString("senha"));
        obj.setTelefone(rs.getString("telefone"));
        obj.setDocumento(rs.getString("documento"));
        obj.setCrm(rs.getString("crm"));
        obj.setEspecialidade(rs.getString("especialidade"));
        return obj;
    }
}
