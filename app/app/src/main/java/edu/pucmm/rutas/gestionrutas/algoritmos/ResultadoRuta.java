package edu.pucmm.rutas.gestionrutas.algoritmos;

import edu.pucmm.rutas.gestionrutas.modelo.Parada;

import java.util.ArrayList;
import java.util.List;

/*
 * ResultadoRuta
 *
 * Esta clase representa el resultado general de cualquier algoritmo de rutas del sistema.
 *
 * La idea es tener un solo objeto de salida para BFS, DFS, Dijkstra, Bellman-Ford
 * y Floyd-Warshall, sin obligar a que todos produzcan exactamente la misma información.
 *
 * Según el algoritmo usado, algunos campos tendrán valor y otros no.
 *
 * Ejemplos:
 * - BFS, Dijkstra y Bellman-Ford: llenan camino y costoTotal.
 * - DFS: normalmente llena existeCamino.
 * - Floyd-Warshall: llena matrizCostos.
 */
public class ResultadoRuta {

    // Nombre del algoritmo que generó este resultado.
    // Ejemplo: "BFS", "DFS", "Dijkstra", "Bellman-Ford", "Floyd-Warshall"
    private String algoritmo;

    // Indica si el algoritmo encontró una solución válida.
    // Por ejemplo:
    // - en BFS o Dijkstra, si encontró un camino
    // - en DFS, si existe conexión
    // - en Floyd, si la matriz se calculó correctamente
    private boolean exitoso;

    // Mensaje explicativo del resultado.
    // Sirve para mostrar algo entendible en consola o en la interfaz.
    private String mensaje;

    // Ruta concreta encontrada entre origen y destino.
    // Este campo es útil para BFS, Dijkstra y Bellman-Ford.
    private List<Parada> camino;

    // Costo total de la ruta encontrada.
    // Puede representar tiempo, distancia, costo o transbordos,
    // dependiendo del criterio usado.
    private double costoTotal;

    // Criterio con el que se calculó la ruta.
    // Se usa sobre todo en algoritmos con peso como Dijkstra, Bellman-Ford y Floyd.
    private CriterioOptimizacion criterio;

    // Indica si existe un camino entre el origen y el destino.
    // Es especialmente útil para DFS, aunque también puede usarse en otros algoritmos.
    private boolean existeCamino;

    // Matriz de costos mínimos entre todos los pares de paradas.
    // Se usa principalmente con Floyd-Warshall.
    private double[][] matrizCostos;

    /*
     * Constructor vacío.
     *
     * Inicializa la lista del camino para evitar valores null.
     * Útil cuando se quiere crear el resultado poco a poco con setters.
     */
    public ResultadoRuta() {
        this.camino = new ArrayList<>();
    }

    /*
     * Constructor para algoritmos que devuelven una ruta concreta.
     *
     * Recomendado para BFS, Dijkstra y Bellman-Ford.
     */
    public ResultadoRuta(String algoritmo, boolean exitoso, String mensaje,
                         List<Parada> camino, double costoTotal,
                         CriterioOptimizacion criterio) {
        this.algoritmo = algoritmo;
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.camino = camino;
        this.costoTotal = costoTotal;
        this.criterio = criterio;
        this.existeCamino = exitoso;
    }

    /*
     * Constructor para algoritmos que solo verifican existencia de camino.
     *
     * Recomendado para DFS.
     */
    public ResultadoRuta(String algoritmo, boolean existeCamino, String mensaje) {
        this.algoritmo = algoritmo;
        this.existeCamino = existeCamino;
        this.exitoso = existeCamino;
        this.mensaje = mensaje;
        this.camino = new ArrayList<>();
    }

    /*
     * Constructor para algoritmos globales que generan una matriz.
     *
     * Recomendado para Floyd-Warshall.
     */
    public ResultadoRuta(String algoritmo, boolean exitoso, String mensaje,
                         double[][] matrizCostos, CriterioOptimizacion criterio) {
        this.algoritmo = algoritmo;
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.matrizCostos = matrizCostos;
        this.criterio = criterio;
        this.camino = new ArrayList<>();
    }

    // Devuelve el nombre del algoritmo usado.
    public String getAlgoritmo() {
        return algoritmo;
    }

    // Permite asignar el nombre del algoritmo usado.
    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    // Indica si el algoritmo terminó con un resultado útil.
    public boolean isExitoso() {
        return exitoso;
    }

    // Permite marcar si el resultado fue exitoso.
    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    // Devuelve un mensaje descriptivo del resultado.
    public String getMensaje() {
        return mensaje;
    }

    // Permite asignar un mensaje descriptivo.
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    // Devuelve la lista de paradas del camino encontrado.
    public List<Parada> getCamino() {
        return camino;
    }

    // Permite asignar el camino encontrado.
    public void setCamino(List<Parada> camino) {
        this.camino = camino;
    }

    // Devuelve el costo total de la ruta.
    public double getCostoTotal() {
        return costoTotal;
    }

    // Permite asignar el costo total de la ruta.
    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    // Devuelve el criterio usado para calcular el resultado.
    public CriterioOptimizacion getCriterio() {
        return criterio;
    }

    // Permite asignar el criterio de optimización.
    public void setCriterio(CriterioOptimizacion criterio) {
        this.criterio = criterio;
    }

    // Indica si existe camino entre origen y destino.
    public boolean isExisteCamino() {
        return existeCamino;
    }

    // Permite marcar si existe camino.
    public void setExisteCamino(boolean existeCamino) {
        this.existeCamino = existeCamino;
    }

    // Devuelve la matriz de costos mínimos.
    public double[][] getMatrizCostos() {
        return matrizCostos;
    }

    // Permite asignar la matriz de costos mínimos.
    public void setMatrizCostos(double[][] matrizCostos) {
        this.matrizCostos = matrizCostos;
    }
}