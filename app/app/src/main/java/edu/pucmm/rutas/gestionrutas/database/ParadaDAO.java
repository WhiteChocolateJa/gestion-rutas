package edu.pucmm.rutas.gestionrutas.database;

import edu.pucmm.rutas.gestionrutas.modelo.Parada;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/*
   Clase: ParadaDAO
   Esta clase se encarga de gestionar las operaciones de base de datos relacionadas con las paradas.
   Permite guardar, actualizar, eliminar y consultar paradas en la base de datos.
   Funciona como intermediario entre la base de datos y el modelo Parada.
*/

public class ParadaDAO {

/*
   Método: guardar
   Este método inserta una nueva parada en la base de datos utilizando una consulta SQL.
   Toma los datos del objeto Parada y los envía a la tabla correspondiente.
   Retorno:
      - No devuelve ningún valor.
*/

    public void guardar(Parada parada) {
        String sql = "INSERT INTO paradas (id, nombre, x, y, activa, descripcion, zona) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, parada.getId());
            stmt.setString(2, parada.getNombre());
            stmt.setDouble(3, parada.getX());
            stmt.setDouble(4, parada.getY());
            stmt.setBoolean(5, parada.isActiva());
            stmt.setString(6, parada.getDescripcion());
            stmt.setString(7, parada.getZona());

            stmt.executeUpdate();
            System.out.println("Parada guardada correctamente.");

        } catch (Exception e) {
            System.out.println("Error al guardar la parada.");
            e.printStackTrace();
        }
    }

/*
   Método: actualizar
   Este método actualiza los datos de una parada existente en la base de datos según su id.
   Ejecuta una consulta SQL UPDATE y verifica si la operación afectó alguna fila.
   Retorno:
      - No devuelve ningún valor.
*/

    public void actualizar(Parada parada) {
        String sql = "UPDATE paradas SET nombre = ?, x = ?, y = ?, activa = ?, descripcion = ?, zona = ? WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, parada.getNombre());
            stmt.setDouble(2, parada.getX());
            stmt.setDouble(3, parada.getY());
            stmt.setBoolean(4, parada.isActiva());
            stmt.setString(5, parada.getDescripcion());
            stmt.setString(6, parada.getZona());
            stmt.setString(7, parada.getId());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Parada actualizada correctamente.");
            } else {
                System.out.println("No se encontró una parada con ese ID para actualizar.");
            }

        } catch (Exception e) {
            System.out.println("Error al actualizar la parada.");
            e.printStackTrace();
        }
    }

/*
   Método: eliminar
   Este método elimina una parada de la base de datos utilizando su id.
   Ejecuta una consulta SQL DELETE y verifica si la eliminación fue exitosa.
   Retorno:
      - No devuelve ningún valor.
*/

    public void eliminar(String id) {
        String sql = "DELETE FROM paradas WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, id);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Parada eliminada correctamente.");
            } else {
                System.out.println("No se encontró una parada con ese ID para eliminar.");
            }

        } catch (Exception e) {
            System.out.println("Error al eliminar la parada.");
            e.printStackTrace();
        }
    }

/*
   Método: buscarPorId
   Este método busca una parada específica en la base de datos utilizando su id.
   Si la encuentra, crea un objeto Parada con los datos obtenidos y lo devuelve.
   Si no existe, retorna null.
   Retorno:
      - Devuelve un objeto Parada si se encuentra.
      - Devuelve null si no existe.
*/

    public Parada buscarPorId(String id) {
        String sql = "SELECT * FROM paradas WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Parada parada = new Parada(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            rs.getDouble("x"),
                            rs.getDouble("y")
                    );

                    parada.setActiva(rs.getBoolean("activa"));
                    parada.setDescripcion(rs.getString("descripcion"));
                    parada.setZona(rs.getString("zona"));

                    return parada;
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar la parada por ID.");
            e.printStackTrace();
        }

        return null;
    }

/*
   Método: obtenerTodas
   Este método obtiene todas las paradas almacenadas en la base de datos.
   Recorre los resultados de la consulta SQL y crea una lista de objetos Parada.
   Retorno:
      - Devuelve una lista de objetos Parada.
*/
    public List<Parada> obtenerTodas() {
        List<Parada> paradas = new ArrayList<>();
        String sql = "SELECT * FROM paradas ORDER BY id";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Parada parada = new Parada(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getDouble("x"),
                        rs.getDouble("y")
                );

                parada.setActiva(rs.getBoolean("activa"));
                parada.setDescripcion(rs.getString("descripcion"));
                parada.setZona(rs.getString("zona"));

                paradas.add(parada);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener todas las paradas.");
            e.printStackTrace();
        }

        return paradas;
    }
}