package edu.pucmm.rutas.gestionrutas;

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

    private static final GrafoRepository grafoBaseDatos= new GrafoRepository();
    private static Grafo elGrafo = grafoBaseDatos.cargarGrafo();

    @FXML
    TextField txtCodigorRuta;

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
        String codigo = txtCodigorRuta.getText();
        Double tiempo = spnTiempo.getValue();
        Double distancia = spnDistancia.getValue();
        Double costo = spnCosto.getValue();
        Integer trasbordo = spnTrasbordo.getValue();

        System.out.println("Entró a aceptarRuta");

        Ruta laRuta = new Ruta(codigo, origen, destino, tiempo, distancia, costo, trasbordo);

        elGrafo.anadirRuta(laRuta);
        grafoBaseDatos.sincronizar(elGrafo);
        elGrafo = grafoBaseDatos.cargarGrafo();

        Stage stage = (Stage) txtCodigorRuta.getScene().getWindow();
        stage.close();

    }

    @FXML
    public void initialize() {

        txtCodigorRuta.setText("RUT-"+ (grafoBaseDatos.cargarGrafo().getRutas().size()+1));

        // 1. Llenar los ComboBox con las paradas existentes en el grafo
        if (cbxOrigen != null) {
            cbxOrigen.getItems().setAll(elGrafo.getParadas().values());
        }

        if (cbxDestino != null) {
            cbxDestino.getItems().setAll(elGrafo.getParadas().values());
        }

        // 2. Configurar los límites y valores por defecto de los Spinners
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
