package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Algoritmo Floyd-Warshall
 *
 * Calcula el costo mínimo entre TODOS los pares de paradas del grafo
 * y reconstruye la ruta óptima entre un origen y destino específicos.
 */
public class FloydWarshall {

    public List<Parada> algoritmoFloydWarshall(Parada origen, Parada destino, Grafo grafo, CriterioOptimizacion criterio) {

        if (origen == null || destino == null || grafo == null ||
                !grafo.getParadas().containsKey(origen.getId()) ||
                !grafo.getParadas().containsKey(destino.getId())) {
            return new ArrayList<>();
        }

        List<Parada> listaParadas = new ArrayList<>(grafo.getParadas().values());
        int n = listaParadas.size();

        Map<Parada, Integer> indices = new HashMap<>();
        for (int i = 0; i < n; i++) {
            indices.put(listaParadas.get(i), i);
        }

        double[][] distan = new double[n][n];

        int[][] siguiente = new int[n][n];

        // 1. Inicializar las matrices
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distan[i][j] = Double.POSITIVE_INFINITY;
                siguiente[i][j] = -1;
            }
            distan[i][i] = 0;
            siguiente[i][i] = i;
        }

        for (Ruta ruta : grafo.getRutas().values()) {
            int i = indices.get(ruta.getParadaOrigen());
            int j = indices.get(ruta.getParadaDestino());
            double peso = ruta.getPeso(criterio);

            if (peso < distan[i][j]) {
                distan[i][j] = peso;
                siguiente[i][j] = j;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distan[i][k] != Double.POSITIVE_INFINITY && distan[k][j] != Double.POSITIVE_INFINITY) {
                        if (distan[i][k] + distan[k][j] < distan[i][j]) {
                            distan[i][j] = distan[i][k] + distan[k][j];
                            siguiente[i][j] = siguiente[i][k];
                        }
                    }
                }
            }
        }

        List<Parada> camino = new ArrayList<>();
        int indiceOrigen = indices.get(origen);
        int indiceDestino = indices.get(destino);

        if (distan[indiceOrigen][indiceDestino] == Double.POSITIVE_INFINITY || siguiente[indiceOrigen][indiceDestino] == -1) {
            return camino;
        }

        int actual = indiceOrigen;
        camino.add(listaParadas.get(actual));

        while (actual != indiceDestino) {
            actual = siguiente[actual][indiceDestino];
            camino.add(listaParadas.get(actual));
        }

        return camino;
    }
}