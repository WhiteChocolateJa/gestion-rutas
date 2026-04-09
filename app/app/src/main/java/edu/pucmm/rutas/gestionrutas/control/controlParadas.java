package edu.pucmm.rutas.gestionrutas.control;

import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.ui.HelloApplication;
import edu.pucmm.rutas.gestionrutas.ui.HelloController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    private HelloController helloController;

    @FXML
    public void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void aceptarLaParada() {

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debe completar los campos obligatorios: Código y Nombre.");
            alert.showAndWait();
            return;
        }

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
    public void continuarACrearRuta() throws IOException {

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debe completar los campos obligatorios: Código y Nombre.");
            alert.showAndWait();
            return;
        }

        Parada nueva = new Parada(codigo, nombre, 0.0, 0.0);
        nueva.setDescripcion(txtDescripcion.getText());
        nueva.setZona(txtZona.getText());

        Grafo grafoActual = grafoBaseDatos.cargarGrafo();
        grafoActual.anadirParada(nueva);
        grafoBaseDatos.sincronizar(grafoActual);

        Parada paradaEnGrafo = grafoActual.getParadas().get(nueva.getId());

        if (helloController != null) {
            helloController.refrescarVista();
        }

        if (grafoActual.getParadas().size() > 1) {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("/visual/crear_rutas.fxml")
            );
            Scene scene = new Scene(loader.load());

            controlRutas controller = loader.getController();
            controller.setParadaOrigen(paradaEnGrafo);

            controller.setHelloController(this.helloController);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();

            if (!controller.isRutaCreada()) {
                grafoActual.eliminarParada(nueva.getId());
                grafoBaseDatos.sincronizar(grafoActual);
            }
        }

        Stage ventanaActual = (Stage) txtCodigo.getScene().getWindow();
        ventanaActual.close();
    }

    @FXML
    public void initialize() {
        Grafo grafoActual = grafoBaseDatos.cargarGrafo();
        txtCodigo.setText("PAR-" + (grafoActual.getParadas().size() + 1));

        if (cbxDireccion != null) {
            cbxDireccion.getItems().setAll(grafoActual.getParadas().values());
        }
    }

    public void setHelloController(HelloController controller) {
        this.helloController = controller;
    }
}