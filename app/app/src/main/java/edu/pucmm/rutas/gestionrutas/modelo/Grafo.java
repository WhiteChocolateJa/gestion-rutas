package edu.pucmm.rutas.gestionrutas.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {

    // MAPA PRINCIPAL DEL GRAFO.
    // GUARDA CADA PARADA POR SU ID PARA ACCESO RÁPIDO.
    private Map<String, Parada> paradas;
    private Map <String, Ruta> rutas;

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
    // ESTO EVITA DUPLICADOS BÁSICOS EN LA ESTRUCTURA.
    public void anadirParada(Parada paradaAgregar) {
        if(!this.paradas.containsKey(paradaAgregar.getId())) {
            this.paradas.put(paradaAgregar.getId(), paradaAgregar);
        }
    }

    // AGREGA UNA RUTA SOLO SI LA PARADA ORIGEN Y DESTINO EXISTEN EN EL GRAFO.
    // POR AHORA NO SE VALIDA DUPLICADO CON contains() PARA EVITAR DEPENDER DE equals() EN Ruta.
    public void anadirRuta(Ruta ruta) {
        Parada origen = ruta.getParadaOrigen();
        Parada destino = ruta.getParadaDestino();

        if (paradas.containsKey(origen.getId()) && paradas.containsKey(destino.getId())) {
            origen.agregarRuta(ruta);
            rutas.put(ruta.getId(), ruta);
        }
    }

    // MODIFICA UNA RUTA BUSCÁNDOLA DENTRO DE LAS RUTAS SALIENTES DE SU ORIGEN.
    // SE COMPARA POR EL ID DEL DESTINO PARA NO DEPENDER DE equals() EN Ruta.
    public void modificarRuta(Ruta rutaActualizada) {
        Parada origen = rutaActualizada.getParadaOrigen();
        List<Ruta> rutas2 = origen.getRutasSalientes();
        rutas.put(rutaActualizada.getId(), rutaActualizada);

        for (int i = 0; i < rutas2.size(); i++) {
            Ruta rutaExistente = rutas2.get(i);

            if (rutaExistente.getParadaDestino().getId()
                    .equals(rutaActualizada.getParadaDestino().getId())) {
                rutas2.set(i, rutaActualizada);
                break;
            }
        }
    }

    // MODIFICA LOS DATOS DE UNA PARADA REEMPLAZÁNDOLA EN EL MAPA SEGÚN SU ID.
    public void modificarParada(Parada parada) {
        this.paradas.put(parada.getId(), parada);
        for (Ruta r : parada.getRutasSalientes()){
            rutas.put(r.getId(), r);
        }
    }

    // ELIMINA UNA RUTA DE LA LISTA DE RUTAS SALIENTES DE SU PARADA ORIGEN.
    public void eliminarRuta(Ruta ruta) {
        ruta.getParadaOrigen().eliminarRuta(ruta);
        rutas.remove(ruta.getId());
    }

    // ELIMINA UNA PARADA DEL GRAFO.
    // ANTES DE BORRARLA, TAMBIÉN ELIMINA TODAS LAS RUTAS DE OTRAS PARADAS
    // QUE APUNTEN HACIA ESA PARADA PARA EVITAR REFERENCIAS INVÁLIDAS.
    public void eliminarParada(String idParada) {
        for (Ruta r : paradas.get(idParada).getRutasSalientes()){
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
    // SIRVE PARA BUSCAR PARADAS RÁPIDAMENTE DESDE EL GRAFO.
    public Parada obtenerParada(String id) {
        return paradas.get(id);
    }

}
