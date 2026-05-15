package com.fatec.poo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.fatec.poo.bean.Cliente;


public class ClienteDAO {
    private String url = "jdbc:sqlite:database.db";
    private Connection conexao;
    
    public ClienteDAO() throws Exception{
       conexao = DriverManager.getConnection(url);

       String sql = """
            CREATE TABLE IF NOT EXISTS cliente (
                codigo INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                email TEXT NOT NULL,
                senha TEXT ,
                telefone TEXT,
                documento TEXT
            )""";

        Statement stmt = conexao.createStatement();
        stmt.execute(sql);
    }

    public void create(Cliente obj) throws Exception{
        String sql = "insert into cliente(nome, email,documento, senha) values(?,?,?,?)";
        PreparedStatement comandoSql = conexao.prepareStatement(sql);
        comandoSql.setString(1, obj.getNome());
        comandoSql.setString(2, obj.getEmail());
        comandoSql.setString(3, obj.getDocumento());
        comandoSql.setString(4, obj.getSenha());
        comandoSql.executeUpdate();
    }

    
    public Cliente read(int codigo) throws  Exception{
        Cliente obj = new Cliente();
        String sql = "select * from cliente where codigo=?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, codigo);

        ResultSet resultado = stmt.executeQuery();
        if(!resultado.isClosed()){
          obj.setCodigo(codigo);
          obj.setDocumento(resultado.getString("documento"));
          obj.setEmail(resultado.getString("email"));
          obj.setNome(resultado.getString("nome"));
        }
        return new Cliente();
    }
    
}
