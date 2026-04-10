package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
   Clase: BellmanFord
   Esta clase implementa el algoritmo de Bellman-Ford para encontrar la ruta óptima entre dos paradas.
   A diferencia de Dijkstra, permite trabajar con pesos negativos y también detectar ciclos negativos en el grafo.
*/
public class BellmanFord {

    /*
       Método: algoritmoBellmanFord
       Este método calcula la mejor ruta entre una parada origen y una parada destino dentro del grafo.
       Primero valida que las paradas existan en el grafo.
       Luego inicializa un mapa de distancias (con infinito) y un mapa de predecesores para reconstruir el camino.
       Después realiza múltiples iteraciones (cantidad de vértices - 1) relajando todas las rutas,
       es decir, intentando mejorar las distancias conocidas si encuentra un camino más corto.
       Luego hace una pasada adicional para verificar si existe un ciclo negativo.
       Si lo detecta, lanza una excepción.
       Finalmente, reconstruye el camino desde el destino hacia el origen utilizando los predecesores.
       Retorno:
          - Devuelve una lista de objetos Parada que representa la ruta óptima encontrada.
          - Si no existe ruta, devuelve una lista vacía.
    */
    public List<Parada> algoritmoBellmanFord(Parada origen, Parada destino, Grafo grafo, CriterioOptimizacion criterio) {

        if (origen == null || destino == null || !grafo.getParadas().containsKey(origen.getId()) || !grafo.getParadas().containsKey(destino.getId())) {
            return new ArrayList<>();
        }

        Map<Parada, Double> distancia = new HashMap<>();
        Map<Parada, Parada> predecesores = new HashMap<>();

        for (Parada p : grafo.getParadas().values()) {
            distancia.put(p, Double.POSITIVE_INFINITY);
            predecesores.put(p, null);
        }

        distancia.put(origen, 0.0);

        int vertices = grafo.getParadas().size();

        for (int i = 0; i < vertices - 1; i++) {
            for (Ruta ruta : grafo.getRutas().values()) {
                Parada inicio = ruta.getParadaOrigen();
                Parada fin = ruta.getParadaDestino();
                double peso = ruta.getPeso(criterio);

                if (distancia.get(inicio) != Double.POSITIVE_INFINITY &&
                        distancia.get(inicio) + peso < distancia.get(fin)) {

                    distancia.put(fin, distancia.get(inicio) + peso);
                    predecesores.put(fin, inicio);
                }
            }
        }

        for (Ruta ruta : grafo.getRutas().values()) {
            Parada inicio = ruta.getParadaOrigen();
            Parada fin = ruta.getParadaDestino();
            double peso = ruta.getPeso(criterio);

            if (distancia.get(inicio) != Double.POSITIVE_INFINITY &&
                    distancia.get(inicio) + peso < distancia.get(fin)) {
                throw new RuntimeException("El grafo contiene un ciclo negativo.");
            }
        }

        List<Parada> camino = new ArrayList<>();

        if (distancia.get(destino) == Double.POSITIVE_INFINITY) {
            return camino;
        }

        Parada actual = destino;
        while (actual != null) {
            camino.add(actual);
            actual = predecesores.get(actual);
        }

        Collections.reverse(camino);

        return camino;
    }
}