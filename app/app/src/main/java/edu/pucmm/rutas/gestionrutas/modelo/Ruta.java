package edu.pucmm.rutas.gestionrutas.modelo;

public class Ruta {

    private Parada paradaOrigen;
    private Parada paradaDestino;
    private double tiempoViaje;
    private double distancia;
    private double costo;
    private int transbordos;

    public Ruta(Parada paradaOrigen, Parada paradaDestino, double tiempoViaje, double distancia, double costo, int transbordos) {
        this.paradaOrigen = paradaOrigen;
        this.paradaDestino = paradaDestino;
        this.tiempoViaje = tiempoViaje;
        this.distancia = distancia;
        this.costo = costo;
        this.transbordos = transbordos;
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
}
