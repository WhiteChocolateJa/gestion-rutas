module edu.pucmm.rutas.gestionrutas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens edu.pucmm.rutas.gestionrutas to javafx.fxml;
    exports edu.pucmm.rutas.gestionrutas;
}