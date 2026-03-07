package edu.pucmm.rutas.gestionrutas.database;

import edu.pucmm.rutas.gestionrutas.modelo.Parada;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ParadaDAO {

    // GUARDA UNA PARADA NUEVA EN LA BASE DE DATOS
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

    // ACTUALIZA LOS DATOS DE UNA PARADA EXISTENTE
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

    // ELIMINA UNA PARADA SEGUN SU ID
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

    // BUSCA UNA PARADA POR SU ID Y LA DEVUELVE SI EXISTE
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

    // OBTIENE TODAS LAS PARADAS DE LA BASE DE DATOS
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