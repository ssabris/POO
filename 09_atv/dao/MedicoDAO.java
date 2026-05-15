package com.fatec.poo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fatec.poo.bean.Medico;

public class MedicoDAO {
    private String url = "jdbc:sqlite:database.db";
    private Connection conexao;

    // -------------------------------------------------------------------------
    // Construtor: abre conexão e garante que a tabela existe
    // -------------------------------------------------------------------------
    public MedicoDAO() throws Exception {
        conexao = DriverManager.getConnection(url);

        String sql = """
                CREATE TABLE IF NOT EXISTS medico (
                    codigo       INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome         TEXT NOT NULL,
                    email        TEXT NOT NULL,
                    senha        TEXT,
                    telefone     TEXT,
                    documento    TEXT,
                    crm          TEXT NOT NULL,
                    especialidade TEXT
                )""";

        Statement stmt = conexao.createStatement();
        stmt.execute(sql);
    }

    // -------------------------------------------------------------------------
    // CREATE – insere um novo médico
    // -------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------
    // READ – busca um médico pelo código
    // -------------------------------------------------------------------------
    public Medico read(int codigo) throws Exception {
        String sql = "SELECT * FROM medico WHERE codigo = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, codigo);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
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
        return null; // não encontrado
    }

    // -------------------------------------------------------------------------
    // READ ALL – retorna todos os médicos
    // -------------------------------------------------------------------------
    public List<Medico> readAll() throws Exception {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM medico";
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Medico obj = new Medico();
            obj.setCodigo(rs.getInt("codigo"));
            obj.setNome(rs.getString("nome"));
            obj.setEmail(rs.getString("email"));
            obj.setSenha(rs.getString("senha"));
            obj.setTelefone(rs.getString("telefone"));
            obj.setDocumento(rs.getString("documento"));
            obj.setCrm(rs.getString("crm"));
            obj.setEspecialidade(rs.getString("especialidade"));
            lista.add(obj);
        }
        return lista;
    }

    // -------------------------------------------------------------------------
    // UPDATE – atualiza os dados de um médico existente
    // -------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------
    // DELETE – remove um médico pelo código
    // -------------------------------------------------------------------------
    public void delete(int codigo) throws Exception {
        String sql = "DELETE FROM medico WHERE codigo = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, codigo);
        stmt.executeUpdate();
    }
}
