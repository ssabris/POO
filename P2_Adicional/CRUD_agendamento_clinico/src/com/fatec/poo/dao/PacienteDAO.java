package com.fatec.poo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.fatec.poo.bean.Paciente;

public class PacienteDAO {

    private Connection conexao;

    public PacienteDAO() throws Exception {
        conexao = ConexaoMySQL.getConexao();
        String sql = """
                CREATE TABLE IF NOT EXISTS paciente (
                    codigo          INT PRIMARY KEY AUTO_INCREMENT,
                    nome            VARCHAR(100) NOT NULL,
                    email           VARCHAR(100) NOT NULL,
                    senha           VARCHAR(100),
                    telefone        VARCHAR(20),
                    documento       VARCHAR(20),
                    data_nascimento VARCHAR(10),
                    plano_saude     VARCHAR(50)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""";
        conexao.createStatement().execute(sql);
    }

    // CREATE
    public void create(Paciente obj) throws Exception {
        String sql = """
                INSERT INTO paciente (nome, email, senha, telefone, documento, data_nascimento, plano_saude)
                VALUES (?, ?, ?, ?, ?, ?, ?)""";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getEmail());
        stmt.setString(3, obj.getSenha());
        stmt.setString(4, obj.getTelefone());
        stmt.setString(5, obj.getDocumento());
        stmt.setString(6, obj.getDataNascimento());
        stmt.setString(7, obj.getPlanoSaude());
        stmt.executeUpdate();
    }

    // READ por código
    public Paciente read(int codigo) throws Exception {
        String sql = "SELECT * FROM paciente WHERE codigo = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, codigo);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapear(rs);
        }
        return null;
    }

    // READ ALL
    public List<Paciente> readAll() throws Exception {
        List<Paciente> lista = new ArrayList<>();
        ResultSet rs = conexao.createStatement().executeQuery("SELECT * FROM paciente ORDER BY nome");
        while (rs.next()) {
            lista.add(mapear(rs));
        }
        return lista;
    }

    // UPDATE
    public void update(Paciente obj) throws Exception {
        String sql = """
                UPDATE paciente
                   SET nome = ?, email = ?, senha = ?, telefone = ?,
                       documento = ?, data_nascimento = ?, plano_saude = ?
                 WHERE codigo = ?""";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getEmail());
        stmt.setString(3, obj.getSenha());
        stmt.setString(4, obj.getTelefone());
        stmt.setString(5, obj.getDocumento());
        stmt.setString(6, obj.getDataNascimento());
        stmt.setString(7, obj.getPlanoSaude());
        stmt.setInt(8, obj.getCodigo());
        stmt.executeUpdate();
    }

    // DELETE
    public void delete(int codigo) throws Exception {
        PreparedStatement stmt = conexao.prepareStatement("DELETE FROM paciente WHERE codigo = ?");
        stmt.setInt(1, codigo);
        stmt.executeUpdate();
    }

    public void close() throws Exception {
        if (conexao != null && !conexao.isClosed()) conexao.close();
    }

    private Paciente mapear(ResultSet rs) throws Exception {
        Paciente obj = new Paciente();
        obj.setCodigo(rs.getInt("codigo"));
        obj.setNome(rs.getString("nome"));
        obj.setEmail(rs.getString("email"));
        obj.setSenha(rs.getString("senha"));
        obj.setTelefone(rs.getString("telefone"));
        obj.setDocumento(rs.getString("documento"));
        obj.setDataNascimento(rs.getString("data_nascimento"));
        obj.setPlanoSaude(rs.getString("plano_saude"));
        return obj;
    }
}
