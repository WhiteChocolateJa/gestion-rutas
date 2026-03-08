package edu.pucmm.rutas.gestionrutas.prueba;

import edu.pucmm.rutas.gestionrutas.algoritmos.CriterioOptimizacion;
import edu.pucmm.rutas.gestionrutas.algoritmos.Dijkstra;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;

import java.util.List;

public class pruebaInicial {

    public static void main(String[] args) {

        Grafo grafo = new Grafo();

        Parada A = new Parada("A", "Parada A", 0, 0);
        Parada B = new Parada("B", "Parada B", 1, 1);
        Parada C = new Parada("C", "Parada C", 2, 2);
        Parada D = new Parada("D", "Parada D", 3, 3);
        Parada E = new Parada("E", "Parada E", 4, 4);

        System.out.println("Paradas creadas: A, B, C, D, E");

        grafo.anadirParada(A);
        grafo.anadirParada(B);
        grafo.anadirParada(C);
        grafo.anadirParada(D);
        grafo.anadirParada(E);

        System.out.println("Total de paradas en el grafo: " + grafo.getParadas().size());

        // CREAR RUTAS CON ID
        Ruta r1 = new Ruta("R1", A, B, 5, 5, 5, 1);
        Ruta r2 = new Ruta("R2", A, C, 8, 5, 5, 0);
        Ruta r3 = new Ruta("R3", C, D, 3, 3, 5, 0);
        Ruta r4 = new Ruta("R4", B, D, 6, 6, 12, 0);

        System.out.println("Rutas creadas:");
        System.out.println("R1: A -> B");
        System.out.println("R2: A -> C");
        System.out.println("R3: C -> D");
        System.out.println("R4: B -> D");

        grafo.anadirRuta(r1);
        grafo.anadirRuta(r2);
        grafo.anadirRuta(r3);
        grafo.anadirRuta(r4);

        System.out.println("\nMOSTRANDO LISTA DE ADYACENCIA DEL GRAFO...");
        mostrarRutasSalientes(A);
        mostrarRutasSalientes(B);
        mostrarRutasSalientes(C);
        mostrarRutasSalientes(D);
        mostrarRutasSalientes(E);

        System.out.println("\nPROBANDO BUSQUEDA DE PARADA POR ID...");
        Parada paradaBuscada = grafo.obtenerParada("C");

        if (paradaBuscada != null) {
            System.out.println("Parada encontrada: " + paradaBuscada.getNombre());
        } else {
            System.out.println("No se encontro la parada.");
        }

        System.out.println("\nPROBANDO MODIFICACION DE PARADA...");
        C.setNombre("Parada C Modificada");
        grafo.modificarParada(C);
        System.out.println("Nuevo nombre de la parada C: " + grafo.obtenerParada("C").getNombre());

        System.out.println("\nPROBANDO MODIFICACION DE RUTA...");
        Ruta r1Modificada = new Ruta("R1", A, B, 4, 4, 4, 1);
        grafo.modificarRuta(r1Modificada);

        System.out.println("Ruta R1 modificada.");
        mostrarRutasSalientes(A);

        System.out.println("\nPROBANDO DIJKSTRA CON DIFERENTES CRITERIOS...");
        Dijkstra dijkstra = new Dijkstra();

        probarDijkstra(grafo, dijkstra, A, D, CriterioOptimizacion.TIEMPO);
        probarDijkstra(grafo, dijkstra, A, D, CriterioOptimizacion.DISTANCIA);
        probarDijkstra(grafo, dijkstra, A, D, CriterioOptimizacion.COSTO);
        probarDijkstra(grafo, dijkstra, A, D, CriterioOptimizacion.TRANSBORDOS);

        System.out.println("\nPROBANDO CASO SIN CAMINO POSIBLE...");
        probarDijkstra(grafo, dijkstra, A, E, CriterioOptimizacion.DISTANCIA);

        System.out.println("\nPROBANDO ELIMINACION DE RUTA...");
        grafo.eliminarRuta(r4);
        System.out.println("Ruta R4 eliminada.");
        mostrarRutasSalientes(B);

        System.out.println("\nPROBANDO ELIMINACION DE PARADA...");
        grafo.eliminarParada("C");
        System.out.println("Parada C eliminada.");

        System.out.println("Paradas restantes en el grafo:");
        for (Parada p : grafo.getParadas().values()) {
            System.out.println("- " + p.getNombre());
        }

        System.out.println("\nRutas salientes despues de eliminar C:");
        mostrarRutasSalientes(A);
        mostrarRutasSalientes(B);
        mostrarRutasSalientes(D);
        mostrarRutasSalientes(E);

        System.out.println("\nPROBANDO DIJKSTRA DESPUES DE ELIMINAR C...");
        probarDijkstra(grafo, dijkstra, A, D, CriterioOptimizacion.DISTANCIA);
    }

    public static void mostrarRutasSalientes(Parada parada) {
        System.out.println("\nRutas salientes de " + parada.getNombre() + ":");

        if (parada.getRutasSalientes().isEmpty()) {
            System.out.println("No tiene rutas salientes.");
            return;
        }

        for (Ruta ruta : parada.getRutasSalientes()) {
            System.out.println("  -> ID: " + ruta.getId()
                    + " | Destino: " + ruta.getParadaDestino().getNombre()
                    + " | Tiempo: " + ruta.getTiempoViaje()
                    + " | Distancia: " + ruta.getDistancia()
                    + " | Costo: " + ruta.getCosto()
                    + " | Transbordos: " + ruta.getTransbordos());
        }
    }

    public static void probarDijkstra(Grafo grafo, Dijkstra dijkstra, Parada origen, Parada destino, CriterioOptimizacion criterio) {
        System.out.println();
        System.out.println("Probando Dijkstra");
        System.out.println("Origen: " + origen.getNombre());
        System.out.println("Destino: " + destino.getNombre());
        System.out.println("Criterio: " + criterio);

        List<Parada> camino = dijkstra.dijkstra(origen, destino, grafo, criterio);

        if (camino.isEmpty()) {
            System.out.println("No existe camino entre las paradas indicadas.");
            return;
        }

        System.out.println("Ruta encontrada:");
        for (int i = 0; i < camino.size(); i++) {
            Parada paradaActual = camino.get(i);
            System.out.print(paradaActual.getNombre());

            if (i != camino.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}