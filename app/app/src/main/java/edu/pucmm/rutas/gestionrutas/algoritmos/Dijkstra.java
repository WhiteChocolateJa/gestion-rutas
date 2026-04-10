package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.*;

/*
   Clase: Dijkstra
   Esta clase implementa el algoritmo de Dijkstra para encontrar la ruta óptima entre dos paradas dentro del grafo.
   Su funcionamiento depende del criterio de optimización seleccionado, por lo que puede calcular rutas por tiempo, distancia, costo o transbordos.
*/

public class Dijkstra {


/*
   Método: dijkstra
   Este método calcula la mejor ruta entre una parada origen y una parada destino dentro del grafo.
   Para hacerlo, utiliza un mapa de distancias para guardar el menor costo conocido hacia cada parada,
   un mapa de anteriores para reconstruir el camino final, y un conjunto de visitados para no repetir nodos ya procesados.
   Primero inicializa todas las distancias en infinito, excepto la parada de origen que comienza en 0.
   Luego recorre el grafo buscando siempre la parada no visitada con la menor distancia acumulada,
   actualizando las distancias de sus vecinos si encuentra un camino más corto.
   Al final, reconstruye el camino desde el destino hacia el origen utilizando el mapa de anteriores.
   Retorno:
      - Devuelve una lista de objetos Parada que representa la ruta óptima encontrada.
      - Si no existe ruta, devuelve una lista vacía.
*/

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

            for (Ruta r : grafo.getRutas().values()) {
                if (r.getParadaOrigen().equals(actual)) {
                    Parada vecino = r.getParadaDestino();
                    if (!visitados.contains(vecino)) {
                        double nuevaDistancia = distancia.get(actual) + r.getPeso(criterio);

                        if (nuevaDistancia < distancia.get(vecino)) {
                            distancia.put(vecino, nuevaDistancia);
                            anterior.put(vecino, actual);
                        }
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