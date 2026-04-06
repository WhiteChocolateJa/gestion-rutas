package edu.pucmm.rutas.gestionrutas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/visual/ventanaPrincipal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Gestión de Rutas");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}