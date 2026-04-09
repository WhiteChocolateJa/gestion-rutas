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

    // Carga el grafo completo desde la base de datos.
    // Primero se cargan las paradas y luego las rutas.
    public Grafo cargarGrafo() {
        Grafo grafo = new Grafo();

        List<Parada> paradas = paradaDAO.obtenerTodas();
        for (Parada parada : paradas) {
            grafo.anadirParada(parada);
        }

        List<Ruta> rutas = rutaDAO.obtenerTodas();
        for (Ruta ruta : rutas) {
            grafo.anadirRuta(ruta);
        }

        return grafo;
    }

    // Sincroniza el grafo actual con la base de datos.
    // Esta versión elimina todos los registros existentes
    // y luego vuelve a insertar el contenido actual del grafo.
    public void sincronizar(Grafo grafo) {

        List<Ruta> rutasExistentes = rutaDAO.obtenerTodas();
        for (Ruta ruta : rutasExistentes) {
            rutaDAO.eliminar(ruta.getId());
        }

        List<Parada> paradasExistentes = paradaDAO.obtenerTodas();
        for (Parada parada : paradasExistentes) {
            paradaDAO.eliminar(parada.getId());
        }

        for (Parada parada : grafo.getParadas().values()) {
            paradaDAO.guardar(parada);
        }

        for (Ruta ruta : grafo.getRutas().values()) {
            rutaDAO.guardar(ruta);
        }

        System.out.println("Grafo sincronizado correctamente con la base de datos.");
    }
}