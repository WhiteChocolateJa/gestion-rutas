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


/*
   Clase: controlEditarRutas
   Esta clase controla la ventana de edición de rutas dentro de la interfaz.
   Permite seleccionar una ruta existente, visualizar sus datos, modificarlos o eliminarla de la base de datos.
*/
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


    /*
   Método: initialize
   Este método se ejecuta automáticamente al cargar la ventana.
   Se encarga de configurar los Spinner y de cargar tanto las paradas como las rutas disponibles en la interfaz.
   Retorno:
      - No devuelve ningún valor.
*/
    @FXML
    public void initialize() {
        configurarSpinners();
        cargarParadas();
        cargarRutas();
    }


    /*
   Método: configurarSpinners
   Este método inicializa los Spinner con los rangos permitidos para tiempo, distancia, costo y transbordos.
   También permite que el usuario pueda escribir manualmente los valores en esos campos.
   Retorno:
      - No devuelve ningún valor.
*/
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


    /*
   Método: cargarParadas
   Este método obtiene todas las paradas desde la base de datos utilizando ParadaDAO
   y las coloca en los ComboBox de origen y destino.
   Retorno:
      - No devuelve ningún valor.
*/
    private void cargarParadas() {
        List<Parada> paradas = paradaDAO.obtenerTodas();
        cbxOrigen.setItems(FXCollections.observableArrayList(paradas));
        cbxDestino.setItems(FXCollections.observableArrayList(paradas));
    }


    /*
   Método: cargarRutas
   Este método obtiene todas las rutas almacenadas en la base de datos utilizando RutaDAO
   y las coloca en el ComboBox para que el usuario pueda seleccionarlas.
   Retorno:
      - No devuelve ningún valor.
*/
    private void cargarRutas() {
        List<Ruta> rutas = rutaDAO.obtenerTodas();
        cbxRutas.setItems(FXCollections.observableArrayList(rutas));
    }


    /*
   Método: cargarDatosRuta
   Este método muestra en la interfaz la información de la ruta seleccionada.
   Carga el código, la parada de origen, la parada de destino y los valores de tiempo, distancia, costo y transbordos.
   Si no hay una ruta seleccionada, limpia los campos.
   Retorno:
      - No devuelve ningún valor.
*/
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

    /*
   Método: modificarRuta
   Este método permite actualizar una ruta seleccionada.
   Primero valida que exista una ruta seleccionada, que el origen y el destino estén definidos,
   y que no sean la misma parada.
   Luego obtiene los nuevos valores ingresados, crea una nueva instancia de Ruta con esos datos
   y la actualiza en la base de datos mediante RutaDAO.
   Después recarga la lista de rutas, vuelve a seleccionar la ruta modificada y muestra un mensaje de confirmación.
   Retorno:
      - No devuelve ningún valor.
*/
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

    /*
   Método: eliminarRuta
   Este método elimina una ruta seleccionada de la base de datos.
   Primero valida que exista una ruta seleccionada.
   Luego muestra una alerta de confirmación al usuario.
   Si el usuario acepta, elimina la ruta, actualiza la lista y limpia los campos.
   Retorno:
      - No devuelve ningún valor.
*/
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


    /*
   Método: cerrarVentana
   Este método cierra la ventana actual de edición de rutas.
   Retorno:
      - No devuelve ningún valor.
*/
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) cbxRutas.getScene().getWindow();
        stage.close();
    }

    /*
   Método: limpiarCampos
   Este método limpia todos los campos de la interfaz y elimina la selección actual.
   También reinicia los Spinner a sus valores por defecto.
   Retorno:
      - No devuelve ningún valor.
*/
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

    /*
   Método: seleccionarRutaPorId
   Este método busca una ruta en el ComboBox utilizando su id y la selecciona automáticamente.
   Se utiliza después de modificar una ruta para mantenerla activa en la interfaz.
   Retorno:
      - No devuelve ningún valor.
*/
    private void seleccionarRutaPorId(String id) {
        for (Ruta ruta : cbxRutas.getItems()) {
            if (ruta.getId().equals(id)) {
                cbxRutas.setValue(ruta);
                break;
            }
        }
    }

    /*
   Método: mostrarInformacion
   Este método muestra una alerta informativa al usuario con un mensaje específico.
   Retorno:
      - No devuelve ningún valor.
*/
    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /*
   Método: mostrarAdvertencia
   Este método muestra una alerta de advertencia al usuario cuando ocurre un error o falta información.
   Retorno:
      - No devuelve ningún valor.
*/
    private void mostrarAdvertencia(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}