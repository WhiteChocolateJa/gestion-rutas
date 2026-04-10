package edu.pucmm.rutas.gestionrutas.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/*
   Clase: HelloApplication
   Esta clase es el punto de inicio de la aplicación JavaFX.
   Se encarga de cargar la interfaz principal, aplicar los estilos y mostrar la ventana al usuario.
*/
public class HelloApplication extends Application {

    /*
   Método: start
   Este método se ejecuta automáticamente al iniciar la aplicación.
   Se encarga de cargar el archivo FXML de la ventana principal,
   crear la escena, aplicar los estilos CSS y configurar la ventana.
   Finalmente muestra la interfaz en pantalla maximizada.
   Retorno:
      - No devuelve ningún valor.
*/
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("/visual/ventanaPrincipal.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());

        // Conectar CSS
        scene.getStylesheets().add(
                HelloApplication.class.getResource("/smartgraph.css").toExternalForm()
        );

        stage.setTitle("Gestión de Rutas");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    /*
   Método: main
   Este método inicia la ejecución del programa.
   Llama al método launch, que arranca el ciclo de vida de JavaFX y ejecuta el método start.
   Retorno:
      - No devuelve ningún valor.
*/
    public static void main(String[] args) {
        launch(args);
    }
}