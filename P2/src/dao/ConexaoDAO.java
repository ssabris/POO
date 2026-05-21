package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centraliza a criação de conexão com o banco.
 * Ajuste URL, USER e PASS conforme seu ambiente local.
 */
public class ConexaoDAO {
    private static final String URL  = "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "1234";

    static {
        try {
            // Garante que o driver MySQL seja carregado
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL não encontrado. Adicione o conector ao classpath.", e);
        }
    }

    /**
     * Retorna uma nova conexão com o banco de dados.
     * O chamador é responsável por fechá-la (try-with-resources).
     */
    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
