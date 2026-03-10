package edu.pucmm.rutas.gestionrutas;

import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;
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

    private GrafoRepository grafoBaseDatos= new GrafoRepository();
    private Grafo elGrafo = grafoBaseDatos.cargarGrafo();

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
    public void aceptarLaParada(){
        String codigo = txtCodigo.getText();
        String nombre = txtNombre.getText();
        Parada parada = new Parada(codigo, nombre, 0.0, 0.0);
        elGrafo.anadirParada(parada);
        Stage stage = (Stage) txtCodigo.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cargarParadas(){
        cbxDireccion.getItems().addAll(
                elGrafo.getParadas().values()
        );
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

    public void cargarParadasOrigen(){
        cbxOrigen.getItems().addAll(
                elGrafo.getParadas().values()
        );
    }

    public void cargarParadasDestino(){
        cbxDestino.getItems().addAll(
                elGrafo.getParadas().values()
        );
    }

    public void aceptarRuta(){
        Parada origen =  cbxOrigen.getValue();
        Parada destino = cbxDestino.getValue();
        String codigo = txtCodigorRuta.getText();
        Double costo = spnCosto.getValue();
        Double distancia = spnDistancia.getValue();
        Integer trasbordo = spnTrasbordo.getValue();
        Double tiempo = spnTiempo.getValue();
        Ruta laruta = new Ruta(codigo, origen, destino, tiempo, costo, distancia, trasbordo);
        elGrafo.anadirRuta(laruta);
        Stage stage = (Stage) txtCodigorRuta.getScene().getWindow();
        stage.close();
    }

    //VISUALIZAR LAS PARADAS
    @FXML
    private TableColumn COLUMID;
    @FXML
    private TableColumn COLUMNOM;
    @FXML
    private TableView TABLA;

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

    public void cargarDatosTabla() {
        COLUMID.setCellValueFactory(new PropertyValueFactory<>("id"));
        COLUMNOM.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TABLA.getItems().addAll(elGrafo.getParadas().values());
    }

    //VISUALIZAR RUTAS

    @FXML
    private TableView RUTAS;

    @FXML
    private TableColumn COLIDR;

    @FXML
    private TableColumn COLOIRG;

    @FXML
    private TableColumn COLDEST;

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

    public void cargarDatosTablaRutas() {
        COLIDR.setCellValueFactory(new PropertyValueFactory<>("id"));
        COLOIRG.setCellValueFactory(new PropertyValueFactory<>("paradaOrigen"));
        COLDEST.setCellValueFactory(new PropertyValueFactory<>("paradaDestino"));
        RUTAS.getItems().addAll(elGrafo.getRutas().values());
    }
}