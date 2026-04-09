package edu.pucmm.rutas.gestionrutas.ui;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

public class TraductorLibreria {

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