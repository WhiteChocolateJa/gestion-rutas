package edu.pucmm.rutas.gestionrutas.control;

import edu.pucmm.rutas.gestionrutas.database.ParadaDAO;
import edu.pucmm.rutas.gestionrutas.database.RutaDAO;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class controlEditarRutas {

    @FXML
    private ComboBox<Ruta> cbxRutas;

    @FXML
    private TextField txtCodigoRuta;

    @FXML
    private ComboBox<Parada> cbxOrigen;

    @FXML
    private ComboBox<Parada> cbxDestino;

    @FXML
    private Spinner<Double> spnTiempo;

    @FXML
    private Spinner<Double> spnDistancia;

    @FXML
    private Spinner<Double> spnCosto;

    @FXML
    private Spinner<Integer> spnTransbordo;

    private final RutaDAO rutaDAO = new RutaDAO();
    private final ParadaDAO paradaDAO = new ParadaDAO();

    @FXML
    public void initialize() {
        configurarSpinners();
        cargarParadas();
        cargarRutas();
    }

    private void configurarSpinners() {
        spnTiempo.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100000, 0, 1));
        spnDistancia.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100000, 0, 0.5));
        spnCosto.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100000, 0, 1));
        spnTransbordo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0, 1));

        spnTiempo.setEditable(true);
        spnDistancia.setEditable(true);
        spnCosto.setEditable(true);
        spnTransbordo.setEditable(true);
    }

    private void cargarParadas() {
        List<Parada> paradas = paradaDAO.obtenerTodas();
        cbxOrigen.setItems(FXCollections.observableArrayList(paradas));
        cbxDestino.setItems(FXCollections.observableArrayList(paradas));
    }

    private void cargarRutas() {
        List<Ruta> rutas = rutaDAO.obtenerTodas();
        cbxRutas.setItems(FXCollections.observableArrayList(rutas));
    }

    @FXML
    private void cargarDatosRuta() {
        Ruta rutaSeleccionada = cbxRutas.getValue();

        if (rutaSeleccionada == null) {
            limpiarCampos();
            return;
        }

        txtCodigoRuta.setText(rutaSeleccionada.getId());
        cbxOrigen.setValue(rutaSeleccionada.getParadaOrigen());
        cbxDestino.setValue(rutaSeleccionada.getParadaDestino());
        spnTiempo.getValueFactory().setValue(rutaSeleccionada.getTiempoViaje());
        spnDistancia.getValueFactory().setValue(rutaSeleccionada.getDistancia());
        spnCosto.getValueFactory().setValue(rutaSeleccionada.getCosto());
        spnTransbordo.getValueFactory().setValue(rutaSeleccionada.getTransbordos());
    }

    @FXML
    private void modificarRuta() {
        Ruta rutaSeleccionada = cbxRutas.getValue();

        if (rutaSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una ruta.");
            return;
        }

        Parada origen = cbxOrigen.getValue();
        Parada destino = cbxDestino.getValue();

        if (origen == null || destino == null) {
            mostrarAdvertencia("Debe seleccionar la parada de origen y destino.");
            return;
        }

        if (origen.equals(destino)) {
            mostrarAdvertencia("El origen y el destino no pueden ser la misma parada.");
            return;
        }

        double tiempo = spnTiempo.getValue();
        double distancia = spnDistancia.getValue();
        double costo = spnCosto.getValue();
        int transbordos = spnTransbordo.getValue();

        Ruta rutaActualizada = new Ruta(rutaSeleccionada.getId(), origen, destino, tiempo, distancia, costo, transbordos);

        rutaDAO.actualizar(rutaActualizada);

        cargarRutas();
        seleccionarRutaPorId(rutaSeleccionada.getId());
        cargarDatosRuta();

        mostrarInformacion("Ruta modificada correctamente.");
    }

    @FXML
    private void eliminarRuta() {
        Ruta rutaSeleccionada = cbxRutas.getValue();

        if (rutaSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una ruta.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Eliminación");
        alerta.setHeaderText("¿Eliminar la ruta " + rutaSeleccionada.getId() + "?");
        alerta.setContentText("Origen: " + rutaSeleccionada.getParadaOrigen().getNombre() + "\n" + "Destino: " + rutaSeleccionada.getParadaDestino().getNombre() + "\n\n" + "Esto podría hacer que el mapa no sea conexo.\n" + "¿Deseas continuar?");
        alerta.showAndWait();

        if (alerta.getResult() == javafx.scene.control.ButtonType.OK) {
            String idRuta = rutaSeleccionada.getId();
            rutaDAO.eliminar(idRuta);
            cargarRutas();
            limpiarCampos();
            mostrarInformacion("Ruta eliminada correctamente.");
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) cbxRutas.getScene().getWindow();
        stage.close();
    }

    private void limpiarCampos() {
        cbxRutas.getSelectionModel().clearSelection();
        txtCodigoRuta.clear();
        cbxOrigen.getSelectionModel().clearSelection();
        cbxDestino.getSelectionModel().clearSelection();
        spnTiempo.getValueFactory().setValue(0.0);
        spnDistancia.getValueFactory().setValue(0.0);
        spnCosto.getValueFactory().setValue(0.0);
        spnTransbordo.getValueFactory().setValue(0);
    }

    private void seleccionarRutaPorId(String id) {
        for (Ruta ruta : cbxRutas.getItems()) {
            if (ruta.getId().equals(id)) {
                cbxRutas.setValue(ruta);
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