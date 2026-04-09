/*
 * DFS (Depth-First Search)
 *
 * Este algoritmo se utiliza para determinar si existe al menos un camino
 * entre dos paradas dentro del grafo.
 *
 * Explora el grafo en profundidad, avanzando por un camino hasta que no
 * puede continuar, y luego retrocede.
 */
package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.HashSet;
import java.util.Set;

public class DFS {

    public boolean existeCamino(Parada origen, Parada destino) {
        Set<Parada> visitados = new HashSet<>();
        return dfs(origen, destino, visitados);       // esto es para meterse profundo en el grafo
    }
    private boolean dfs(Parada actual, Parada destino, Set<Parada> visitados) {
        if (actual.equals(destino)) { // caso base, si ya llegaste termina.
            return true;
        }
        visitados.add(actual); // marcar visitados, para evitar ciclos
        for (Ruta ruta : actual.getRutasSalientes()){ // mira todas las rutas desde esa parada
            Parada vecino = ruta.getParadaDestino();

            if (!visitados.contains(vecino)) {
                if (dfs(vecino, destino, visitados)) { // si un camino sirve ya terminamos
                    return true;
                }
            }
        }

        return false; // no hay camino
    }



}
