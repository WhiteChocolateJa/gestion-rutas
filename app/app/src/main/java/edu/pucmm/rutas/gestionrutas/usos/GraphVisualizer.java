package edu.pucmm.rutas.gestionrutas.usos;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GraphVisualizer {

    public static <V, E> void show(Stage stage, Digraph<V, E> graph) {

        // Crear el panel del grafo
        SmartGraphPanel<V, E> graphView = new SmartGraphPanel<>(graph);

        // Crear escena
        Scene scene = new Scene(graphView, 800, 600);

        // Cargar CSS automáticamente
        String css = GraphVisualizer.class
                .getResource("/smartgraph.css")
                .toExternalForm();

        scene.getStylesheets().add(css);

        // Mostrar ventana
        stage.setTitle("Visualización de Grafo");
        stage.setScene(scene);
        stage.show();

        // Inicializar grafo
        graphView.init();
    }
}