package edu.pucmm.rutas.gestionrutas.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {

    // MAPA PRINCIPAL DEL GRAFO.
    // GUARDA CADA PARADA POR SU ID PARA ACCESO RÁPIDO.
    private Map<String, Parada> paradas;
    private Map<String, Ruta> rutas;

    public Map<String, Parada> getParadas() {
        return paradas;
    }

    public Map<String, Ruta> getRutas() {
        return rutas;
    }

    public Grafo() {
        this.paradas = new HashMap<>();
        this.rutas = new HashMap<>();
    }

    // AGREGA UNA PARADA AL GRAFO SOLO SI SU ID NO EXISTE YA.
    public void anadirParada(Parada paradaAgregar) {
        if (!this.paradas.containsKey(paradaAgregar.getId())) {
            this.paradas.put(paradaAgregar.getId(), paradaAgregar);
        }
    }

    // AGREGA UNA RUTA SOLO SI LA PARADA ORIGEN Y DESTINO EXISTEN EN EL GRAFO.
    public void anadirRuta(Ruta ruta) {
        Parada origen = ruta.getParadaOrigen();
        Parada destino = ruta.getParadaDestino();

        if (paradas.containsKey(origen.getId()) && paradas.containsKey(destino.getId())) {
            origen.agregarRuta(ruta);
            rutas.put(ruta.getId(), ruta);
        }
    }

    // MODIFICA UNA RUTA EXISTENTE.
    public void modificarRuta(Ruta rutaActualizada) {
        Parada origen = rutaActualizada.getParadaOrigen();
        List<Ruta> rutas2 = origen.getRutasSalientes();
        rutas.put(rutaActualizada.getId(), rutaActualizada);

        for (int i = 0; i < rutas2.size(); i++) {
            Ruta rutaExistente = rutas2.get(i);

            if (rutaExistente.getId().equals(rutaActualizada.getId())) {
                rutas2.set(i, rutaActualizada);
                break;
            }
        }
    }

    // MODIFICA LOS DATOS DE UNA PARADA.
    public void modificarParada(Parada parada) {
        this.paradas.put(parada.getId(), parada);
        for (Ruta r : parada.getRutasSalientes()) {
            rutas.put(r.getId(), r);
        }
    }

    // ELIMINA UNA RUTA DEL GRAFO.
    public void eliminarRuta(Ruta ruta) {
        ruta.getParadaOrigen().eliminarRuta(ruta);
        rutas.remove(ruta.getId());
    }

    // ELIMINA UNA PARADA Y TODAS SUS RUTAS RELACIONADAS.
    public void eliminarParada(String idParada) {
        Parada parada = paradas.get(idParada);
        if (parada == null) return;

        for (Ruta r : parada.getRutasSalientes()) {
            rutas.remove(r.getId());
        }

        for (Parada p : paradas.values()) {
            List<Ruta> rutas2 = p.getRutasSalientes();
            for (int i = 0; i < rutas2.size(); i++) {
                if (rutas2.get(i).getParadaDestino().getId().equals(idParada)) {
                    rutas.remove(rutas2.get(i).getId());
                    rutas2.remove(i);
                    i--;
                }
            }
        }

        paradas.remove(idParada);
    }

    // DEVUELVE LA PARADA ASOCIADA AL ID INDICADO.
    public Parada obtenerParada(String id) {
        return paradas.get(id);
    }

    // DEVUELVE LA RUTA DIRECTA EN EL SENTIDO p1 -> p2.
    public Ruta obtenerRutaDirecta(Parada p1, Parada p2) {
        for (Ruta ruta : rutas.values()) {
            if (ruta.getParadaOrigen().equals(p1) && ruta.getParadaDestino().equals(p2)) {
                return ruta;
            }
        }
        return null;
    }
}