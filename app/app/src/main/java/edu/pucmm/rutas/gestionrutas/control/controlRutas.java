package edu.pucmm.rutas.gestionrutas.control;

import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class controlRutas {

    private static final GrafoRepository grafoBaseDatos = new GrafoRepository();
    private static Grafo elGrafo = grafoBaseDatos.cargarGrafo();

    @FXML
    private TextField txtCodigorRuta;

    @FXML
    private ComboBox<Parada> cbxOrigen;

    @FXML
    private ComboBox<Parada> cbxDestino;

    @FXML
    private Spinner<Double> spnTiempo;

    @FXML
    private Spinner<Double> spnCosto;

    @FXML
    private Spinner<Double> spnDistancia;

    @FXML
    private Spinner<Integer> spnTrasbordo;

    @FXML
    public void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void aceptarRuta() {
        Parada origen = cbxOrigen.getValue();
        Parada destino = cbxDestino.getValue();

        if (origen == null || destino == null) {
            System.out.println("Debe seleccionar origen y destino.");
            return;
        }

        if (origen.equals(destino)) {
            System.out.println("El origen y el destino no pueden ser iguales.");
            return;
        }

        String codigo = txtCodigorRuta.getText();
        Double tiempo = spnTiempo.getValue();
        Double distancia = spnDistancia.getValue();
        Double costo = spnCosto.getValue();
        Integer trasbordo = spnTrasbordo.getValue();

        Ruta laRuta = new Ruta(codigo, origen, destino, tiempo, distancia, costo, trasbordo);

        elGrafo.anadirRuta(laRuta);
        grafoBaseDatos.sincronizar(elGrafo);
        elGrafo = grafoBaseDatos.cargarGrafo();

        Stage stage = (Stage) txtCodigorRuta.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        Grafo grafoActual = grafoBaseDatos.cargarGrafo();

        txtCodigorRuta.setText("RUT-" + (grafoActual.getRutas().size() + 1));

        if (cbxOrigen != null) {
            cbxOrigen.getItems().setAll(grafoActual.getParadas().values());
        }

        if (cbxDestino != null) {
            cbxDestino.getItems().setAll(grafoActual.getParadas().values());
        }

        if (spnTiempo != null) {
            spnTiempo.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10000, 0, 1));
        }

        if (spnDistancia != null) {
            spnDistancia.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100000, 0, 1));
        }

        if (spnCosto != null) {
            spnCosto.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100000, 0, 1));
        }

        if (spnTrasbordo != null) {
            spnTrasbordo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1));
        }
    }
}