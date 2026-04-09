package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.*;

/*
 * BFS (Breadth-First Search)
 *
 * Este algoritmo se utiliza para encontrar la ruta más directa entre
 * dos paradas, es decir, el camino con menor cantidad de saltos.
 *
 * Recorre el grafo por niveles usando una cola, evita repetir nodos
 * con un conjunto de visitados y reconstruye el camino con ayuda
 * de un mapa de nodos anteriores.
 */
public class BFS {

    public List<Parada> buscarCamino(Parada origen, Parada destino) {
        if (origen == null || destino == null) {
            return new ArrayList<>();
        }

        Queue<Parada> cola = new LinkedList<>();
        Set<Parada> visitados = new HashSet<>();
        Map<Parada, Parada> anterior = new HashMap<>();

        cola.add(origen);
        visitados.add(origen);

        while (!cola.isEmpty()) {
            Parada actual = cola.poll();

            if (actual.equals(destino)) {
                break;
            }

            for (Ruta ruta : actual.getRutasSalientes()) {
                Parada vecino = ruta.getParadaDestino();

                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    anterior.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        if (!visitados.contains(destino)) {
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