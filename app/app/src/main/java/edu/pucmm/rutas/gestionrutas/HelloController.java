package edu.pucmm.rutas.gestionrutas;


import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    private static final GrafoRepository grafoBaseDatos= new GrafoRepository();
    private static Grafo elGrafo = grafoBaseDatos.cargarGrafo();

    //CREAR PARADAS
    @FXML
    private Button cancelar;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtZona;

    @FXML
    private ComboBox <Parada> cbxDireccion;


    @FXML
    public void abrirCreacionParado() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("crear_paradas.fxml")
        );

        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void cerrarVentana(ActionEvent event) throws IOException {
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


    //CREAR RUTAS

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
    public void abrirCreacionRuta() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("crear_rutas.fxml")
        );

        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
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

    //VISUALIZAR LAS PARADAS
    @FXML
    private TableColumn<Parada, String>COLUMID;
    @FXML
    private TableColumn<Parada, String> COLUMNOM;
    @FXML
    private TableView<Parada> TABLA;

    @FXML
    public void abrirVerParadas() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("visualizar_paradasTEMPORAL.fxml")
        );

        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }

    //VISUALIZAR RUTAS

    @FXML
    private TableView<Ruta> RUTAS;

    @FXML
    private TableColumn<Ruta, String> COLIDR;

    @FXML
    private TableColumn<Ruta, Parada> COLOIRG;

    @FXML
    private TableColumn<Ruta, Parada> COLDEST;

    @FXML
    public void abrirVerRutas() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("visualizar_rutasTEMPORAL.fxml")
        );

        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void initialize() {

        if (cbxDireccion != null) {
            cbxDireccion.getItems().setAll(elGrafo.getParadas().values());
        }

        if (cbxOrigen != null) {
            cbxOrigen.getItems().setAll(elGrafo.getParadas().values());
        }

        if (cbxDestino != null) {
            cbxDestino.getItems().setAll(elGrafo.getParadas().values());
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

        if (TABLA != null) {
            COLUMID.setCellValueFactory(new PropertyValueFactory<>("id"));
            COLUMNOM.setCellValueFactory(new PropertyValueFactory<>("nombre"));

            ObservableList<Parada> listaParadas = FXCollections.observableArrayList(elGrafo.getParadas().values());
            TABLA.setItems(listaParadas);
        }

        if (RUTAS != null) {
            COLIDR.setCellValueFactory(new PropertyValueFactory<>("id"));
            COLOIRG.setCellValueFactory(new PropertyValueFactory<>("paradaOrigen"));
            COLDEST.setCellValueFactory(new PropertyValueFactory<>("paradaDestino"));

            ObservableList<Ruta> listaRutas = FXCollections.observableArrayList(elGrafo.getRutas().values());
            RUTAS.setItems(listaRutas);
        }
    }
}