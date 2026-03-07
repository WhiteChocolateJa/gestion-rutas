package edu.pucmm.rutas.gestionrutas;

import edu.pucmm.rutas.gestionrutas.database.ConexionDB;

import java.sql.Connection;

public class DbTest {

    public static void main(String[] args) {
        Connection conexion = null;

        try {
            conexion = ConexionDB.obtenerConexion();
            System.out.println("Conexion exitosa a PostgreSQL.");

        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();

        } finally {
            ConexionDB.cerrarConexion(conexion);
        }
    }
}