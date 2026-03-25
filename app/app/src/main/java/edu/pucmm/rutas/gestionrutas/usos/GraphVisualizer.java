package edu.pucmm.rutas.gestionrutas.usos;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import com.brunomnsilva.smartgraph.graphview.SmartRandomPlacementStrategy;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class GraphVisualizer {

    public static void show(Stage stage, Grafo grafoLogico) {
        Digraph<String, String> g = new DigraphEdgeList<>();
        Map<String, Vertex<String>> vertices = new HashMap<>();

        for (Parada parada : grafoLogico.getParadas().values()) {
            Vertex<String> v = g.insertVertex(parada.getId());
            vertices.put(parada.getId(), v);
        }

        for (Ruta ruta : grafoLogico.getRutas().values()) {
            String idOrigen = ruta.getParadaOrigen().getId();
            String idDestino = ruta.getParadaDestino().getId();

            Vertex<String> origen = vertices.get(idOrigen);
            Vertex<String> destino = vertices.get(idDestino);

            if (origen != null && destino != null) {
                g.insertEdge(origen, destino, ruta.getId());
            }
        }

        SmartGraphProperties props = new SmartGraphProperties();
        SmartRandomPlacementStrategy strategy = new SmartRandomPlacementStrategy();

        SmartGraphPanel<String, String> graphView =
                new SmartGraphPanel<>(g, props, strategy);

        Scene scene = new Scene(graphView, 800, 600);
        scene.getStylesheets().add(
                GraphVisualizer.class.getResource("/smartgraph.css").toExternalForm()
        );

        stage.setTitle("Visualización de Grafo");
        stage.setScene(scene);
        stage.show();

        graphView.init();
    }
}