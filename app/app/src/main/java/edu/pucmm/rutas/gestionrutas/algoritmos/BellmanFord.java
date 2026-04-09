package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.HashMap;
import java.util.Map;

/*
 * Algoritmo Bellman-Ford
 *
 * Este algoritmo calcula la distancia mínima desde una parada origen
 * hacia todas las demás paradas del grafo.
 *
 * A diferencia de Dijkstra, Bellman-Ford puede trabajar con aristas
 * de peso negativo, lo cual es útil en escenarios donde existan
 * descuentos o ajustes especiales en los costos.
 *
 * También permite detectar ciclos negativos, es decir, ciclos cuyo
 * costo total es menor que cero.
 */
public class BellmanFord {

    /*
     * Parámetros:
     * - origen: parada desde la cual se empieza el cálculo
     * - grafo: estructura completa con paradas y rutas
     * - criterio: criterio usado como peso (tiempo, distancia, costo, transbordos)
     *
     * Retorna:
     * - Un mapa donde la clave es la parada y el valor es la distancia mínima
     *   desde el origen hasta esa parada
     */
    public Map<Parada, Double> algoritmoBellmanFord(Parada origen, Grafo grafo, CriterioOptimizacion criterio) {

        // Si no existe una parada origen válida, retorna un mapa vacío
        if (origen == null) {
            return new HashMap<>();
        }

        // Mapa que guarda la mejor distancia conocida desde el origen
        // hacia cada parada del grafo
        Map<Parada, Double> distancia = new HashMap<>();

        // Inicializa todas las distancias en infinito
        // porque al inicio no se conoce ningún camino
        for (Parada p : grafo.getParadas().values()) {
            distancia.put(p, Double.POSITIVE_INFINITY);
        }

        // La distancia desde el origen hacia sí mismo es 0
        distancia.put(origen, 0.0);

        // Cantidad total de vértices del grafo
        int vertices = grafo.getParadas().size();

        // Relaja todas las aristas V - 1 veces
        // Esto garantiza que se encuentren los caminos mínimos
        // si no existen ciclos negativos
        for (int i = 0; i < vertices - 1; i++) {

            for (Ruta ruta : grafo.getRutas().values()) {

                // Parada de inicio y fin de la ruta actual
                Parada inicio = ruta.getParadaOrigen();
                Parada destino = ruta.getParadaDestino();

                // Peso de la ruta según el criterio seleccionado
                double peso = ruta.getPeso(criterio);

                // Si se conoce un camino hasta "inicio"
                // y pasar por esta ruta mejora la distancia hacia "destino",
                // entonces se actualiza el valor
                if (distancia.get(inicio) != Double.POSITIVE_INFINITY &&
                        distancia.get(inicio) + peso < distancia.get(destino)) {

                    distancia.put(destino, distancia.get(inicio) + peso);
                }
            }
        }

        // Verifica si existe un ciclo negativo
        // Si todavía se puede mejorar una distancia después de V - 1 iteraciones,
        // significa que el grafo contiene un ciclo negativo
        for (Ruta ruta : grafo.getRutas().values()) {
            Parada inicio = ruta.getParadaOrigen();
            Parada destino = ruta.getParadaDestino();
            double peso = ruta.getPeso(criterio);

            if (distancia.get(inicio) != Double.POSITIVE_INFINITY &&
                    distancia.get(inicio) + peso < distancia.get(destino)) {

                throw new RuntimeException("El grafo contiene un ciclo negativo.");
            }
        }

        // Devuelve el mapa final con las distancias mínimas
        // desde el origen hacia todas las demás paradas
        return distancia;
    }
}