// el algoritmo bfs para este sistema es para implementar una opcion que diga "Encontrar una ruta mas directa"
//esto significa la ruta con menos paradas intermedias, o sea el camino con menos saltos.

/*
Qué es BFS
BFS significa:

Breadth-First Search
o en español: búsqueda en anchura

Es un algoritmo que recorre un grafo por niveles.

Idea simple

Si empiezas en una parada A, BFS hace esto:
	•	primero visita las paradas directamente conectadas con A
	•	luego las conectadas con esas
	•	luego las siguientes
	•	y así sucesivamente

O sea, va avanzando “capa por capa”.

Cómo se ve mentalmente

Supón este grafo:
	•	A -> B
	•	A -> C
	•	B -> D
	•	C -> E

Si empiezas en A, BFS visita en este orden:
	•	A
	•	B, C
	•	D, E

Primero los vecinos cercanos, luego los más lejanos.
* */

package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.*;

public class BFS {

    public List<Parada> buscarCamino(Parada origen, Parada destino) {
        Queue<Parada> cola = new LinkedList<>(); // estructura de datos fifo. Esto tiene dos metodos: poll-> que saca y devuelve el primer elemento, lo saca de la cola y lo devuelve. peek solo mira el primer elemento, lo devuelve pero no lo elimina
        Set<Parada> visitados = new HashSet<>(); // esto es para marcar que ya lo visitamos y no volverlo a hacer
        Map<Parada, Parada> anterior = new HashMap<>(); // reconstruye el camino hasta el final

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
                    visitados.add(vecino); // ya pase por aqui
                    anterior.put(vecino, actual); // llegue hasta vecino desde actual
                    cola.add(vecino); // despues me tocara procesarlo
                }
            }
        }

        if (!visitados.contains(destino)) { // si el destino nunca fue visitado, no existe camino.
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
