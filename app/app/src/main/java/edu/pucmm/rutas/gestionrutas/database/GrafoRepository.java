package edu.pucmm.rutas.gestionrutas.database;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.List;

public class GrafoRepository {

    private ParadaDAO paradaDAO;
    private RutaDAO rutaDAO;

    public GrafoRepository() {
        this.paradaDAO = new ParadaDAO();
        this.rutaDAO = new RutaDAO();
    }

    // CARGA EL GRAFO COMPLETO DESDE LA BASE DE DATOS
    // PRIMERO CARGA LAS PARADAS Y LUEGO LAS RUTAS
    public Grafo cargarGrafo() {
        Grafo grafo = new Grafo();

        // CARGAR PARADAS
        List<Parada> paradas = paradaDAO.obtenerTodas();
        for (Parada parada : paradas) {
            grafo.anadirParada(parada);
        }

        // CARGAR RUTAS
        List<Ruta> rutas = rutaDAO.obtenerTodas();
        for (Ruta ruta : rutas) {
            grafo.anadirRuta(ruta);
        }

        return grafo;
    }

    // SINCRONIZA EL GRAFO ACTUAL CON LA BASE DE DATOS
    // ESTA VERSION BORRA TODO Y VUELVE A INSERTAR
    public void sincronizar(Grafo grafo) {

        // ELIMINAR TODAS LAS RUTAS
        List<Ruta> rutasExistentes = rutaDAO.obtenerTodas();
        for (Ruta ruta : rutasExistentes) {
            rutaDAO.eliminar(ruta.getId());
        }

        // ELIMINAR TODAS LAS PARADAS
        List<Parada> paradasExistentes = paradaDAO.obtenerTodas();
        for (Parada parada : paradasExistentes) {
            paradaDAO.eliminar(parada.getId());
        }

        // GUARDAR PARADAS DEL GRAFO ACTUAL
        for (Parada parada : grafo.getParadas().values()) {
            paradaDAO.guardar(parada);
        }

        // GUARDAR RUTAS DEL GRAFO ACTUAL
        for (Ruta ruta : grafo.getRutas().values()) {
            rutaDAO.guardar(ruta);
        }

        System.out.println("Grafo sincronizado correctamente con la base de datos.");
    }
}