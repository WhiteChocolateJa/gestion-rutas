package edu.pucmm.rutas.gestionrutas.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


/*
   Clase: ConexionDB
   Esta clase se encarga de manejar la conexión con la base de datos.
   Lee la configuración desde un archivo (db.properties) y permite abrir y cerrar conexiones de forma centralizada.
   Se utiliza en los DAO para interactuar con la base de datos.
*/

public class ConexionDB {

    private static String url;
    private static String user;
    private static String password;


    /*
   Bloque estático
   Este bloque se ejecuta automáticamente una sola vez cuando la clase se carga.
   Su objetivo es leer el archivo de configuración (db.properties) y cargar los datos de conexión (url, usuario y contraseña).
   Si ocurre algún error o no se encuentra el archivo, lanza una excepción.
   No devuelve ningún valor.
*/

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

/*
   Método: obtenerConexion
   Este método se encarga de crear una nueva conexión con la base de datos utilizando los datos previamente cargados.
   Utiliza DriverManager para establecer la conexión.
   Retorno:
      - Devuelve un objeto Connection que representa la conexión activa a la base de datos.
*/

    public static Connection obtenerConexion() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }

/*
   Método: cerrarConexion
   Este método se encarga de cerrar una conexión si existe.
   Se utiliza para liberar recursos y evitar problemas de memoria o conexiones abiertas innecesarias.
   Retorno:
      - No devuelve ningún valor.
*/

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