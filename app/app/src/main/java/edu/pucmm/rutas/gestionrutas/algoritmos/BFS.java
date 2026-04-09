package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.*;

public class BFS {

    public List<Parada> buscarCamino(Parada origen, Parada destino, Grafo grafo) {
        if (origen == null || destino == null || grafo == null) {
            return new ArrayList<>();
        }

        Queue<Parada> cola = new LinkedList<>();
        Set<String> visitados = new HashSet<>();
        Map<String, Parada> anterior = new HashMap<>();

        cola.add(origen);
        visitados.add(origen.getId());

        while (!cola.isEmpty()) {
            Parada actual = cola.poll();

            if (actual.getId().equals(destino.getId())) {
                break;
            }

            for (Ruta ruta : grafo.getRutas().values()) {
                if (ruta.getParadaOrigen().getId().equals(actual.getId())) {

                    Parada vecino = ruta.getParadaDestino();

                    if (!visitados.contains(vecino.getId())) {
                        visitados.add(vecino.getId());
                        anterior.put(vecino.getId(), actual);
                        cola.add(vecino);
                    }
                }
            }
        }

        if (!visitados.contains(destino.getId())) {
            return new ArrayList<>();
        }

        List<Parada> camino = new ArrayList<>();
        Parada paso = destino;

        while (paso != null) {
            camino.add(0, paso);
            paso = anterior.get(paso.getId());
        }

        return camino;
    }
}