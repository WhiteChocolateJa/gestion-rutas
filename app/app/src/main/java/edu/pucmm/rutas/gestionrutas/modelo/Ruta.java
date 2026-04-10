package edu.pucmm.rutas.gestionrutas.modelo;

import edu.pucmm.rutas.gestionrutas.algoritmos.CriterioOptimizacion;


/*
   Clase: Ruta
   Esta clase representa una conexión entre dos paradas dentro del sistema.
   Funciona como una arista del grafo, ya que une una parada origen con una parada destino.
   Además, almacena información importante como el tiempo de viaje, distancia, costo y número de transbordos.
*/


public class Ruta {

    private String id;
    private Parada paradaOrigen;
    private Parada paradaDestino;
    private double tiempoViaje;
    private double distancia;
    private double costo;
    private int transbordos;

    /*
   Constructor
   Se encarga de crear una ruta con todos sus datos (id, parada origen, parada destino y sus métricas).
   Permite definir completamente la conexión entre dos paradas dentro del grafo.
   No devuelve ningún valor, solo crea el objeto.
*/

    public Ruta(String id, Parada paradaOrigen, Parada paradaDestino, double tiempoViaje, double distancia, double costo, int transbordos) {
        this.id = id;
        this.paradaOrigen = paradaOrigen;
        this.paradaDestino = paradaDestino;
        this.tiempoViaje = tiempoViaje;
        this.distancia = distancia;
        this.costo = costo;
        this.transbordos = transbordos;
    }


    /*
   Métodos: getters
   Se utilizan para acceder a los datos de la ruta como el id, origen, destino, tiempo, distancia, costo y transbordos.
   Retorno:
      - Devuelven valores según el atributo (String, Parada, double o int).
*/

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


    /*
   Método: getPeso
   Este método es clave dentro del sistema, ya que permite obtener el valor de la ruta dependiendo del criterio seleccionado.
   Según el criterio (tiempo, distancia, costo o transbordos), devuelve el valor correspondiente.
   Esto permite que los algoritmos como Dijkstra funcionen con distintos criterios sin cambiar su lógica.
   Retorno:
      - Devuelve un valor tipo double que representa el peso de la ruta según el criterio.
*/
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

    /*
   Método: toString
   Devuelve el identificador de la ruta en formato texto.
   Se utiliza principalmente para mostrar la ruta en la interfaz.
   Retorno:
      - Devuelve un String.
*/
    @Override
    public String toString() {
        return id;
    }


}
