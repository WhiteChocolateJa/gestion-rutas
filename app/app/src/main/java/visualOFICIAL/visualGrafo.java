package visualOFICIAL;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class visualGrafo extends Application {

    @Override
    public void start(Stage primaryStage) {

        Grafo miGrafo = new Grafo();


        Parada p1 = new Parada("P1", "Campus Principal", 10.0, 20.0);
        Parada p2 = new Parada("P2", "Estación Centro", 50.0, 60.0);
        Parada p3 = new Parada("P3", "Zona Norte", 90.0, 10.0);

        miGrafo.anadirParada(p1);
        miGrafo.anadirParada(p2);
        miGrafo.anadirParada(p3);

        Ruta r1 = new Ruta("R101", p1, p2, 15.0, 5.5, 30.0, 0);
        Ruta r2 = new Ruta("R102", p2, p3, 10.0, 3.2, 25.0, 0);
        Ruta r3 = new Ruta("R103", p3, p1, 20.0, 8.0, 35.0, 1); // Ruta de vuelta para probar

        miGrafo.anadirRuta(r1);
        miGrafo.anadirRuta(r2);
        miGrafo.anadirRuta(r3);


        Digraph<Parada, Ruta> grafoListoParaDibujar = traductorLibreria.convertirGrafo(miGrafo);

        SmartPlacementStrategy estrategia = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<Parada, Ruta> panelVisual = new SmartGraphPanel<>(grafoListoParaDibujar, estrategia);

        Scene scene = new Scene(panelVisual, 800, 600);
        try {
            String cssPath = getClass().getResource("/smartgraph.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (NullPointerException e) {
            System.err.println("¡CRÍTICO!: Java sigue sin ver el archivo smartgraph.css en la raíz de resources.");
        }
        primaryStage.setTitle("Simulador de Rutas de Transporte");
        primaryStage.setScene(scene);
        primaryStage.show();

        panelVisual.init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}