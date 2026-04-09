package edu.pucmm.rutas.gestionrutas.ui;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import edu.pucmm.rutas.gestionrutas.algoritmos.*;
import edu.pucmm.rutas.gestionrutas.control.controlParadas;
import edu.pucmm.rutas.gestionrutas.control.controlRutas;
import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    private static final GrafoRepository grafoBaseDatos = new GrafoRepository();
    private static Grafo elGrafo = grafoBaseDatos.cargarGrafo();

    // --- VARIABLES FXML ---

    // CREAR PARADAS
    @FXML private Button cancelar;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtZona;
    @FXML private ComboBox<Parada> cbxDireccion;

    // RESULTADO Y PANEL PRINCIPAL
    @FXML private TextArea txtResultadoRuta;
    @FXML private AnchorPane panelDerecho;

    // CREAR RUTAS
    @FXML private TextField txtCodigorRuta;
    @FXML private ComboBox<Parada> cbxOrigen;
    @FXML private ComboBox<Parada> cbxDestino;
    @FXML private Spinner<Double> spnTiempo;
    @FXML private Spinner<Double> spnCosto;
    @FXML private Spinner<Double> spnDistancia;
    @FXML private Spinner<Integer> spnTrasbordo;

    // CALCULAR RUTA (Panel Izquierdo)
    @FXML private ComboBox<Parada> cbxOrigen2;
    @FXML private ComboBox<Parada> cbxDestino2;
    @FXML private ComboBox<CriterioOptimizacion> cbxCriterio;

    // OPCIONES AVANZADAS
    @FXML private ComboBox<String> combxOpcAv; // <- Especificamos el tipo aquí
    @FXML private CheckBox cbxOpcAv; // <- Sin el "= new CheckBox()"

    private SmartGraphPanel<Parada, Ruta> panelVisual;


    @FXML
    public void initialize() {
        if (combxOpcAv != null && cbxOpcAv != null) {
            combxOpcAv.getItems().setAll("Dijkstra", "BellmanFord", "FloydWarshall", "DFS", "BFS");
            combxOpcAv.setDisable(true);

            cbxOpcAv.setOnAction(event -> {
                if (cbxOpcAv.isSelected()) {
                    combxOpcAv.setDisable(false);
                } else {
                    combxOpcAv.setDisable(true);
                }
                buscarMostrarRutaOptima();
            });

            combxOpcAv.setOnAction(event -> {
                if (!combxOpcAv.isDisabled()) {
                    buscarMostrarRutaOptima();
                }
            });
        }

        if (cbxCriterio != null) {
            cbxCriterio.setOnAction(event -> buscarMostrarRutaOptima());
        }

        refrescarVista();
    }

    public void refrescarVista() {
        Grafo miGrafo = grafoBaseDatos.cargarGrafo();
        elGrafo = miGrafo;

        if (cbxCriterio != null) {
            cbxCriterio.getItems().setAll(CriterioOptimizacion.values());
            cbxCriterio.getSelectionModel().selectFirst();
        }

        if (cbxOrigen2 != null && cbxDestino2 != null) {
            cbxOrigen2.getItems().setAll(miGrafo.getParadas().values());
            cbxDestino2.getItems().setAll(miGrafo.getParadas().values());
        }

        Digraph<Parada, Ruta> grafoListo = TraductorLibreria.convertirGrafo(miGrafo);
        SmartPlacementStrategy estrategia = new SmartCircularSortedPlacementStrategy();
        panelVisual = new SmartGraphPanel<>(grafoListo, estrategia);

        if (panelDerecho != null) {
            panelDerecho.getChildren().clear();
            panelDerecho.getChildren().add(0, panelVisual);

            AnchorPane.setTopAnchor(panelVisual, 0.0);
            AnchorPane.setBottomAnchor(panelVisual, 0.0);
            AnchorPane.setLeftAnchor(panelVisual, 0.0);
            AnchorPane.setRightAnchor(panelVisual, 0.0);

            Platform.runLater(() -> {
                panelVisual.init();
                instalarTooltips();
            });
        }

        if (txtResultadoRuta != null) {
            txtResultadoRuta.setText("Seleccione origen, destino y criterio para calcular una ruta.");
        }
    }


    @FXML
    public void abrirCreacionParado() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("/visual/crear_paradas.fxml")
        );

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();

        controlParadas controller = loader.getController();
        controller.setHelloController(this);

        stage.sizeToScene();
        stage.setScene(scene);
        stage.showAndWait();
        refrescarVista();
    }

    @FXML
    public void abrirCreacionRuta() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("/visual/crear_rutas.fxml")
        );

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();

        controlRutas controller = loader.getController();
        controller.setHelloController(this);

        stage.sizeToScene();
        stage.setScene(scene);
        stage.showAndWait();
        refrescarVista();
    }

    public List<Parada> encontrarMejor(Parada origen, Parada destino, CriterioOptimizacion criterio) {
        Grafo grafoActual = grafoBaseDatos.cargarGrafo();
        if (cbxOpcAv.isSelected()){
            if (combxOpcAv.getValue().equals("Dijkstra")){
                Dijkstra algoritmoDijkstra = new Dijkstra();
                return algoritmoDijkstra.dijkstra(origen, destino, grafoActual, criterio);
            }

            else if (combxOpcAv.getValue().equals("BellmanFord")){
                BellmanFord algoritmoBellamn = new BellmanFord();
                return algoritmoBellamn.algoritmoBellmanFord(origen, destino, grafoActual, criterio);
            }
            else if (combxOpcAv.getValue().equals("FloydWarshall")) {
                FloydWarshall algoritmoFloyd = new FloydWarshall();
                return algoritmoFloyd.algoritmoFloydWarshall(origen, destino, grafoActual, criterio);
            }
            else if (combxOpcAv.getValue().equals("BFS")) {
                BFS algoritmoBFS = new BFS();
                return algoritmoBFS.buscarCamino(origen, destino, grafoActual);
            }
            else if (combxOpcAv.getValue().equals("DFS")) {
                DFS algoritmoDFS = new DFS();
                return algoritmoDFS.buscarCamino(origen, destino, grafoActual);
            }
            }
            Dijkstra algoritmoDijkstra = new Dijkstra();
            return algoritmoDijkstra.dijkstra(origen, destino, grafoActual, criterio);
    }

    public void resaltarRutaEnMapa(List<Parada> camino) {
        for (Parada p : elGrafo.getParadas().values()) {
            try {
                panelVisual.getStylableVertex(p).removeStyleClass("parada-optima");
            } catch (Exception e) {}
        }
        for (Ruta r : elGrafo.getRutas().values()) {
            try {
                panelVisual.getStylableEdge(r).removeStyleClass("ruta-optima");
            } catch (Exception e) {}
        }

        if (camino == null || camino.isEmpty()) return;

        for (Parada p : camino) {
            try {
                Parada pEnPantalla = elGrafo.getParadas().get(p.getId());
                if (pEnPantalla != null) {
                    panelVisual.getStylableVertex(pEnPantalla).addStyleClass("parada-optima");
                }
            } catch (Exception e) {}
        }

        for (int i = 0; i < camino.size() - 1; i++) {
            Parada p1 = camino.get(i);
            Parada p2 = camino.get(i + 1);

            Parada p1EnPantalla = elGrafo.getParadas().get(p1.getId());
            Parada p2EnPantalla = elGrafo.getParadas().get(p2.getId());

            if (p1EnPantalla != null && p2EnPantalla != null) {
                Ruta tramo = elGrafo.obtenerRutaDirecta(p1EnPantalla, p2EnPantalla);
                if (tramo != null) {
                    try {
                        panelVisual.getStylableEdge(tramo).addStyleClass("ruta-optima");
                    } catch (Exception e) {}
                }
            }
        }
    }

    @FXML
    public void abrirEditarParadas() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("/visual/editar_paradas.fxml")
        );

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.sizeToScene();
        stage.setScene(scene);
        stage.showAndWait();
        refrescarVista();
    }

    @FXML
    public void abrirEditarRutas() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("/visual/editar_rutas.fxml")
        );

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.sizeToScene();
        stage.setScene(scene);
        stage.showAndWait();
        refrescarVista();
    }

    private double calcularCostoTotal(List<Parada> camino, CriterioOptimizacion criterio) {
        double total = 0;
        for (int i = 0; i < camino.size() - 1; i++) {
            Parada p1 = camino.get(i);
            Parada p2 = camino.get(i + 1);
            Ruta r = elGrafo.obtenerRutaDirecta(p1, p2);
            if (r != null) total += r.getPeso(criterio);
        }
        return total;
    }

    @FXML
    public void buscarMostrarRutaOptima() {
        Parada origen = cbxOrigen2.getValue();
        Parada destino = cbxDestino2.getValue();
        CriterioOptimizacion criterio = cbxCriterio.getValue();

        if (origen != null && destino != null && criterio != null) {
            List<Parada> camino = encontrarMejor(origen, destino, criterio);

            if (camino != null && camino.size() > 1) {
                resaltarRutaEnMapa(camino);

                double total = calcularCostoTotal(camino, criterio);

                StringBuilder rutaTexto = new StringBuilder();
                for (int i = 0; i < camino.size(); i++) {
                    rutaTexto.append(camino.get(i).getNombre());
                    if (i < camino.size() - 1) rutaTexto.append(" → ");
                }

                txtResultadoRuta.setText("Origen: " + origen.getNombre() + "\n" + "Destino: " + destino.getNombre() + "\n" + "Criterio: " + criterio + "\n\n" + "Costo total: " + total + "\n\n" + "Ruta:\n" + rutaTexto);
            } else {
                resaltarRutaEnMapa(null);
                txtResultadoRuta.setText("No se encontró ruta.");
            }
        } else {
            resaltarRutaEnMapa(null);
            txtResultadoRuta.setText("Seleccione origen, destino y criterio.");
        }
    }

    public void origenDeLaConexion(Parada parada1, Parada parada2, Ruta propuesta) {
        Grafo grafoActual = grafoBaseDatos.cargarGrafo();

        Parada p1Real = grafoActual.getParadas().get(parada1.getId());
        Parada p2Real = grafoActual.getParadas().get(parada2.getId());

        if (p1Real == null || p2Real == null) return;

        p1Real.agregarRuta(propuesta);
        grafoActual.anadirRuta(propuesta);

        if (grafoActual.getParadas().size() == 2 && grafoActual.getRutas().size() == 1) {
            String idInversa = "RUT-" + (grafoActual.getRutas().size() + 1);Ruta rutaInversa = new Ruta(idInversa, p2Real, p1Real, propuesta.getTiempoViaje(), propuesta.getDistancia(), propuesta.getCosto(), propuesta.getTransbordos());p2Real.agregarRuta(rutaInversa);grafoActual.anadirRuta(rutaInversa);
        }

        if (grafoActual.getParadas().size() > 2) {
            List<Ruta> rutasNuevas = new ArrayList<>();

            for (Ruta r : grafoActual.getRutas().values()) {

                if (r.getParadaDestino().getId().equals(p2Real.getId()) && !r.getParadaOrigen().getId().equals(p1Real.getId())) {

                    double TIEMPO = propuesta.getTiempoViaje() + r.getTiempoViaje();
                    double DISTANCIA = propuesta.getDistancia() + r.getDistancia();
                    double COSTO = propuesta.getCosto() + r.getCosto();
                    int TRANSBORDO = propuesta.getTransbordos() + r.getTransbordos() + 1;

                    String nuevoId = "RUT-" + (grafoActual.getRutas().size() + rutasNuevas.size() + 1);

                    Ruta nuevaRuta = new Ruta(nuevoId, r.getParadaOrigen(), p1Real, TIEMPO, DISTANCIA, COSTO, TRANSBORDO);

                    r.getParadaOrigen().agregarRuta(nuevaRuta);
                    rutasNuevas.add(nuevaRuta);
                }
            }
            for (Ruta nuevaRuta : rutasNuevas) {
                grafoActual.anadirRuta(nuevaRuta);
            }
        }
        grafoBaseDatos.sincronizar(grafoActual);
    }

    private void instalarTooltips() {

        for (Parada p : elGrafo.getParadas().values()) {
            try {
                String info = "Código: " + p.getId() + "\n" +
                        "Nombre: " + p.getNombre() + "\n" +
                        "Zona: " + (p.getZona() != null ? p.getZona() : "N/A") + "\n" +
                        "Descripción: " + (p.getDescripcion() != null ? p.getDescripcion() : "N/A");

                Tooltip tooltip = new Tooltip(info);
                tooltip.setShowDelay(Duration.seconds(1));

                Tooltip.install((Node) panelVisual.getStylableVertex(p), tooltip);

            } catch (Exception e) {}
        }

        for (Ruta r : elGrafo.getRutas().values()) {
            try {
                String info = "Origen: " + r.getParadaOrigen().getNombre() + "\n" + "Destino: " + r.getParadaDestino().getNombre() + "\n" + "Tiempo: " + r.getTiempoViaje() + "\n" + "Distancia: " + r.getDistancia() + "\n" + "Costo: " + r.getCosto() + "\n" + "Transbordos: " + r.getTransbordos();
                Tooltip tooltip = new Tooltip(info);
                tooltip.setShowDelay(Duration.seconds(1));

                Tooltip.install((Node) panelVisual.getStylableEdge(r), tooltip);

            } catch (Exception e) {}
        }
    }
}