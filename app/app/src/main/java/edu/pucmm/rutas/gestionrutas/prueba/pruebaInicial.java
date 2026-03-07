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
        Parada E = new Parada("E", "Parada E", 4, 4); // se usará para probar caso sin camino
        System.out.println("Paradas creadas: A, B, C, D, E");

        grafo.anadirParada(A);
        grafo.anadirParada(B);
        grafo.anadirParada(C);
        grafo.anadirParada(D);
        grafo.anadirParada(E);
        System.out.println("Total de paradas en el grafo: " + grafo.getParadas().size());

        // A -> B: rápida, distancia media, barata, 1 transbordo
        Ruta r1 = new Ruta(A, B, 5, 5, 5, 1);

        // A -> C: más lenta, misma distancia, mismo costo, 0 transbordos
        Ruta r2 = new Ruta(A, C, 8, 5, 5, 0);

        // C -> D: rápida y sin transbordo
        Ruta r3 = new Ruta(C, D, 3, 3, 5, 0);

        // B -> D: más tiempo, más distancia, más costo, 0 transbordos
        Ruta r4 = new Ruta(B, D, 6, 6, 12, 0);

        System.out.println("Rutas creadas:");
        System.out.println("A -> B");
        System.out.println("A -> C");
        System.out.println("C -> D");
        System.out.println("B -> D");


        // 5) AGREGANDO RUTAS AL GRAFO
        grafo.anadirRuta(r1);
        grafo.anadirRuta(r2);
        grafo.anadirRuta(r3);
        grafo.anadirRuta(r4);

        // 6) MOSTRANDO LAS RUTAS SALIENTES DE CADA PARADA
        System.out.println("\nMOSTRANDO LISTA DE ADYACENCIA DEL GRAFO...");

        mostrarRutasSalientes(A);
        mostrarRutasSalientes(B);
        mostrarRutasSalientes(C);
        mostrarRutasSalientes(D);
        mostrarRutasSalientes(E);

        // 7) PRUEBA DE BUSQUEDA DE PARADA
        System.out.println("\nPROBANDO BUSQUEDA DE PARADA POR ID...");
        Parada paradaBuscada = grafo.obtenerParada("C");

        if (paradaBuscada != null) {
            System.out.println("Parada encontrada: " + paradaBuscada.getNombre());
        } else {
            System.out.println("No se encontro la parada.");
        }


        // 8) PRUEBA DE MODIFICACION DE PARADA
        System.out.println("\nPROBANDO MODIFICACION DE PARADA...");

        C.setNombre("Parada C Modificada");
        grafo.modificarParada(C);

        System.out.println("Nuevo nombre de la parada C: " + grafo.obtenerParada("C").getNombre());

        // 9) PRUEBA DE MODIFICACION DE RUTA
        System.out.println("\nPROBANDO MODIFICACION DE RUTA...");

        // Se modifica la ruta A -> B
        Ruta r1Modificada = new Ruta(A, B, 4, 4, 4, 1);
        grafo.modificarRuta(r1Modificada);

        System.out.println("Ruta A -> B modificada.");
        mostrarRutasSalientes(A);

        // 10) PRUEBAS DE DIJKSTRA CON DISTINTOS CRITERIOS
        System.out.println("\nPROBANDO DIJKSTRA CON DIFERENTES CRITERIOS...");

        Dijkstra dijkstra = new Dijkstra();

        probarDijkstra(grafo, dijkstra, A, D, CriterioOptimizacion.TIEMPO);
        probarDijkstra(grafo, dijkstra, A, D, CriterioOptimizacion.DISTANCIA);
        probarDijkstra(grafo, dijkstra, A, D, CriterioOptimizacion.COSTO);
        probarDijkstra(grafo, dijkstra, A, D, CriterioOptimizacion.TRANSBORDOS);


        // 11) CASO SIN CAMINO POSIBLE
        System.out.println("\nPROBANDO CASO SIN CAMINO POSIBLE...");
        probarDijkstra(grafo, dijkstra, A, E, CriterioOptimizacion.DISTANCIA);

        // 12) PRUEBA DE ELIMINACION DE RUTA
        System.out.println("\nPROBANDO ELIMINACION DE RUTA...");

        grafo.eliminarRuta(r4); // elimina B -> D
        System.out.println("Ruta B -> D eliminada.");
        mostrarRutasSalientes(B);

        // 13) PRUEBA DE ELIMINACION DE PARADA
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

        // 14) PRUEBA FINAL DE DIJKSTRA DESPUES DE ELIMINAR C
        System.out.println("\nPROBANDO DIJKSTRA DESPUES DE ELIMINAR C...");
        probarDijkstra(grafo, dijkstra, A, D, CriterioOptimizacion.DISTANCIA);

    }

    // Muestra todas las rutas salientes de una parada
    public static void mostrarRutasSalientes(Parada parada) {
        System.out.println("\nRutas salientes de " + parada.getNombre() + ":");

        if (parada.getRutasSalientes().isEmpty()) {
            System.out.println("No tiene rutas salientes.");
            return;
        }

        for (Ruta ruta : parada.getRutasSalientes()) {
            System.out.println("  -> " + ruta.getParadaDestino().getNombre()
                    + " | Tiempo: " + ruta.getTiempoViaje()
                    + " | Distancia: " + ruta.getDistancia()
                    + " | Costo: " + ruta.getCosto()
                    + " | Transbordos: " + ruta.getTransbordos());
        }
    }

    // Ejecuta Dijkstra y muestra el resultado de forma clara
    public static void probarDijkstra(Grafo grafo, Dijkstra dijkstra, Parada origen, Parada destino, CriterioOptimizacion criterio) {
        System.out.println("\n");
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