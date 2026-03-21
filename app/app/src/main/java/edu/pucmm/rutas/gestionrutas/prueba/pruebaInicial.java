package edu.pucmm.rutas.gestionrutas.prueba;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import edu.pucmm.rutas.gestionrutas.usos.GraphVisualizer;
import javafx.application.Application;
import javafx.stage.Stage;

public class pruebaInicial extends Application {

    @Override
    public void start(Stage stage) {
        Digraph<String, String> g = new DigraphEdgeList<>();

        var a = g.insertVertex("A");
        var b = g.insertVertex("B");
        var c = g.insertVertex("C");

        g.insertEdge(a, b, "A-B");
        g.insertEdge(a, c, "A-C");

        GraphVisualizer.show(stage, g);
    }

    public static void main(String[] args) {
        launch(args);
    }
}