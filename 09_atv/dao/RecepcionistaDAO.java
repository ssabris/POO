package com.fatec.poo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fatec.poo.bean.Recepcionista;

public class RecepcionistaDAO {
    private String url = "jdbc:sqlite:database.db";
    private Connection conexao;

    // -------------------------------------------------------------------------
    // Construtor: abre conexão e garante que a tabela existe
    // -------------------------------------------------------------------------
    public RecepcionistaDAO() throws Exception {
        conexao = DriverManager.getConnection(url);

        String sql = """
                CREATE TABLE IF NOT EXISTS recepcionista (
                    codigo    INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome      TEXT NOT NULL,
                    email     TEXT NOT NULL,
                    senha     TEXT,
                    telefone  TEXT,
                    documento TEXT,
                    turno     TEXT
                )""";

        Statement stmt = conexao.createStatement();
        stmt.execute(sql);
    }

    // -------------------------------------------------------------------------
    // CREATE – insere uma nova recepcionista
    // -------------------------------------------------------------------------
    public void create(Recepcionista obj) throws Exception {
        String sql = """
                INSERT INTO recepcionista (nome, email, senha, telefone, documento, turno)
                VALUES (?, ?, ?, ?, ?, ?)""";

        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getEmail());
        stmt.setString(3, obj.getSenha());
        stmt.setString(4, obj.getTelefone());
        stmt.setString(5, obj.getDocumento());
        stmt.setString(6, obj.getTurno());
        stmt.executeUpdate();
    }

    // -------------------------------------------------------------------------
    // READ – busca uma recepcionista pelo código
    // -------------------------------------------------------------------------
    public Recepcionista read(int codigo) throws Exception {
        String sql = "SELECT * FROM recepcionista WHERE codigo = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, codigo);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Recepcionista obj = new Recepcionista();
            obj.setCodigo(rs.getInt("codigo"));
            obj.setNome(rs.getString("nome"));
            obj.setEmail(rs.getString("email"));
            obj.setSenha(rs.getString("senha"));
            obj.setTelefone(rs.getString("telefone"));
            obj.setDocumento(rs.getString("documento"));
            obj.setTurno(rs.getString("turno"));
            return obj;
        }
        return null; // não encontrado
    }

    // -------------------------------------------------------------------------
    // READ ALL – retorna todas as recepcionistas
    // -------------------------------------------------------------------------
    public List<Recepcionista> readAll() throws Exception {
        List<Recepcionista> lista = new ArrayList<>();
        String sql = "SELECT * FROM recepcionista";
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Recepcionista obj = new Recepcionista();
            obj.setCodigo(rs.getInt("codigo"));
            obj.setNome(rs.getString("nome"));
            obj.setEmail(rs.getString("email"));
            obj.setSenha(rs.getString("senha"));
            obj.setTelefone(rs.getString("telefone"));
            obj.setDocumento(rs.getString("documento"));
            obj.setTurno(rs.getString("turno"));
            lista.add(obj);
        }
        return lista;
    }

    // -------------------------------------------------------------------------
    // UPDATE – atualiza os dados de uma recepcionista existente
    // -------------------------------------------------------------------------
    public void update(Recepcionista obj) throws Exception {
        String sql = """
                UPDATE recepcionista
                   SET nome = ?, email = ?, senha = ?, telefone = ?,
                       documento = ?, turno = ?
                 WHERE codigo = ?""";

        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setString(1, obj.getNome());
        stmt.setString(2, obj.getEmail());
        stmt.setString(3, obj.getSenha());
        stmt.setString(4, obj.getTelefone());
        stmt.setString(5, obj.getDocumento());
        stmt.setString(6, obj.getTurno());
        stmt.setInt(7, obj.getCodigo());
        stmt.executeUpdate();
    }

    // -------------------------------------------------------------------------
    // DELETE – remove uma recepcionista pelo código
    // -------------------------------------------------------------------------
    public void delete(int codigo) throws Exception {
        String sql = "DELETE FROM recepcionista WHERE codigo = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, codigo);
        stmt.executeUpdate();
    }
}
