module edu.pucmm.rutas.gestionrutas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens edu.pucmm.rutas.gestionrutas to javafx.fxml;
    opens edu.pucmm.rutas.gestionrutas.modelo to javafx.base;

    exports edu.pucmm.rutas.gestionrutas;
    exports edu.pucmm.rutas.gestionrutas.modelo;
    exports edu.pucmm.rutas.gestionrutas.database;
    exports edu.pucmm.rutas.gestionrutas.algoritmos;
}