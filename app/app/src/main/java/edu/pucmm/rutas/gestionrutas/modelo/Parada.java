package edu.pucmm.rutas.gestionrutas.modelo;

import java.util.ArrayList;
import java.util.List;

/*
   Clase: Parada
   Esta clase representa una parada dentro del sistema de rutas. Funciona como un nodo del grafo, ya que desde ella pueden salir múltiples rutas hacia otras paradas.
   Contiene información básica como el id, nombre, ubicación, estado, descripción y zona, además de una lista de rutas salientes que permite recorrer el grafo.
*/


public class Parada {

    private String id;
    private String nombre;
    private double x;
    private double y;
    private boolean activa;
    private String descripcion;
    private String zona;
    private List<Ruta> rutasSalientes;


    /*
   Constructor principal
   Se encarga de crear una parada con todos sus datos básicos (id, nombre y coordenadas).
   También inicializa la parada como activa y crea la lista de rutas salientes vacía.
   No devuelve ningún valor, solo crea el objeto.
*/

    public Parada(String id, String nombre, double x, double y) {
        this.id = id;
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.activa = true;
        this.rutasSalientes = new ArrayList<Ruta>();
    }

    /*
   Constructor simplificado
   Permite crear una parada sin necesidad de especificar coordenadas, asignando valores por defecto (0.0, 0.0).
   No devuelve ningún valor, solo crea el objeto.
*/

    public Parada(String id, String nombre){
        this(id, nombre, 0.0, 0.0);
    }

    /*
   Getters y Setters
   Se utilizan para acceder y modificar los atributos de la parada como nombre, zona, descripción, coordenadas, etc.
   Los getters devuelven el valor del atributo y los setters no devuelven ningún valor.
*/

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isActiva() {
        return activa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getZona() {
        return zona;
    }

    public List<Ruta> getRutasSalientes() {
        return rutasSalientes;
    }

    /*
   Método: agregarRuta
   Permite agregar una nueva ruta a la lista de rutas salientes de la parada.
   Esto es importante porque crea conexiones dentro del grafo.
   No devuelve ningún valor.
*/

    public void agregarRuta(Ruta ruta) {
        this.rutasSalientes.add(ruta);
    }

    /*
   Método: eliminarRuta
   Permite eliminar una ruta existente de la lista de rutas salientes.
   Se utiliza cuando se quiere quitar una conexión entre paradas.
   No devuelve ningún valor.
*/

    public void eliminarRuta(Ruta ruta) {
        this.rutasSalientes.remove(ruta);
    }


    /*
   Método: toString
   Devuelve el nombre de la parada en formato texto.
   Se utiliza principalmente para mostrar la parada en la interfaz gráfica.
   Devuelve un String.
*/

    @Override
    public String toString() {
        return nombre;
    }


    /*
   Método: equals
   Permite comparar dos paradas para determinar si son la misma.
   La comparación se hace utilizando el id, ya que este es único para cada parada.
   Devuelve true si son iguales y false en caso contrario.
*/

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Parada otra = (Parada) obj;
        return this.getId().equals(otra.getId());
    }

    /*
   Método: hashCode
   Genera un código hash basado en el id de la parada.
   Esto es necesario para que la parada funcione correctamente en estructuras como HashSet o HashMap.
   Devuelve un número entero (int).
*/
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
