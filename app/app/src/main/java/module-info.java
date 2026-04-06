module edu.pucmm.rutas.gestionrutas {
        requires javafx.controls;
        requires javafx.fxml;
        requires javafx.graphics;
        requires javafx.base;
        requires java.sql;
        requires java.desktop;
        requires java.logging;

        opens edu.pucmm.rutas.gestionrutas to javafx.fxml;
        opens edu.pucmm.rutas.gestionrutas.modelo to javafx.base;
        opens edu.pucmm.rutas.gestionrutas.prueba to javafx.graphics;

        exports edu.pucmm.rutas.gestionrutas;
        exports edu.pucmm.rutas.gestionrutas.modelo;
        exports edu.pucmm.rutas.gestionrutas.database;
        exports edu.pucmm.rutas.gestionrutas.algoritmos;
        exports edu.pucmm.rutas.gestionrutas.prueba;
        exports visualOFICIAL;
}