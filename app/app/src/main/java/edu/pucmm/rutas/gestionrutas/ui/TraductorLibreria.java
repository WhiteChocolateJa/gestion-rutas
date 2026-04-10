package edu.pucmm.rutas.gestionrutas.ui;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;


/*
   Clase: TraductorLibreria
   Esta clase se encarga de convertir el grafo del sistema (backend) en un formato compatible con la librería gráfica utilizada.
   Permite adaptar la estructura interna del grafo para que pueda ser visualizada en la interfaz.
*/
public class TraductorLibreria {



    /*
       Método: convertirGrafo
       Este método toma el grafo del sistema y lo transforma en un grafo que puede ser interpretado por la librería visual.
       Primero valida que el grafo no sea nulo.
       Luego crea un nuevo grafo visual.
       Después recorre todas las paradas del grafo original y las inserta como vértices en el grafo visual.
       Luego recorre todas las rutas y las inserta como aristas conectando las paradas correspondientes.
       Finalmente, devuelve el grafo listo para ser mostrado en pantalla.
       Retorno:
          - Devuelve un objeto Digraph que representa el grafo adaptado para la visualización.
    */
    public static Digraph<Parada, Ruta> convertirGrafo(Grafo miGrafoBackend) {

        if (miGrafoBackend == null) {
            return new DigraphEdgeList<>();
        }

        Digraph<Parada, Ruta> grafoVisual = new DigraphEdgeList<>();

        for (Parada parada : miGrafoBackend.getParadas().values()) {
            grafoVisual.insertVertex(parada);
        }

        for (Ruta ruta : miGrafoBackend.getRutas().values()) {
            grafoVisual.insertEdge(ruta.getParadaOrigen(), ruta.getParadaDestino(), ruta);
        }

        return grafoVisual;
    }
}