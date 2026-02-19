package edu.pucmm.rutas.gestionrutas;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DbTest {

    public static void main(String[] args) {
        try {
            Properties p = new Properties();
            try (InputStream is = DbTest.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (is == null) throw new RuntimeException("No encuentro db.properties en src/main/resources");
                p.load(is);
            }

            String url = p.getProperty("db.url");
            String user = p.getProperty("db.user");
            String pass = p.getProperty("db.password");

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, nombre FROM prueba ORDER BY id")) {

                System.out.println("Conectado a PostgreSQL");
                while (rs.next()) {
                    System.out.println("id=" + rs.getInt("id") + " | nombre=" + rs.getString("nombre"));
                }
            }

        } catch (Exception e) {
            System.out.println("Error conectando o consultando:");
            e.printStackTrace();
        }
    }
}