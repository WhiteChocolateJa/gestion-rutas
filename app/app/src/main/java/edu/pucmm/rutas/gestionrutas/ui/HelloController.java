package edu.pucmm.rutas.gestionrutas.ui;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import edu.pucmm.rutas.gestionrutas.algoritmos.CriterioOptimizacion;
import edu.pucmm.rutas.gestionrutas.algoritmos.Dijkstra;
import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    private static final GrafoRepository grafoBaseDatos = new GrafoRepository();
    private static Grafo elGrafo = grafoBaseDatos.cargarGrafo();

    // CREAR PARADAS
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
    private ComboBox<Parada> cbxDireccion;

    @FXML
    private TextArea txtResultadoRuta;

    @FXML
    public void abrirCreacionParado() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("/visual/crear_paradas.fxml")
        );

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.sizeToScene();
        stage.setScene(scene);
        stage.showAndWait();
        refrescarVista();
    }

    // CREAR RUTAS
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
                HelloApplication.class.getResource("/visual/crear_rutas.fxml")
        );

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.sizeToScene();
        stage.setScene(scene);
        stage.showAndWait();
        refrescarVista();
    }

    @FXML
    private AnchorPane panelDerecho;

    @FXML
    private ComboBox<Parada> cbxOrigen2;

    @FXML
    private ComboBox<Parada> cbxDestino2;

    @FXML
    private ComboBox<CriterioOptimizacion> cbxCriterio;

    @FXML
    public void initialize() {
        refrescarVista();
    }

    private void refrescarVista() {
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

            Platform.runLater(() -> panelVisual.init());
        }

        if (txtResultadoRuta != null) {
            txtResultadoRuta.setText("Seleccione origen, destino y criterio para calcular una ruta.");
        }
    }

    private SmartGraphPanel<Parada, Ruta> panelVisual;

    public List<Parada> encontrarMejor(Parada origen, Parada destino, CriterioOptimizacion criterio) {
        Grafo grafoActual = grafoBaseDatos.cargarGrafo();
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
            panelVisual.getStylableVertex(p).addStyleClass("parada-optima");
        }

        for (int i = 0; i < camino.size() - 1; i++) {
            Parada p1 = camino.get(i);
            Parada p2 = camino.get(i + 1);

            Ruta tramo = elGrafo.obtenerRutaDirecta(p1, p2);
            if (tramo != null) {
                panelVisual.getStylableEdge(tramo).addStyleClass("ruta-optima");
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

                txtResultadoRuta.setText(
                        "Origen: " + origen.getNombre() + "\n" +
                                "Destino: " + destino.getNombre() + "\n" +
                                "Criterio: " + criterio + "\n\n" +
                                "Costo total: " + total + "\n\n" +
                                "Ruta:\n" + rutaTexto
                );
            } else {
                txtResultadoRuta.setText("No se encontró ruta.");
            }
        } else {
            txtResultadoRuta.setText("Seleccione origen, destino y criterio.");
        }
    }

    // --- MÉTODOS PARA MANTENER CONEXIONES ENTRE PARADAS ---
    public void origenDeLaConexion(Parada parada1, Parada parada2, Ruta propuesta) {
        Grafo elgrafo2 = grafoBaseDatos.cargarGrafo();

        if (elgrafo2.getParadas().size() == 1) {
            Ruta nueva = new Ruta(
                    "RUT-" + (elgrafo2.getRutas().size() + 1),
                    parada1,
                    parada2,
                    propuesta.getTiempoViaje(),
                    propuesta.getDistancia(),
                    propuesta.getCosto(),
                    propuesta.getTransbordos()
            );
            parada1.agregarRuta(nueva);
            elgrafo2.anadirRuta(nueva);
            grafoBaseDatos.sincronizar(elgrafo2);
        } else {
            mantenerConexion(parada1, parada2, elgrafo2, propuesta);
        }
    }

    public void mantenerConexion(Parada parada1, Parada parada2, Grafo elgrafito, Ruta rutica) {
        parada1.agregarRuta(rutica);
        elgrafito.anadirRuta(rutica);

        List<Ruta> rutasNuevas = new ArrayList<>();

        for (Ruta r : elgrafito.getRutas().values()) {
            if (r.getParadaOrigen().equals(parada2)) {
                Parada destino = r.getParadaDestino();
                if (!destino.equals(parada1) && !existeRuta(elgrafito, parada1, destino)) {
                    double TIEMPO = Math.sqrt(rutica.getTiempoViaje()* rutica.getTiempoViaje() + r.getTiempoViaje()*r.getTiempoViaje());
                    double DISTANCIA = Math.sqrt(rutica.getDistancia()* rutica.getDistancia() + r.getDistancia()*r.getDistancia());
                    double COSTO = Math.sqrt(rutica.getCosto()* rutica.getCosto() + r.getCosto()*r.getCosto());
                    int TRANSBORDO = (int) Math.round(Math.sqrt(rutica.getTransbordos()* rutica.getTransbordos() + r.getTransbordos()*r.getTransbordos()));

                    Ruta nueva = new Ruta(
                            "RUT-" + (elgrafito.getRutas().size() + rutasNuevas.size() + 1),
                            parada1,
                            destino,
                            TIEMPO,
                            DISTANCIA,
                            COSTO,
                            TRANSBORDO
                    );
                    parada1.agregarRuta(nueva);
                    rutasNuevas.add(nueva);
                }
            }
        }

        for (Ruta r : elgrafito.getRutas().values()) {
            Parada origen = r.getParadaOrigen();
            if (r.getParadaDestino().equals(parada2) && !origen.equals(parada1)) {
                double TIEMPO = Math.sqrt(rutica.getTiempoViaje()* rutica.getTiempoViaje() + r.getTiempoViaje()*r.getTiempoViaje());
                double DISTANCIA = Math.sqrt(rutica.getDistancia()* rutica.getDistancia() + r.getDistancia()*r.getDistancia());
                double COSTO = Math.sqrt(rutica.getCosto()* rutica.getCosto() + r.getCosto()*r.getCosto());
                int TRANSBORDO = (int) Math.round(Math.sqrt(rutica.getTransbordos()* rutica.getTransbordos() + r.getTransbordos()*r.getTransbordos()));

                Ruta nueva = new Ruta(
                        "RUT-" + (elgrafito.getRutas().size() + rutasNuevas.size() + 1),
                        origen,
                        parada1,
                        TIEMPO,
                        DISTANCIA,
                        COSTO,
                        TRANSBORDO
                );
                origen.agregarRuta(nueva);
                rutasNuevas.add(nueva);
                break;
            }
        }

        for (Ruta nuevaRuta : rutasNuevas) {
            elgrafito.anadirRuta(nuevaRuta);
        }
        grafoBaseDatos.sincronizar(elgrafito);
    }

    public boolean existeRuta(Grafo grafo, Parada origen, Parada destino) {
        for (Ruta r : grafo.getRutas().values()) {
            if (r.getParadaOrigen().equals(origen) && r.getParadaDestino().equals(destino)) {
                return true;
            }
        }
        return false;
    }

}