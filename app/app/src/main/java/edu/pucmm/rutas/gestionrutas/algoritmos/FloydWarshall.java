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
 * Calcula el costo mínimo entre TODOS los pares de paradas del grafo.
 *
 * A diferencia de BFS o Dijkstra, este algoritmo no responde a una sola consulta,
 * sino que construye una matriz completa con las mejores rutas entre cualquier
 * origen y cualquier destino.
 */
public class FloydWarshall {

    // Calcula la matriz de costos mínimos entre todas las paradas del grafo
    // según el criterio de optimización seleccionado
    public double[][] calcular(Grafo grafo, CriterioOptimizacion criterio) {

        if (grafo == null) {
            return new double[0][0];
        }

        // Convierte las paradas del grafo en una lista indexada
        // para poder trabajar con filas y columnas en la matriz
        List<Parada> listaParadas = new ArrayList<>(grafo.getParadas().values());

        // Cantidad total de paradas
        int n = listaParadas.size();

        // Matriz de distancias:
        // distan[i][j] representa el costo mínimo conocido desde i hasta j
        double[][] distan = new double[n][n];

        // Mapa auxiliar para acceder rápidamente al índice de cada parada
        Map<Parada, Integer> indices = new HashMap<>();
        for (int i = 0; i < n; i++) {
            indices.put(listaParadas.get(i), i);
        }

        // Inicializa la matriz:
        // - 0 en la diagonal porque ir de una parada a sí misma no cuesta nada
        // - infinito cuando todavía no se conoce un camino entre dos paradas
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distan[i][j] = 0;
                } else {
                    distan[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        // Registra en la matriz los caminos directos entre paradas
        // usando el peso correspondiente al criterio elegido
        for (Ruta ruta : grafo.getRutas().values()) {

            // Obtiene el índice del origen y del destino de manera eficiente
            int i = indices.get(ruta.getParadaOrigen());
            int j = indices.get(ruta.getParadaDestino());

            // Guarda el menor costo directo entre esas dos paradas
            distan[i][j] = Math.min(distan[i][j], ruta.getPeso(criterio));
        }

        // Aplicación de Floyd-Warshall
        // k representa la parada intermedia que se intenta usar como puente
        for (int k = 0; k < n; k++) {

            // i representa el origen
            for (int i = 0; i < n; i++) {

                // j representa el destino
                for (int j = 0; j < n; j++) {

                    // Solo tiene sentido comparar si existe camino de i a k
                    // y también de k a j
                    if (distan[i][k] != Double.POSITIVE_INFINITY &&
                            distan[k][j] != Double.POSITIVE_INFINITY) {

                        // Si pasar por k mejora el costo actual de i a j,
                        // se actualiza la matriz con el nuevo costo mínimo
                        if (distan[i][k] + distan[k][j] < distan[i][j]) {
                            distan[i][j] = distan[i][k] + distan[k][j];
                        }
                    }
                }
            }
        }

        // Devuelve la matriz final con los costos mínimos
        // entre todos los pares de paradas
        return distan;
    }
}