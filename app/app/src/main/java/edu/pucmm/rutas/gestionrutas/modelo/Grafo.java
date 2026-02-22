package edu.pucmm.rutas.gestionrutas.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {

    private Map<String, Parada> paradas;
    private Map<String, List<Ruta>> rutasPorParada;

    public Grafo() {
        this.paradas = new HashMap<>();
        this.rutasPorParada = new HashMap<>();
    }
}
