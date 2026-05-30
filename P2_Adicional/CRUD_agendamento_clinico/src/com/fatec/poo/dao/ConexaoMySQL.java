package com.fatec.poo.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Classe utilitária para centralizar a conexão com o MySQL.
 * Altere as constantes HOST, PORT, DATABASE, USER e PASSWORD
 * conforme o seu ambiente.
 */
public class ConexaoMySQL {

    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "clinica";
    private static final String USER     = "root";
    private static final String PASSWORD = "1234";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";

    /**
     * Retorna uma nova Connection ao banco MySQL.
     * A conexão deve ser fechada pelo chamador (try-with-resources ou dao.close()).
     */
    public static Connection getConexao() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
