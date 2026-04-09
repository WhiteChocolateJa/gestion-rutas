package edu.pucmm.rutas.gestionrutas.control;

import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.ui.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class controlParadas {

    private static final GrafoRepository grafoBaseDatos = new GrafoRepository();
    private static Grafo elGrafo = grafoBaseDatos.cargarGrafo();

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtZona;

    @FXML
    private ComboBox<Parada> cbxDireccion;

    @FXML
    public void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void aceptarLaParada() {
        String codigo = txtCodigo.getText();
        String nombre = txtNombre.getText();

        Parada parada = new Parada(codigo, nombre, 0.0, 0.0);
        parada.setDescripcion(txtDescripcion.getText());
        parada.setZona(txtZona.getText());

        elGrafo.anadirParada(parada);
        grafoBaseDatos.sincronizar(elGrafo);
        elGrafo = grafoBaseDatos.cargarGrafo();

        Stage stage = (Stage) txtCodigo.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        Grafo grafoActual = grafoBaseDatos.cargarGrafo();
        txtCodigo.setText("PAR-" + (grafoActual.getParadas().size() + 1));

        if (cbxDireccion != null) {
            cbxDireccion.getItems().setAll(grafoActual.getParadas().values());
        }
    }


    @FXML
    public void continuarACrearRuta() throws IOException {

        Parada nueva = new Parada(txtCodigo.getText(), txtNombre.getText());
        nueva.setZona(txtZona.getText());
        nueva.setDescripcion(txtDescripcion.getText());

        // 1. CARGAMOS Y GUARDAMOS LA PARADA PRIMERO
        Grafo grafoActual = grafoBaseDatos.cargarGrafo();
        grafoActual.anadirParada(nueva);
        grafoBaseDatos.sincronizar(grafoActual);
        // Ahora la base de datos ya tiene la parada oficialmente.

        if (!grafoActual.getParadas().isEmpty()){
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("/visual/crear_rutas.fxml")
            );
            Scene scene = new Scene(loader.load());

            controlRutas controller = loader.getController();

            controller.setParadaOrigen(nueva);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
        }

        Stage ventanaActual = (Stage) txtCodigo.getScene().getWindow();
        ventanaActual.close();
    }
}