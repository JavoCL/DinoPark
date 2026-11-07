package cl.duoc.dinopark.controller;

// region IMPORTACIONES
// Importaciones de clases del modelo, DAO, colecciones y componentes de JavaFX
import cl.duoc.dinopark.GestorParque;
import cl.duoc.dinopark.dao.EspecieDao;
import cl.duoc.dinopark.dao.EspecieDaoJson;
import cl.duoc.dinopark.model.DatoInvalidoException;
import cl.duoc.dinopark.model.EspecieCarnivoro;
import cl.duoc.dinopark.model.EspecieHerbivoro;
import cl.duoc.dinopark.model.EspecieDinosaurio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
// endregion

/**
 * Controlador de la vista principal "Dino DEX".
 * Administra los formularios de registro de especies, la visualización en ListView
 * y la persistencia de datos mediante el patrón DAO y formato JSON.
 * Debe esta referenciado en la vista 'vista-principal.fxml'
 */
public class VistaPrincipalController implements Initializable {

    // ==========================================
    // BLOQUE 1: COMPONENTES FXML - FORMULARIO CARNÍVORO
    // ==========================================
    @FXML private TextField txtEspCarnId;
    @FXML private TextField txtEspCarnNombre;
    @FXML private ComboBox<String> cbEspCarnPeriodo;
    @FXML private TextField txtEspCarnPeso;
    @FXML private ComboBox<Integer> cbEspCarnPeligrosidad;

    // ==========================================
    // BLOQUE 2: COMPONENTES FXML - FORMULARIO HERBÍVORO
    // ==========================================
    @FXML private TextField txtEspHerbId;
    @FXML private TextField txtEspHerbNombre;
    @FXML private ComboBox<String> cbEspHerbPeriodo;
    @FXML private TextField txtEspHerbPeso;
    @FXML private TextField txtEspHerbTipoPlanta;

    // ==========================================
    // BLOQUE 3: COMPONENTES FXML - LISTA Y BOTONES
    // ==========================================
    @FXML private ListView<EspecieDinosaurio> lvEspeciesDinosaurios;
    @FXML private Button btnAgregarEspCarn;
    @FXML private Button btnAgregarEspHerb;
    @FXML private Button btnGuardarJSON;

    // ==========================================
    // BLOQUE 4: ATRIBUTOS DE LÓGICA Y PERSISTENCIA
    // ==========================================
    private GestorParque gestor;
    private EspecieDao especieDao;

    // Declaramos un atributo como lista observable las especies de dinosaurios agregadas.
    // Esto estará "atento" a cualquier cambio que ocurra en la lista de las especies
    // en el gestor.
    private ObservableList<EspecieDinosaurio> observableEspecies;

    /**
     * Metodo inicializador de la vista JavaFX.
     * Se ejecuta automáticamente al cargar el archivo FXML.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ==========================================
        // BLOQUE 5: INICIALIZACIÓN DE INSTANCIAS Y CARGA DESDE ARCHIVO JSON
        // ==========================================

        // Obtenemos el gestor del parque actual con getInstancia.
        // Si no esta creado, lo creará internamente (revisar metodo getInstancia())
        gestor = GestorParque.getInstancia();
        // Crea un objeto de la clase DAO que gestionará los registros con el archivo
        especieDao = new EspecieDaoJson();

        // Intentar leer las especies previamente guardadas en el archivo JSON mediante el DAO
        try {
            List<EspecieDinosaurio> especiesGuardadas = especieDao.cargarEspecies();
            if (especiesGuardadas != null && !especiesGuardadas.isEmpty()) {
                // Evita duplicados en memoria limpiando e insertando la lista leída desde el JSON
                gestor.getListaEspecies().clear();
                gestor.getListaEspecies().addAll(especiesGuardadas);
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Lectura",
                    "No se pudieron cargar los datos previos del archivo JSON: " + e.getMessage());
        }

        // ==========================================
        // BLOQUE 6: POBLAR COMBOBOX CON OPCIONES DE FORMULARIO
        // ==========================================
        // Períodos geológicos para carnívoros y herbívoros
        cbEspCarnPeriodo.getItems().setAll("Triásico", "Jurásico", "Cretácico");
        cbEspHerbPeriodo.getItems().setAll("Triásico", "Jurásico", "Cretácico");

        // Nivel de peligrosidad (del 1 al 10) para el selector de carnívoros
        cbEspCarnPeligrosidad.getItems().setAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Actualizar la interfaz visual con la lista de especies (incluidas las leídas del JSON)
        actualizarListaEspecies();
    }

    /**
     * Sincroniza la lista en memoria del GestorParque con el componente ListView de la vista.
     */
    private void actualizarListaEspecies() {
        // ==========================================
        // BLOQUE 7: REFRESCAR ELEMENTOS EN EL LISTVIEW
        // ==========================================

        // Añadimos los elementos de la lista en el gestor a la lista observable.
        // Si un elemento cambia en la lista del gestor, la lista observable se actualiza inmediatamente
        observableEspecies = FXCollections.observableArrayList(gestor.getListaEspecies());
        // Utilzamos un ListView para mostrar los elementos de la lista a mostrar
        lvEspeciesDinosaurios.setItems(observableEspecies);
    }

    /**
     * Maneja el registro de especies carnívoras con validaciones.
     */
    @FXML
    private void AgregarEspecieCarnivoro() {
        // ==========================================
        // BLOQUE 8: EVENTO AGREGAR ESPECIE CARNÍVORA
        // ==========================================
        try {
            // Obtenemos los valores del text field de id y nombre
            // ( ❕ IMPORTANTE: Aprender esta forma de obtener valores de text fields)
            String id = txtEspCarnId.getText();
            String nombre = txtEspCarnNombre.getText();
            // Obtenemos el valor seleccionado en el combo box de período
            // ( ❕ IMPORTANTE: Aprender esta forma de obtener valores de combo box)
            String periodo = cbEspCarnPeriodo.getValue();

            if (txtEspCarnPeso.getText().isEmpty()) {
                throw new DatoInvalidoException("El campo peso no puede estar vacío.");
            }
            // Transformamos el valor string que viene del campo texto de peso a float
            // ( ❕ IMPORTANTE: Aprender el formato de estas transformaciones)
            float peso = Float.parseFloat(txtEspCarnPeso.getText());

            // Obtenemos el valor seleccionado en el combo box de Nivel de peligrosidad
            // ( ❕ IMPORTANTE: Aprender esta forma de obtener valores de combo box)
            Integer peligrosidad = cbEspCarnPeligrosidad.getValue();
            if (peligrosidad == null) {
                throw new DatoInvalidoException("Debe seleccionar un nivel de peligrosidad.");
            }

            // Instancia de EspecieCarnivoro
            EspecieCarnivoro carnivoro = new EspecieCarnivoro(id, nombre, periodo, peso, peligrosidad);

            // Registro en el Gestor en memoria
            gestor.agregarEspecie(carnivoro);

            // Actualización de UI
            actualizarListaEspecies();
            limpiarFormularioCarnivoro();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Especie carnívora agregada a la lista.");

        }
        // En caso que ocurran excepciones, se enviaran alertas.
        // ( ❕ IMPORTANTE: Aprender el formato de alertas)
        catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El peso ingresado debe ser un número válido.");
        } catch (DatoInvalidoException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error al registrar la especie: " + e.getMessage());
        }
    }

    /**
     * Maneja el registro de especies herbívoras con validaciones.
     */
    @FXML
    private void AgregarEspecieHerbivoro() {
        // ==========================================
        // BLOQUE 9: EVENTO AGREGAR ESPECIE HERBÍVORA
        // ==========================================
        try {
            // Obtenemos los valores del text field de id y nombre
            // ( ❕ IMPORTANTE: Aprender esta forma de obtener valores de text fields)
            String id = txtEspHerbId.getText();
            String nombre = txtEspHerbNombre.getText();
            // Obtenemos el valor seleccionado en el combo box de período
            // ( ❕ IMPORTANTE: Aprender esta forma de obtener valores de combo box)
            String periodo = cbEspHerbPeriodo.getValue();

            if (txtEspHerbPeso.getText().isEmpty()) {
                throw new DatoInvalidoException("El campo peso no puede estar vacío.");
            }
            // Transformamos el valor string que viene del campo texto de peso a float
            // ( ❕ IMPORTANTE: Aprender el formato de estas transformaciones)
            float peso = Float.parseFloat(txtEspHerbPeso.getText());
            String tipoPlanta = txtEspHerbTipoPlanta.getText();

            // Instancia de EspecieHerbivoro
            EspecieHerbivoro herbivoro = new EspecieHerbivoro(id, nombre, periodo, peso, tipoPlanta);

            // Registro en el Gestor en memoria
            gestor.agregarEspecie(herbivoro);

            // Actualización de UI
            actualizarListaEspecies();
            limpiarFormularioHerbivoro();

            // Mensaje final de exito
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Especie herbívora agregada a la lista.");

        }
        // En caso que ocurran excepciones, se enviaran alertas.
        // ( ❕ IMPORTANTE: Aprender el formato de alertas)
        catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El peso ingresado debe ser un número válido.");
        } catch (DatoInvalidoException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error al registrar la especie: " + e.getMessage());
        }
    }

    /**
     * Guarda la lista completa de especies en el archivo JSON mediante el DAO.
     */
    @FXML
    private void GuardarJSON() {
        // ==========================================
        // BLOQUE 10: GUARDAR REGISTROS EN ARCHIVO JSON (PATRÓN DAO)
        // ==========================================
        try {
            // Invoca la capa DAO para sobreescribir/actualizar especies_dino.json con la lista actual
            especieDao.guardarEspecies(gestor.getListaEspecies());
            mostrarAlerta(Alert.AlertType.INFORMATION, "Persistencia Exitosa",
                    "Los registros de especies se han guardado correctamente en el archivo JSON.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Persistencia",
                    "No se pudo guardar el archivo JSON: " + e.getMessage());
        }
    }

    // ==========================================
    // BLOQUE 11: MÉTODOS AUXILIARES Y ALERTAS
    // ==========================================

    // Limpia el formulario carnivoro (opcional)
    private void limpiarFormularioCarnivoro() {
        txtEspCarnId.clear();
        txtEspCarnNombre.clear();
        cbEspCarnPeriodo.setValue(null);
        txtEspCarnPeso.clear();
        cbEspCarnPeligrosidad.setValue(null);
    }

    // Limpia el formulario herbivoro (opcional)
    private void limpiarFormularioHerbivoro() {
        txtEspHerbId.clear();
        txtEspHerbNombre.clear();
        cbEspHerbPeriodo.setValue(null);
        txtEspHerbPeso.clear();
        txtEspHerbTipoPlanta.clear();
    }

    // Metodo que envia una alerta como popUp
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}