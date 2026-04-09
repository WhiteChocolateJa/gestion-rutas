package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DFS {
    public List<Parada> buscarCamino(Parada origen, Parada destino, Grafo grafo) {
        List<Parada> camino = new ArrayList<>();
        Set<String> visitados = new HashSet<>();

        if (origen == null || destino == null || grafo == null) {
            return camino;
        }

        if (dfsRecursivo(origen, destino, visitados, camino, grafo)) {
            return camino;
        }

        return new ArrayList<>();
    }

    private boolean dfsRecursivo(Parada actual, Parada destino, Set<String> visitados, List<Parada> camino, Grafo grafo) {
        visitados.add(actual.getId());
        camino.add(actual);

        if (actual.getId().equals(destino.getId())) {
            return true;
        }

        for (Ruta ruta : grafo.getRutas().values()) {
            if (ruta.getParadaOrigen().getId().equals(actual.getId())) {

                Parada vecino = ruta.getParadaDestino();

                if (!visitados.contains(vecino.getId())) {
                    if (dfsRecursivo(vecino, destino, visitados, camino, grafo)) {
                        return true;
                    }
                }
            }
        }

        camino.remove(camino.size() - 1);
        return false;
    }
}