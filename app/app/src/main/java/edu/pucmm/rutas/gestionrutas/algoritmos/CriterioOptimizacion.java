package edu.pucmm.rutas.gestionrutas.algoritmos;

/*
   Tipo: Enum (CriterioOptimizacion)
   Este enum define los diferentes criterios que se pueden utilizar para calcular una ruta óptima.
   Permite seleccionar si la ruta se debe optimizar por tiempo, distancia, costo o cantidad de transbordos.
   Se utiliza principalmente en los algoritmos para determinar qué valor tomar como peso en cada ruta.
*/

public enum CriterioOptimizacion {
    TIEMPO,
    DISTANCIA,
    COSTO,
    TRANSBORDOS
}
