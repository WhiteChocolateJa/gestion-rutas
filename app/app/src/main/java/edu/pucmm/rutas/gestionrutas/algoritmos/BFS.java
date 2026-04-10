package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.*;


/*
   Clase: BFS
   Esta clase implementa el algoritmo Breadth-First Search (Búsqueda en Anchura).
   Se utiliza para encontrar un camino entre dos paradas recorriendo el grafo por niveles,
   es decir, explorando primero las rutas más cercanas antes de profundizar.
*/
public class BFS {


    /*
       Método: buscarCamino
       Este método busca un camino entre una parada origen y una parada destino utilizando BFS.
       Primero valida que los datos no sean nulos.
       Luego utiliza una cola para recorrer el grafo por niveles, un conjunto de visitados para no repetir paradas,
       y un mapa de anteriores para reconstruir el camino final.
       Comienza agregando la parada origen a la cola y marcándola como visitada.
       Después recorre el grafo sacando elementos de la cola y agregando sus vecinos no visitados.
       Cuando encuentra el destino, detiene la búsqueda.
       Finalmente, reconstruye el camino desde el destino hasta el origen utilizando el mapa de anteriores.
       Retorno:
          - Devuelve una lista de objetos Parada que representa el camino encontrado.
          - Si no existe ruta, devuelve una lista vacía.
    */
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