package cl.duoc.dinopark.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDb {
    private static final String URL = "jdbc:mysql://localhost:3306/dinopark_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Usuario por defecto en XAMPP
    private static final String PASSWORD = ""; // Por defecto en XAMPP no tiene contraseña

    public static Connection obtenerConexion() throws SQLException {
        try {
            // Cargar explícitamente el driver (opcional en versiones modernas, pero seguro)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el Driver JDBC de MySQL: " + e.getMessage());
        }
    }
}