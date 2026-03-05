package edu.pucmm.rutas.gestionrutas.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {

    public Map<String, Parada> getParadas() {
        return paradas;
    }

    private Map<String, Parada> paradas;
    private Map<String, List<Ruta>> rutasPorParada;

    public Grafo() {
        this.paradas = new HashMap<>();
        this.rutasPorParada = new HashMap<>();
    }
    //AGREGAR
    public void anadirParada(Parada paragregar) {
        if(!this.paradas.containsKey(paragregar.getId())) {
            this.paradas.put(paragregar.getId(), paragregar);
        }
    }

    public void anadirRuta(Ruta ruta) {
        Parada origen = ruta.getParadaOrigen();
        Parada destino = ruta.getParadaDestino();
        if (paradas.containsKey(origen.getId()) && paradas.containsKey(destino.getId())) {
            if (!origen.getRutasSalientes().contains(ruta)) {
                origen.getRutasSalientes().add(ruta);
            }
        }
    }
    //MODIFICAR
    public void modificarRuta(Ruta rutaActualizada) {
        Parada origen = rutaActualizada.getParadaOrigen();
        List<Ruta> rutas = origen.getRutasSalientes();
        for (int i = 0; i < rutas.size(); i++) {
            if (rutaActualizada.equals(rutas.get(i))) {
                rutas.set(i, rutaActualizada);
                break;
            }
        }
    }
    public void modificarparada (Parada parada) {
        this.paradas.put(parada.getId(), parada);
    }
    //ELIMINAR

    public void eliminarRuta(Ruta ruta) {
        ruta.getParadaOrigen().getRutasSalientes().remove(ruta);
    }
    public void eliminarParada(String idParada) {
        for (Parada p : paradas.values()) {
            List<Ruta> rutas = p.getRutasSalientes();
            for (int i = 0; i < rutas.size(); i++) {
                if (rutas.get(i).getParadaDestino().getId().equals(idParada)) {
                    rutas.remove(i);
                    i--;
                }
            }
        }
        paradas.remove(idParada);
    }

}
