package edu.pucmm.rutas.gestionrutas.control;

import edu.pucmm.rutas.gestionrutas.database.ParadaDAO;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class controlEditarParadas {

    @FXML
    private ComboBox<Parada> cbxParadas;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtZona;

    private final ParadaDAO paradaDAO = new ParadaDAO();

    @FXML
    public void initialize() {
        cargarParadas();
    }

    private void cargarParadas() {
        List<Parada> paradas = paradaDAO.obtenerTodas();
        cbxParadas.setItems(FXCollections.observableArrayList(paradas));
    }

    @FXML
    private void cargarDatosParada() {
        Parada paradaSeleccionada = cbxParadas.getValue();

        if (paradaSeleccionada == null) {
            limpiarCampos();
            return;
        }

        txtCodigo.setText(paradaSeleccionada.getId());
        txtNombre.setText(paradaSeleccionada.getNombre() != null ? paradaSeleccionada.getNombre() : "");
        txtDescripcion.setText(paradaSeleccionada.getDescripcion() != null ? paradaSeleccionada.getDescripcion() : "");
        txtZona.setText(paradaSeleccionada.getZona() != null ? paradaSeleccionada.getZona() : "");
    }

    @FXML
    private void modificarParada() {
        Parada paradaSeleccionada = cbxParadas.getValue();

        if (paradaSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una parada.");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String zona = txtZona.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAdvertencia("El nombre no puede estar vacío.");
            return;
        }

        paradaSeleccionada.setNombre(nombre);
        paradaSeleccionada.setDescripcion(descripcion);
        paradaSeleccionada.setZona(zona);

        paradaDAO.actualizar(paradaSeleccionada);

        cargarParadas();
        seleccionarParadaPorId(paradaSeleccionada.getId());
        cargarDatosParada();

        mostrarInformacion("Parada modificada correctamente.");
    }

    @FXML
    private void eliminarParada() {
        Parada paradaSeleccionada = cbxParadas.getValue();

        if (paradaSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una parada.");
            return;
        }

        String id = paradaSeleccionada.getId();

        paradaDAO.eliminar(id);

        cargarParadas();
        limpiarCampos();

        mostrarInformacion("Parada eliminada correctamente.");
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) cbxParadas.getScene().getWindow();
        stage.close();
    }

    private void limpiarCampos() {
        cbxParadas.getSelectionModel().clearSelection();
        txtCodigo.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtZona.clear();
    }

    private void seleccionarParadaPorId(String id) {
        for (Parada parada : cbxParadas.getItems()) {
            if (parada.getId().equals(id)) {
                cbxParadas.setValue(parada);
                break;
            }
        }
    }

    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}