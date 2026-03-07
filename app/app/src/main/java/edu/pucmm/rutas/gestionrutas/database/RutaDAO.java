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
        String sql = "INSERT INTO rutas (parada_origen_id, parada_destino_id, tiempo_viaje, distancia, costo, transbordos) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, ruta.getParadaOrigen().getId());
            stmt.setString(2, ruta.getParadaDestino().getId());
            stmt.setDouble(3, ruta.getTiempoViaje());
            stmt.setDouble(4, ruta.getDistancia());
            stmt.setDouble(5, ruta.getCosto());
            stmt.setInt(6, ruta.getTransbordos());

            stmt.executeUpdate();
            System.out.println("Ruta guardada correctamente.");

        } catch (Exception e) {
            System.out.println("Error al guardar la ruta.");
            e.printStackTrace();
        }
    }

    // ACTUALIZA UNA RUTA EXISTENTE SEGUN ORIGEN Y DESTINO
    public void actualizar(Ruta ruta) {
        String sql = "UPDATE rutas SET tiempo_viaje = ?, distancia = ?, costo = ?, transbordos = ? " +
                "WHERE parada_origen_id = ? AND parada_destino_id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setDouble(1, ruta.getTiempoViaje());
            stmt.setDouble(2, ruta.getDistancia());
            stmt.setDouble(3, ruta.getCosto());
            stmt.setInt(4, ruta.getTransbordos());
            stmt.setString(5, ruta.getParadaOrigen().getId());
            stmt.setString(6, ruta.getParadaDestino().getId());

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

    // ELIMINA UNA RUTA SEGUN SU PARADA ORIGEN Y DESTINO
    public void eliminar(String idOrigen, String idDestino) {
        String sql = "DELETE FROM rutas WHERE parada_origen_id = ? AND parada_destino_id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, idOrigen);
            stmt.setString(2, idDestino);

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

    // OBTIENE TODAS LAS RUTAS DE LA BASE DE DATOS
    // NECESITA LAS PARADAS YA CARGADAS PARA RECONSTRUIR CADA OBJETO Ruta
    public List<Ruta> obtenerTodas() {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT * FROM rutas ORDER BY parada_origen_id, parada_destino_id";

        ParadaDAO paradaDAO = new ParadaDAO();

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String idOrigen = rs.getString("parada_origen_id");
                String idDestino = rs.getString("parada_destino_id");

                Parada origen = paradaDAO.buscarPorId(idOrigen);
                Parada destino = paradaDAO.buscarPorId(idDestino);

                if (origen != null && destino != null) {
                    Ruta ruta = new Ruta(
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