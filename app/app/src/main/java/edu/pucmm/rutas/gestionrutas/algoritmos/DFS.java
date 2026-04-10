package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/*
   Clase: DFS
   Esta clase implementa el algoritmo Depth-First Search (Búsqueda en Profundidad).
   Se utiliza para encontrar un camino entre dos paradas explorando el grafo de forma profunda,
   es decir, avanzando por un camino hasta el final antes de probar otros.
*/
public class DFS {


    /*
       Método: buscarCamino
       Este método busca un camino entre una parada origen y una parada destino utilizando DFS.
       Primero inicializa una lista para almacenar el camino y un conjunto de visitados para evitar ciclos.
       Luego valida que los datos no sean nulos.
       Después llama a un método recursivo que se encarga de explorar el grafo.
       Si se encuentra el destino, devuelve el camino construido.
       Si no se encuentra, devuelve una lista vacía.
       Retorno:
          - Devuelve una lista de objetos Parada que representa el camino encontrado.
          - Si no existe ruta, devuelve una lista vacía.
    */
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


    /*
       Método: dfsRecursivo
       Este método es el núcleo del algoritmo DFS.
       Marca la parada actual como visitada y la agrega al camino.
       Luego verifica si es el destino; si lo es, termina la búsqueda.
       Si no, recorre todas las rutas que salen de la parada actual e intenta avanzar hacia cada vecino no visitado.
       Si alguno de esos caminos lleva al destino, retorna true y se detiene.
       Si ningún camino funciona, elimina la parada actual del camino (retroceso) y retorna false.
       Retorno:
          - Devuelve true si se encontró el destino.
          - Devuelve false si no se encontró por ese camino.
    */
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