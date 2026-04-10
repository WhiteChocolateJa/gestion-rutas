package edu.pucmm.rutas.gestionrutas.database;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.List;


/*
   Clase: GrafoRepository
   Esta clase se encarga de conectar el grafo del sistema con la base de datos.
   Permite cargar toda la información desde la base de datos para construir el grafo,
   y también guardar los cambios del grafo nuevamente en la base de datos.
*/

public class GrafoRepository {

    private ParadaDAO paradaDAO;
    private RutaDAO rutaDAO;

    /*
   Constructor
   Se encarga de inicializar los objetos ParadaDAO y RutaDAO, los cuales permiten acceder a la base de datos.
   Esto permite que la clase pueda trabajar tanto con paradas como con rutas.
   No devuelve ningún valor.
*/

    public GrafoRepository() {
        this.paradaDAO = new ParadaDAO();
        this.rutaDAO = new RutaDAO();
    }

/*
   Método: cargarGrafo
   Este método construye un grafo completo a partir de los datos almacenados en la base de datos.
   Primero obtiene todas las paradas y las agrega al grafo, y luego obtiene todas las rutas y las conecta.
   De esta forma se reconstruye la estructura completa del sistema en memoria.
   Retorno:
      - Devuelve un objeto Grafo completamente cargado.
*/

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

/*
   Método: sincronizar
   Este método actualiza la base de datos con el estado actual del grafo.
   Primero elimina todas las rutas y paradas existentes en la base de datos,
   y luego vuelve a insertar todas las paradas y rutas que están en el grafo actual.
   Esto garantiza que la base de datos refleje exactamente el contenido del grafo.
   Retorno:
      - No devuelve ningún valor.
*/
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