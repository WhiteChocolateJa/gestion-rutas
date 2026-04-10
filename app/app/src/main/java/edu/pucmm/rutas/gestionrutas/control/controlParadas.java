package edu.pucmm.rutas.gestionrutas.control;

import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.ui.HelloApplication;
import edu.pucmm.rutas.gestionrutas.ui.HelloController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


/*
   Clase: controlParadas
   Esta clase controla la ventana de creación de paradas dentro de la interfaz.
   Se encarga de capturar los datos introducidos por el usuario, crear la nueva parada,
   guardarla en el grafo y, si es necesario, abrir la ventana para conectarla con una ruta.
*/
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
    private ComboBox<Parada> cbxDireccion;

    private HelloController helloController;



    /*
   Método: cerrarVentana
   Este método se encarga de cerrar la ventana actual cuando el usuario presiona el botón de cancelar o cerrar.
   Obtiene la ventana a partir del evento recibido y luego la cierra.
   Retorno:
      - No devuelve ningún valor.
*/
    @FXML
    public void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    /*
   Método: continuarACrearRuta
   Este método se encarga de validar los datos ingresados para la nueva parada y crearla dentro del sistema.
   Primero verifica que los campos obligatorios no estén vacíos.
   Luego crea el objeto Parada, le asigna la descripción y la zona, y la agrega al grafo actual.
   Después sincroniza el grafo con la base de datos.
   Si existe una referencia al controlador principal, actualiza la vista.
   Si ya hay más de una parada en el sistema, abre la ventana de creación de rutas para obligar a conectar la nueva parada con otra existente.
   Si el usuario cancela la creación de la ruta, elimina la parada recién creada y vuelve a sincronizar el grafo para no dejar nodos sueltos.
   Finalmente cierra la ventana actual.
   Retorno:
      - No devuelve ningún valor.
*/
    @FXML
    public void continuarACrearRuta() throws IOException {

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debe completar los campos obligatorios: Código y Nombre.");
            alert.showAndWait();
            return;
        }

        Parada nueva = new Parada(codigo, nombre, 0.0, 0.0);
        nueva.setDescripcion(txtDescripcion.getText());
        nueva.setZona(txtZona.getText());

        Grafo grafoActual = grafoBaseDatos.cargarGrafo();
        grafoActual.anadirParada(nueva);
        grafoBaseDatos.sincronizar(grafoActual);

        Parada paradaEnGrafo = grafoActual.getParadas().get(nueva.getId());

        if (helloController != null) {
            helloController.refrescarVista();
        }

        if (grafoActual.getParadas().size() > 1) {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("/visual/crear_rutas.fxml")
            );
            Scene scene = new Scene(loader.load());

            controlRutas controller = loader.getController();
            controller.setParadaOrigen(paradaEnGrafo);

            controller.setHelloController(this.helloController);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();

            if (!controller.isRutaCreada()) {
                grafoActual.eliminarParada(nueva.getId());
                grafoBaseDatos.sincronizar(grafoActual);
            }
        }

        Stage ventanaActual = (Stage) txtCodigo.getScene().getWindow();
        ventanaActual.close();
    }


    /*
   Método: initialize
   Este método se ejecuta automáticamente al cargar la ventana.
   Se encarga de generar el código automático de la nueva parada y de cargar en el ComboBox las paradas existentes en el grafo.
   Retorno:
      - No devuelve ningún valor.
*/
    @FXML
    public void initialize() {
        Grafo grafoActual = grafoBaseDatos.cargarGrafo();
        txtCodigo.setText("PAR-" + (grafoActual.getParadas().size() + 1));

        if (cbxDireccion != null) {
            cbxDireccion.getItems().setAll(grafoActual.getParadas().values());
        }
    }

    /*
   Método: setHelloController
   Este método permite guardar una referencia al controlador principal.
   Se utiliza para que esta ventana pueda comunicarse con la vista principal y actualizarla cuando se agregue una nueva parada.
   Retorno:
      - No devuelve ningún valor.
*/
    public void setHelloController(HelloController controller) {
        this.helloController = controller;
    }
}