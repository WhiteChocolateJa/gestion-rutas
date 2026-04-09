module edu.pucmm.rutas.gestionrutas {
        requires javafx.controls;
        requires javafx.fxml;
        requires javafx.graphics;
        requires javafx.base;
        requires java.sql;

        opens edu.pucmm.rutas.gestionrutas.ui to javafx.fxml;
        opens edu.pucmm.rutas.gestionrutas.control to javafx.fxml;
        opens edu.pucmm.rutas.gestionrutas.modelo to javafx.base, com.brunomnsilva.smartgraph;

        exports edu.pucmm.rutas.gestionrutas.ui;
        exports edu.pucmm.rutas.gestionrutas.control;
        exports edu.pucmm.rutas.gestionrutas.modelo;
        exports edu.pucmm.rutas.gestionrutas.database;
        exports edu.pucmm.rutas.gestionrutas.algoritmos;
}