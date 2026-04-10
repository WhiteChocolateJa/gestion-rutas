package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
   Clase: FloydWarshall
   Esta clase implementa el algoritmo de Floyd-Warshall para encontrar el costo mínimo entre todas las parejas de paradas del grafo.
   A diferencia de otros algoritmos, calcula todas las rutas posibles de una vez y luego permite reconstruir el camino entre un origen y un destino específico.
*/
public class FloydWarshall {


    /*
       Método: algoritmoFloydWarshall
       Este método calcula la mejor ruta entre una parada origen y una parada destino utilizando el algoritmo de Floyd-Warshall.
       Primero valida que los datos sean correctos.
       Luego convierte las paradas en una lista y les asigna índices para poder trabajar con matrices.
       Después inicializa dos matrices: una de distancias y otra para reconstruir el camino.
       Luego carga los valores iniciales según las rutas existentes en el grafo.
       A continuación, utiliza tres ciclos anidados para evaluar si pasar por una parada intermedia mejora el costo entre dos paradas.
       Este proceso permite encontrar el costo mínimo entre todos los pares de nodos.
       Finalmente, reconstruye el camino desde el origen hasta el destino utilizando la matriz de seguimiento.
       Retorno:
          - Devuelve una lista de objetos Parada que representa la ruta óptima encontrada.
          - Si no existe ruta, devuelve una lista vacía.
    */
    public List<Parada> algoritmoFloydWarshall(Parada origen, Parada destino, Grafo grafo, CriterioOptimizacion criterio) {

        if (origen == null || destino == null || grafo == null ||
                !grafo.getParadas().containsKey(origen.getId()) ||
                !grafo.getParadas().containsKey(destino.getId())) {
            return new ArrayList<>();
        }

        List<Parada> listaParadas = new ArrayList<>(grafo.getParadas().values());
        int n = listaParadas.size();

        Map<Parada, Integer> indices = new HashMap<>();
        for (int i = 0; i < n; i++) {
            indices.put(listaParadas.get(i), i);
        }

        double[][] distan = new double[n][n];

        int[][] siguiente = new int[n][n];

        //Inicializar las matrices
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distan[i][j] = Double.POSITIVE_INFINITY;
                siguiente[i][j] = -1;
            }
            distan[i][i] = 0;
            siguiente[i][i] = i;
        }

        for (Ruta ruta : grafo.getRutas().values()) {
            int i = indices.get(ruta.getParadaOrigen());
            int j = indices.get(ruta.getParadaDestino());
            double peso = ruta.getPeso(criterio);

            if (peso < distan[i][j]) {
                distan[i][j] = peso;
                siguiente[i][j] = j;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distan[i][k] != Double.POSITIVE_INFINITY && distan[k][j] != Double.POSITIVE_INFINITY) {
                        if (distan[i][k] + distan[k][j] < distan[i][j]) {
                            distan[i][j] = distan[i][k] + distan[k][j];
                            siguiente[i][j] = siguiente[i][k];
                        }
                    }
                }
            }
        }

        List<Parada> camino = new ArrayList<>();
        int indiceOrigen = indices.get(origen);
        int indiceDestino = indices.get(destino);

        if (distan[indiceOrigen][indiceDestino] == Double.POSITIVE_INFINITY || siguiente[indiceOrigen][indiceDestino] == -1) {
            return camino;
        }

        int actual = indiceOrigen;
        camino.add(listaParadas.get(actual));

        while (actual != indiceDestino) {
            actual = siguiente[actual][indiceDestino];
            camino.add(listaParadas.get(actual));
        }

        return camino;
    }
}