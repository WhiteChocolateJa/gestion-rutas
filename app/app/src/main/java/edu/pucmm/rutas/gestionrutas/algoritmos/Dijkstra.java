package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.*;

public class Dijkstra {

    public List<Parada> dijkstra(Parada origen, Parada destino, Grafo grafo, CriterioOptimizacion criterio) {

        Map<Parada, Double> distancia = new HashMap<>();
        Map<Parada, Parada> anterior = new HashMap<>();
        Set<Parada> visitados = new HashSet<>();

        for (Parada p : grafo.getParadas().values()) {
            distancia.put(p, Double.MAX_VALUE);
        }

        distancia.put(origen, 0.0);

        Parada actual = origen;

        while (actual != null) {

            visitados.add(actual);

            for (Ruta r : actual.getRutasSalientes()) {
                Parada vecino = r.getParadaDestino();

                // NO SE RELAJA UN VECINO QUE YA FUE PROCESADO COMO VISITADO.
                if (!visitados.contains(vecino)) {
                    // USA EL PESO DE LA RUTA SEGÚN EL CRITERIO SELECCIONADO.
                    double nuevaDistancia = distancia.get(actual) + r.getPeso(criterio);


                    if (nuevaDistancia < distancia.get(vecino)) {
                        distancia.put(vecino, nuevaDistancia);
                        anterior.put(vecino, actual);
                    }
                }
            }
            actual = null;
            double menor = Double.MAX_VALUE;

            for (Parada p : distancia.keySet()) {

                if (!visitados.contains(p) && distancia.get(p) < menor) {
                    menor = distancia.get(p);
                    actual = p;
                }
            }
            if (actual != null && actual.equals(destino)) {
                break;
            }
        }

        // SI EL DESTINO SIGUE EN INFINITO, SIGNIFICA QUE NO EXISTE CAMINO DESDE EL ORIGEN.
        if (distancia.get(destino) == Double.MAX_VALUE) {
            return new ArrayList<>();
        }

        List<Parada> camino = new ArrayList<>();
        Parada paso = destino;
        while (paso != null) {
            camino.add(0, paso);
            paso = anterior.get(paso);
        }
        return camino;
    }
}