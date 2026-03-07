package edu.pucmm.rutas.gestionrutas.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConexionDB {

    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            Properties properties = new Properties();

            try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (input == null) {
                    throw new RuntimeException("No se encontro el archivo db.properties en src/main/resources");
                }
                properties.load(input);
            }

            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");

        } catch (Exception e) {
            throw new RuntimeException("Error cargando la configuracion de la base de datos", e);
        }
    }

    // OBTIENE UNA NUEVA CONEXION A POSTGRESQL
    public static Connection obtenerConexion() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }

    // CIERRA UNA CONEXION SI NO ES NULL
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexion.");
                e.printStackTrace();
            }
        }
    }
}