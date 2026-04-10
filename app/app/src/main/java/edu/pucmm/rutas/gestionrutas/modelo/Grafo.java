package edu.pucmm.rutas.gestionrutas.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
   Clase: Grafo
   Esta clase representa la estructura principal del sistema de rutas.
   Se encarga de almacenar todas las paradas y rutas del grafo, permitiendo agregar, modificar, eliminar y consultar estos elementos.
   Internamente utiliza mapas para acceder rápidamente a las paradas y rutas mediante su id.
*/

public class Grafo {


    private Map<String, Parada> paradas;
    private Map<String, Ruta> rutas;

    /*
   Métodos: getters
   Se utilizan para obtener los mapas completos de paradas y rutas almacenados en el grafo.
   Retorno:
      - Devuelven un Map<String, Parada> o un Map<String, Ruta>, según el caso.
*/

    public Map<String, Parada> getParadas() {
        return paradas;
    }

    public Map<String, Ruta> getRutas() {
        return rutas;
    }


    /*
   Constructor
   Se encarga de inicializar la estructura del grafo creando dos mapas vacíos: uno para las paradas y otro para las rutas.
   No devuelve ningún valor, solo crea el objeto.
*/
    public Grafo() {
        this.paradas = new HashMap<>();
        this.rutas = new HashMap<>();
    }

/*
   Método: anadirParada
   Este método agrega una nueva parada al grafo, siempre que no exista otra con el mismo id.
   Su objetivo es evitar duplicados y mantener organizada la estructura del sistema.
   Retorno:
      - No devuelve ningún valor.
*/

    public void anadirParada(Parada paradaAgregar) {
        if (!this.paradas.containsKey(paradaAgregar.getId())) {
            this.paradas.put(paradaAgregar.getId(), paradaAgregar);
        }
    }

/*
   Método: anadirRuta
   Este método agrega una ruta al grafo solo si las paradas de origen y destino ya existen dentro de la estructura.
   Además de guardar la ruta en el mapa general, también la agrega a la lista de rutas salientes de la parada origen.
   Esto permite mantener correctamente la conexión entre nodos.
   Retorno:
      - No devuelve ningún valor.
*/

    public void anadirRuta(Ruta ruta) {
        Parada origen = ruta.getParadaOrigen();
        Parada destino = ruta.getParadaDestino();

        if (paradas.containsKey(origen.getId()) && paradas.containsKey(destino.getId())) {
            origen.agregarRuta(ruta);
            rutas.put(ruta.getId(), ruta);
        }
    }

    /*
   Método: modificarRuta
   Este método actualiza una ruta existente dentro del grafo.
   Primero reemplaza la ruta en el mapa general de rutas y luego busca esa misma ruta dentro de las rutas salientes de la parada origen para sustituirla por la versión actualizada.
   Retorno:
      - No devuelve ningún valor.
*/

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

    /*
   Método: modificarParada
   Este método actualiza la información de una parada dentro del mapa principal del grafo.
   Además, vuelve a registrar en el mapa de rutas todas las rutas salientes asociadas a esa parada para mantener consistencia.
   Retorno:
      - No devuelve ningún valor.
*/

    public void modificarParada(Parada parada) {
        this.paradas.put(parada.getId(), parada);
        for (Ruta r : parada.getRutasSalientes()) {
            rutas.put(r.getId(), r);
        }
    }

    /*
   Método: eliminarRuta
   Este método elimina una ruta del grafo.
   Primero la quita de la lista de rutas salientes de la parada origen y luego la elimina del mapa general de rutas.
   Retorno:
      - No devuelve ningún valor.
*/

    public void eliminarRuta(Ruta ruta) {
        ruta.getParadaOrigen().eliminarRuta(ruta);
        rutas.remove(ruta.getId());
    }

    /*
   Método: eliminarParada
   Este método elimina una parada del grafo junto con todas las rutas relacionadas con ella.
   Primero elimina las rutas que salen de esa parada, luego recorre las demás paradas para eliminar también las rutas que llegan a ella, y finalmente la elimina del mapa principal.
   Retorno:
      - No devuelve ningún valor.
*/

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

    /*
   Método: obtenerParada
   Este método permite buscar una parada dentro del grafo utilizando su id.
   Se utiliza cuando se necesita acceder directamente a una parada específica.
   Retorno:
      - Devuelve un objeto Parada si existe, o null si no se encuentra.
*/
    public Parada obtenerParada(String id) {
        return paradas.get(id);
    }

    /*
   Método: obtenerRutaDirecta
   Este método busca una ruta directa entre dos paradas específicas, en el sentido origen -> destino.
   Recorre todas las rutas del grafo y compara si coinciden las paradas indicadas.
   Retorno:
      - Devuelve un objeto Ruta si existe una conexión directa, o null si no existe.
*/

    public Ruta obtenerRutaDirecta(Parada p1, Parada p2) {
        for (Ruta ruta : rutas.values()) {
            if (ruta.getParadaOrigen().equals(p1) && ruta.getParadaDestino().equals(p2)) {
                return ruta;
            }
        }
        return null;
    }
}