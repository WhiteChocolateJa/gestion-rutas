package edu.pucmm.rutas.gestionrutas;

import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class controlParadas {

    private static final GrafoRepository grafoBaseDatos = new GrafoRepository();
    private static Grafo elGrafo = grafoBaseDatos.cargarGrafo();

    @FXML private Button cancelar;
    @FXML
    TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtZona;
    @FXML private ComboBox<Parada> cbxDireccion;

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
        txtCodigo.setText("PAR-"+(grafoBaseDatos.cargarGrafo().getParadas().size()+1));
        if (cbxDireccion != null) {
            cbxDireccion.getItems().setAll(elGrafo.getParadas().values());
        }
    }
}