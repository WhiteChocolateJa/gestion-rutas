package edu.pucmm.rutas.gestionrutas.control;

import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    public void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void aceptarLaParada() {

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        //VALIDACIÓN
        if (codigo.isEmpty() || nombre.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debe completar los campos obligatorios: Código y Nombre.");
            alert.showAndWait();

            return;
        }

        //Crear parada
        Parada parada = new Parada(codigo, nombre, 0.0, 0.0);
        parada.setDescripcion(txtDescripcion.getText());
        parada.setZona(txtZona.getText());

        elGrafo.anadirParada(parada);
        grafoBaseDatos.sincronizar(elGrafo);
        elGrafo = grafoBaseDatos.cargarGrafo();

        //Cerrar ventana
        Stage stage = (Stage) txtCodigo.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        Grafo grafoActual = grafoBaseDatos.cargarGrafo();
        txtCodigo.setText("PAR-" + (grafoActual.getParadas().size() + 1));
    }
}