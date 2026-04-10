package edu.pucmm.rutas.gestionrutas.control;

import edu.pucmm.rutas.gestionrutas.database.ParadaDAO;
import edu.pucmm.rutas.gestionrutas.modelo.Parada;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;


/*
   Clase: controlEditarParadas
   Esta clase controla la ventana de edición de paradas dentro de la interfaz.
   Permite seleccionar una parada existente, visualizar sus datos, modificarlos o eliminarla de la base de datos.
*/
public class controlEditarParadas {

    @FXML
    private ComboBox<Parada> cbxParadas;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtZona;

    private final ParadaDAO paradaDAO = new ParadaDAO();

    /*
   Método: initialize
   Este método se ejecuta automáticamente al cargar la ventana.
   Se encarga de cargar todas las paradas disponibles desde la base de datos y mostrarlas en el ComboBox.
   Retorno:
      - No devuelve ningún valor.
*/
    @FXML
    public void initialize() {
        cargarParadas();
    }

    /*
   Método: cargarParadas
   Este método obtiene todas las paradas desde la base de datos utilizando ParadaDAO
   y las coloca en el ComboBox para que el usuario pueda seleccionarlas.
   Retorno:
      - No devuelve ningún valor.
*/
    private void cargarParadas() {
        List<Parada> paradas = paradaDAO.obtenerTodas();
        cbxParadas.setItems(FXCollections.observableArrayList(paradas));
    }


    /*
   Método: cargarDatosParada
   Este método se encarga de mostrar en los campos de texto la información de la parada seleccionada.
   Si no hay ninguna parada seleccionada, limpia los campos.
   Retorno:
      - No devuelve ningún valor.
*/
    @FXML
    private void cargarDatosParada() {
        Parada paradaSeleccionada = cbxParadas.getValue();

        if (paradaSeleccionada == null) {
            limpiarCampos();
            return;
        }

        txtCodigo.setText(paradaSeleccionada.getId());
        txtNombre.setText(paradaSeleccionada.getNombre() != null ? paradaSeleccionada.getNombre() : "");
        txtDescripcion.setText(paradaSeleccionada.getDescripcion() != null ? paradaSeleccionada.getDescripcion() : "");
        txtZona.setText(paradaSeleccionada.getZona() != null ? paradaSeleccionada.getZona() : "");
    }


    /*
   Método: modificarParada
   Este método permite actualizar los datos de una parada seleccionada.
   Primero valida que exista una parada seleccionada y que el nombre no esté vacío.
   Luego actualiza los datos del objeto Parada y los guarda en la base de datos mediante ParadaDAO.
   Después recarga la lista de paradas y muestra un mensaje de confirmación.
   Retorno:
      - No devuelve ningún valor.
*/
    @FXML
    private void modificarParada() {
        Parada paradaSeleccionada = cbxParadas.getValue();

        if (paradaSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una parada.");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String zona = txtZona.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAdvertencia("El nombre no puede estar vacío.");
            return;
        }

        paradaSeleccionada.setNombre(nombre);
        paradaSeleccionada.setDescripcion(descripcion);
        paradaSeleccionada.setZona(zona);

        paradaDAO.actualizar(paradaSeleccionada);

        cargarParadas();
        seleccionarParadaPorId(paradaSeleccionada.getId());
        cargarDatosParada();

        mostrarInformacion("Parada modificada correctamente.");
    }


    /*
   Método: eliminarParada
   Este método elimina una parada seleccionada de la base de datos.
   Primero valida que haya una parada seleccionada.
   Luego muestra una alerta de confirmación al usuario.
   Si el usuario acepta, elimina la parada y actualiza la lista.
   Retorno:
      - No devuelve ningún valor.
*/
    @FXML
    private void eliminarParada() {
        Parada paradaSeleccionada = cbxParadas.getValue();

        if (paradaSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una parada.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Eliminación");
        alerta.setHeaderText("Eliminar la parada: " + paradaSeleccionada.getNombre());
        alerta.setContentText("Si eliminas esta parada, el mapa podría  dejar de ser conexo.\n\n¿Deseas continuar?");

        alerta.showAndWait();

        if (alerta.getResult() == ButtonType.OK) {
            String id = paradaSeleccionada.getId();
            paradaDAO.eliminar(id);

            cargarParadas();
            limpiarCampos();
            mostrarInformacion("Parada eliminada correctamente.");

        }
    }


    /*
   Método: cerrarVentana
   Este método cierra la ventana actual de edición.
   Retorno:
      - No devuelve ningún valor.
*/
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) cbxParadas.getScene().getWindow();
        stage.close();
    }


    /*
   Método: limpiarCampos
   Este método limpia todos los campos de la interfaz y elimina la selección actual.
   Se utiliza cuando no hay parada seleccionada o después de eliminar una.
   Retorno:
      - No devuelve ningún valor.
*/
    private void limpiarCampos() {
        cbxParadas.getSelectionModel().clearSelection();
        txtCodigo.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtZona.clear();
    }


    /*
   Método: seleccionarParadaPorId
   Este método busca una parada en el ComboBox por su id y la selecciona automáticamente.
   Se utiliza después de modificar una parada para mantenerla seleccionada.
   Retorno:
      - No devuelve ningún valor.
*/
    private void seleccionarParadaPorId(String id) {
        for (Parada parada : cbxParadas.getItems()) {
            if (parada.getId().equals(id)) {
                cbxParadas.setValue(parada);
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