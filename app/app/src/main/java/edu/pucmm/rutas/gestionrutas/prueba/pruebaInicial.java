package edu.pucmm.rutas.gestionrutas.prueba;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;
import edu.pucmm.rutas.gestionrutas.usos.GraphVisualizer;
import javafx.application.Application;
import javafx.stage.Stage;

public class pruebaInicial extends Application {

    @Override
    public void start(Stage stage) {
        Grafo grafo = new Grafo();

        Parada a = new Parada("A", "Parada A");
        Parada b = new Parada("B", "Parada B");
        Parada c = new Parada("C", "Parada C");
        Parada d = new Parada("D", "Parada D");

        grafo.anadirParada(a);
        grafo.anadirParada(b);
        grafo.anadirParada(c);
        grafo.anadirParada(d);

        Ruta r1 = new Ruta("1", b,d,10,50,100,1);
        grafo.anadirRuta(r1);
        Ruta r2 = new Ruta("2", b,d,10,50,100,1);
        grafo.anadirRuta(r2);
        Ruta r3 = new Ruta("3", a,c,10,50,100,1);
        grafo.anadirRuta(r3);
        Ruta r4 = new Ruta("4", c,a,10,50,100,1);
        grafo.anadirRuta(r4);

// luego mostrar
        GraphVisualizer.show(stage, grafo);
    }

    public static void main(String[] args) {
        launch(args);
    }
}