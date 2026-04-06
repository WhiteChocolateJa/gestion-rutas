package edu.pucmm.rutas.gestionrutas.modelo;

import java.util.ArrayList;
import java.util.List;

public class Parada {

    private String id;
    private String nombre;
    private double x;
    private double y;
    private boolean activa;
    private String descripcion;
    private String zona;
    private List<Ruta> rutasSalientes;

    public Parada(String id, String nombre, double x, double y) {
        this.id = id;
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.activa = true;
        this.rutasSalientes = new ArrayList<Ruta>();
    }

    public Parada(String id, String nombre){
        this(id, nombre, 0.0, 0.0);
    }

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

    public void agregarRuta(Ruta ruta) {
        this.rutasSalientes.add(ruta);
    }

    public void eliminarRuta(Ruta ruta) {
        this.rutasSalientes.remove(ruta);
    }



    @Override
    public String toString() {
        return nombre;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parada otraParada = (Parada) o;

        return this.id != null && this.id.equals(otraParada.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}
