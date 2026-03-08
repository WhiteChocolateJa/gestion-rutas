package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.HashMap;
import java.util.Map;

public class BellmanFord {

    public Map<Parada, Double> algoritmoBellmanFord(Parada origen, Grafo grafo, CriterioOptimizacion criterio) {

        Map<Parada, Double> distancia = new HashMap<>();

        for (Parada p : grafo.getParadas().values()) {
            distancia.put(p, Double.MAX_VALUE);
        }
        distancia.put(origen, 0.0);

        int vertices = grafo.getParadas().size();

        for (int i = 0; i < vertices - 1; i++) {

            for (Ruta rutica : grafo.getRutas().values()) {

                Parada inicio = rutica.getParadaOrigen();
                Parada destino = rutica.getParadaDestino();
                double peso = rutica.getPeso(criterio);

                if (distancia.get(inicio) != Double.MAX_VALUE && distancia.get(inicio) + peso < distancia.get(destino)) {
                    distancia.put(destino, distancia.get(inicio) + peso);
                }
            }
        }
        /*for (Ruta ruta : grafo.getRutas().values()) {
            Parada inicio = ruta.getParadaOrigen();
            Parada destino = ruta.getParadaDestino();
            double peso = ruta.getPeso(criterio);
            if (distancia.get(inicio) != Double.MAX_VALUE && distancia.get(inicio) + peso < distancia.get(destino)) {
                throw new RuntimeException("El grafo tiene un ciclo negativo");
            }
        }*/
        return distancia;
    }
}
