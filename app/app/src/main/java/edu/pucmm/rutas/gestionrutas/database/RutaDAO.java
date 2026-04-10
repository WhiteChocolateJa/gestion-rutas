package edu.pucmm.rutas.gestionrutas.database;

import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/*
   Clase: RutaDAO
   Esta clase se encarga de gestionar las operaciones de base de datos relacionadas con las rutas.
   Permite guardar, actualizar, eliminar y consultar rutas en la base de datos.
   Funciona como intermediario entre la base de datos y el modelo Ruta.
*/


public class RutaDAO {

/*
   Método: guardar
   Este método inserta una nueva ruta en la base de datos utilizando una consulta SQL.
   Toma los datos del objeto Ruta y guarda tanto sus métricas como los ids de las paradas de origen y destino.
   Retorno:
      - No devuelve ningún valor.
*/

    public void guardar(Ruta ruta) {
        String sql = "INSERT INTO rutas (id, parada_origen_id, parada_destino_id, tiempo_viaje, distancia, costo, transbordos) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, ruta.getId());
            stmt.setString(2, ruta.getParadaOrigen().getId());
            stmt.setString(3, ruta.getParadaDestino().getId());
            stmt.setDouble(4, ruta.getTiempoViaje());
            stmt.setDouble(5, ruta.getDistancia());
            stmt.setDouble(6, ruta.getCosto());
            stmt.setInt(7, ruta.getTransbordos());

            stmt.executeUpdate();
            System.out.println("Ruta guardada correctamente.");

        } catch (Exception e) {
            System.out.println("Error al guardar la ruta.");
            e.printStackTrace();
        }
    }

/*
   Método: actualizar
   Este método actualiza los datos de una ruta existente en la base de datos según su id.
   Modifica las referencias de origen y destino, así como el tiempo, distancia, costo y transbordos.
   Retorno:
      - No devuelve ningún valor.
*/

    public void actualizar(Ruta ruta) {
        String sql = "UPDATE rutas SET parada_origen_id = ?, parada_destino_id = ?, tiempo_viaje = ?, distancia = ?, costo = ?, transbordos = ? " +
                "WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, ruta.getParadaOrigen().getId());
            stmt.setString(2, ruta.getParadaDestino().getId());
            stmt.setDouble(3, ruta.getTiempoViaje());
            stmt.setDouble(4, ruta.getDistancia());
            stmt.setDouble(5, ruta.getCosto());
            stmt.setInt(6, ruta.getTransbordos());
            stmt.setString(7, ruta.getId());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Ruta actualizada correctamente.");
            } else {
                System.out.println("No se encontró la ruta para actualizar.");
            }

        } catch (Exception e) {
            System.out.println("Error al actualizar la ruta.");
            e.printStackTrace();
        }
    }

/*
   Método: eliminar
   Este método elimina una ruta de la base de datos utilizando su id.
   Ejecuta una consulta SQL DELETE y verifica si la operación afectó alguna fila.
   Retorno:
      - No devuelve ningún valor.
*/

    public void eliminar(String idRuta) {
        String sql = "DELETE FROM rutas WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, idRuta);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Ruta eliminada correctamente.");
            } else {
                System.out.println("No se encontró la ruta para eliminar.");
            }

        } catch (Exception e) {
            System.out.println("Error al eliminar la ruta.");
            e.printStackTrace();
        }
    }

/*
   Método: buscarPorId
   Este método busca una ruta específica en la base de datos utilizando su id.
   Para reconstruir correctamente la ruta, primero busca las paradas de origen y destino con ayuda de ParadaDAO.
   Si ambas existen, crea y devuelve un objeto Ruta completo.
   Si no existe, retorna null.
   Retorno:
      - Devuelve un objeto Ruta si se encuentra.
      - Devuelve null si no existe.
*/

    public Ruta buscarPorId(String idRuta) {
        String sql = "SELECT * FROM rutas WHERE id = ?";

        ParadaDAO paradaDAO = new ParadaDAO();

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, idRuta);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Parada origen = paradaDAO.buscarPorId(rs.getString("parada_origen_id"));
                    Parada destino = paradaDAO.buscarPorId(rs.getString("parada_destino_id"));

                    if (origen != null && destino != null) {
                        return new Ruta(
                                rs.getString("id"),
                                origen,
                                destino,
                                rs.getDouble("tiempo_viaje"),
                                rs.getDouble("distancia"),
                                rs.getDouble("costo"),
                                rs.getInt("transbordos")
                        );
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar la ruta por ID.");
            e.printStackTrace();
        }

        return null;
    }

/*
   Método: obtenerTodas
   Este método obtiene todas las rutas almacenadas en la base de datos.
   Recorre los resultados de la consulta SQL y construye una lista de objetos Ruta, buscando también sus paradas asociadas.
   Retorno:
      - Devuelve una lista de objetos Ruta.
*/

    public List<Ruta> obtenerTodas() {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT * FROM rutas ORDER BY id";

        ParadaDAO paradaDAO = new ParadaDAO();

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Parada origen = paradaDAO.buscarPorId(rs.getString("parada_origen_id"));
                Parada destino = paradaDAO.buscarPorId(rs.getString("parada_destino_id"));

                if (origen != null && destino != null) {
                    Ruta ruta = new Ruta(
                            rs.getString("id"),
                            origen,
                            destino,
                            rs.getDouble("tiempo_viaje"),
                            rs.getDouble("distancia"),
                            rs.getDouble("costo"),
                            rs.getInt("transbordos")
                    );

                    rutas.add(ruta);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al obtener todas las rutas.");
            e.printStackTrace();
        }

        return rutas;
    }
}