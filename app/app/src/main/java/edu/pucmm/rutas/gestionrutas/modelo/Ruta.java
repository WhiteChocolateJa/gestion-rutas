package edu.pucmm.rutas.gestionrutas.modelo;

import edu.pucmm.rutas.gestionrutas.algoritmos.CriterioOptimizacion;

public class Ruta {

    private String id;
    private Parada paradaOrigen;
    private Parada paradaDestino;
    private double tiempoViaje;
    private double distancia;
    private double costo;
    private int transbordos;

    public Ruta(String id, Parada paradaOrigen, Parada paradaDestino, double tiempoViaje, double distancia, double costo, int transbordos) {
        this.id = id;
        this.paradaOrigen = paradaOrigen;
        this.paradaDestino = paradaDestino;
        this.tiempoViaje = tiempoViaje;
        this.distancia = distancia;
        this.costo = costo;
        this.transbordos = transbordos;
    }
    public String getId() {
        return id;
    }
    public Parada getParadaOrigen() {
        return paradaOrigen;
    }

    public Parada getParadaDestino() {
        return paradaDestino;
    }

    public double getTiempoViaje() {
        return tiempoViaje;
    }

    public double getDistancia() {
        return distancia;
    }

    public double getCosto() {
        return costo;
    }

    public int getTransbordos() {
        return transbordos;
    }


    // Determina el peso de la ruta según el criterio de optimización seleccionado.
    // Este metodo permite que los algoritmos (por ejemplo Dijkstra) utilicen
    // diferentes métricas como tiempo, distancia, costo o transbordos sin
    // modificar la lógica del algoritmo, mejorando la escalabilidad del sistema.
    public double getPeso(CriterioOptimizacion criterio) {
        if (criterio == CriterioOptimizacion.TIEMPO) {
            return tiempoViaje;
        } else if (criterio == CriterioOptimizacion.DISTANCIA) {
            return distancia;
        } else if (criterio == CriterioOptimizacion.COSTO) {
            return costo;
        } else if (criterio == CriterioOptimizacion.TRANSBORDOS) {
            return transbordos;
        }
        throw new IllegalArgumentException("Criterio no válido");
    }

    @Override
    public String toString() {
        return id + " (" + tiempoViaje + " min)";
    }


}
