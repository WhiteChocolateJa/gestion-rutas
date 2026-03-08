package edu.pucmm.rutas.gestionrutas.database;

import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RutaDAO {

    // GUARDA UNA RUTA NUEVA EN LA BASE DE DATOS
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

    // ACTUALIZA UNA RUTA EXISTENTE SEGUN SU ID
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

    // ELIMINA UNA RUTA SEGUN SU ID
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

    // BUSCA UNA RUTA POR SU ID
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

    // OBTIENE TODAS LAS RUTAS DE LA BASE DE DATOS
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