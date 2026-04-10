package edu.pucmm.rutas.gestionrutas.control;

import edu.pucmm.rutas.gestionrutas.database.GrafoRepository;
import edu.pucmm.rutas.gestionrutas.modelo.Grafo;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import edu.pucmm.rutas.gestionrutas.modelo.Ruta;
import edu.pucmm.rutas.gestionrutas.ui.HelloController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/*
   Clase: controlRutas
   Esta clase controla la ventana de creación de rutas dentro de la interfaz.
   Se encarga de capturar los datos introducidos por el usuario, validar la selección de origen y destino,
   crear la nueva ruta y enviarla al controlador principal para integrarla al grafo.
*/
public class controlRutas {

    private static final GrafoRepository grafoBaseDatos = new GrafoRepository();
    private static Grafo elGrafo = grafoBaseDatos.cargarGrafo();
    private boolean rutaCreada=false;

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
   Método: aceptarRuta
   Este método se encarga de validar los datos ingresados para crear una ruta.
   Primero obtiene la parada de origen y la de destino seleccionadas por el usuario.
   Luego valida que ambas existan y que no sean la misma parada.
   Después obtiene los valores de tiempo, distancia, costo y transbordo desde la interfaz,
   construye un objeto Ruta con esos datos y lo envía al controlador principal para integrarlo al grafo.
   También marca la variable rutaCreada como verdadera para indicar que la operación fue completada.
   Finalmente cierra la ventana actual.
   Retorno:
      - No devuelve ningún valor.
*/
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

        if (helloController!=null){
            this.helloController.origenDeLaConexion(origen, destino, laRuta);
        }

        rutaCreada = true;

        Stage stage = (Stage) txtCodigorRuta.getScene().getWindow();
        stage.close();
    }


    /*
   Método: initialize
   Este método se ejecuta automáticamente al cargar la ventana.
   Se encarga de generar el código automático de la nueva ruta, cargar las paradas disponibles en los ComboBox,
   e inicializar los Spinner con los rangos permitidos para tiempo, distancia, costo y transbordos.
   Además, si ya existe una parada origen predefinida, la coloca automáticamente en el ComboBox y la bloquea para que no pueda modificarse.
   Retorno:
      - No devuelve ningún valor.
*/
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

        if (paradaOrigen != null) {
            cbxOrigen.setValue(paradaOrigen);
            cbxOrigen.setDisable(true); // CUALQUIER COSA ESTO SE PONE EN COMENTARIO
        }
    }

    private Parada paradaOrigen;

    /*
   Método: setParadaOrigen
   Este método permite establecer una parada de origen de forma automática antes de mostrar la ventana.
   Se utiliza principalmente cuando la ruta se está creando inmediatamente después de crear una nueva parada.
   También fija esa parada en el ComboBox y la bloquea para que el usuario no la cambie.
   Retorno:
      - No devuelve ningún valor.
*/
    public void setParadaOrigen(Parada parada) {
        this.paradaOrigen = parada;
        if (cbxOrigen != null) {
            cbxOrigen.setValue(parada);
            cbxOrigen.setDisable(true); // CUALQUIER COSA ESTO SE PONE EN COMENTARIO
        }
    }

    /*
   Método: isRutaCreada
   Este método permite verificar si la ruta fue creada correctamente antes de cerrar la ventana.
   Se utiliza para saber si debe mantenerse o revertirse la creación de la parada asociada.
   Retorno:
      - Devuelve un valor booleano: true si la ruta fue creada, false en caso contrario.
*/
    public boolean isRutaCreada() {
        return rutaCreada;
    }

    private HelloController helloController;

    /*
   Método: setHelloController
   Este método permite guardar una referencia al controlador principal.
   Se utiliza para que esta ventana pueda comunicarse con HelloController y enviarle la nueva ruta creada.
   Retorno:
      - No devuelve ningún valor.
*/
    public void setHelloController(HelloController controller) {
        this.helloController = controller;
    }

}