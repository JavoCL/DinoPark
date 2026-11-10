package cl.duoc.dinopark.controller;

import cl.duoc.dinopark.GestorParque;
import cl.duoc.dinopark.dao.EspecieDao;
import cl.duoc.dinopark.dao.EspecieDaoJson;
import cl.duoc.dinopark.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controlador principal para la nueva vista unificada de DinoPark-Dex.
 * Gestiona la TableView central y el formulario dinámico lateral para registrar
 * tanto carnívoros como herbívoros en una sola pantalla.
 */
public class VistaPrincipalController {

    // ==========================================
    // BLOQUE 1: COMPONENTES FXML - TABLA Y COLUMNAS
    // ==========================================
    @FXML private TableView<EspecieDinosaurio> tblEspecies;
    @FXML private TableColumn<EspecieDinosaurio, String> colTipo;
    @FXML private TableColumn<EspecieDinosaurio, String> colCodigo;
    @FXML private TableColumn<EspecieDinosaurio, String> colNombreEspecie;
    @FXML private TableColumn<EspecieDinosaurio, String> colPeriodo;
    @FXML private TableColumn<EspecieDinosaurio, Double> colPeso;
    @FXML private TableColumn<EspecieDinosaurio, String> colCaracteristicas;

    // ==========================================
    // BLOQUE 2: COMPONENTES FXML - FORMULARIO DINÁMICO
    // ==========================================
    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombreEspecie;
    @FXML private ComboBox<String> cbPeriodo;
    @FXML private TextField txtPeso;
    @FXML private ComboBox<Integer> cbPeligrosidad;
    @FXML private TextField txtPlantaPreferida;

    @FXML private Label lbPeligrosidad;
    @FXML private Label lbPlantaPreferida;
    @FXML private Button btnAgregarEspecie;
    @FXML private Label lblMensaje;

    // ==========================================
    // BLOQUE 3: ATRIBUTOS DE LÓGICA Y GESTIÓN
    // ==========================================
    private ObservableList<EspecieDinosaurio> listaObservable;
    private GestorParque gestor;
    private EspecieDao especieDao;

    /**
     * Método inicializador que se ejecuta automáticamente al cargar el FXML.
     */
    @FXML
    public void initialize() {
        // ==========================================
        // BLOQUE 4: CONFIGURACIÓN INICIAL DE LA TABLA Y DATOS
        // ==========================================
        gestor = GestorParque.getInstancia();
        especieDao = new EspecieDaoJson();

        // Configurar el mapeo de las columnas estándar de la TableView
        colTipo.setCellValueFactory(cellData -> {
            EspecieDinosaurio especie = cellData.getValue();
            String tipoStr = (especie instanceof EspecieCarnivoro) ? "Carnívoro" : "Herbívoro";
            return new javafx.beans.property.SimpleStringProperty(tipoStr);
        });
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreEspecie.setCellValueFactory(new PropertyValueFactory<>("nombreEspecie"));
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        // Columna dinámica para reflejar la característica propia según la subclase polimórfica
        colCaracteristicas.setCellValueFactory(cellData -> {
            EspecieDinosaurio especie = cellData.getValue();
            String caracteristica = "";
            if (especie instanceof EspecieCarnivoro) {
                caracteristica = "Peligrosidad: " + ((EspecieCarnivoro) especie).getNivelPeligrosidad();
            } else if (especie instanceof EspecieHerbivoro) {
                caracteristica = "Planta: " + ((EspecieHerbivoro) especie).getTipoPlantaPreferida();
            }
            return new javafx.beans.property.SimpleStringProperty(caracteristica);
        });

        // Poblar los ComboBox principales con opciones predefinidas
        cbTipo.setItems(FXCollections.observableArrayList("Carnívoro", "Herbívoro"));
        cbPeriodo.setItems(FXCollections.observableArrayList("Triásico", "Jurásico", "Cretácico"));
        cbPeligrosidad.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        // Cargamos los datos en el gestor
        try {
            // 1. Cargamos las especies desde el archivo JSON al iniciar
            List<EspecieDinosaurio> especiesGuardadas = especieDao.cargarEspecies();

            // 2. Si existen registros, los incorporamos al gestor en memoria
            if (especiesGuardadas != null && !especiesGuardadas.isEmpty()) {
                for (EspecieDinosaurio especie : especiesGuardadas) {
                    gestor.agregarEspecie(especie);
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudieron cargar los datos previos: " + e.getMessage());
        }

        // Cargar los datos actuales almacenados en el Gestor hacia la lista observable de la tabla
        listaObservable = FXCollections.observableArrayList(gestor.getListaEspecies());
        tblEspecies.setItems(listaObservable);

        lblMensaje.setText("Sistema listo. Ingrese los datos de la especie.");
    }

    /**
     * Alterna la visibilidad de los campos específicos del formulario según el tipo seleccionado.
     */
    @FXML
    public void cambiarCategoria(ActionEvent event) {
        // ==========================================
        // BLOQUE 5: CAMBIO DINÁMICO DE CAMPOS (INTERFAZ)
        // ==========================================
        String seleccion = cbTipo.getValue();
        if ("Carnívoro".equals(seleccion)) {
            // Mostrar campos exclusivos de carnívoros
            cbPeligrosidad.setManaged(true);
            cbPeligrosidad.setVisible(true);
            lbPeligrosidad.setManaged(true);
            lbPeligrosidad.setVisible(true);

            // Ocultar campos de herbívoros
            txtPlantaPreferida.setManaged(false);
            txtPlantaPreferida.setVisible(false);
            lbPlantaPreferida.setManaged(false);
            lbPlantaPreferida.setVisible(false);
        } else if ("Herbívoro".equals(seleccion)) {
            // Ocultar campos de carnívoros
            cbPeligrosidad.setManaged(false);
            cbPeligrosidad.setVisible(false);
            lbPeligrosidad.setManaged(false);
            lbPeligrosidad.setVisible(false);

            // Mostrar campos exclusivos de herbívoros
            txtPlantaPreferida.setManaged(true);
            txtPlantaPreferida.setVisible(true);
            lbPlantaPreferida.setManaged(true);
            lbPlantaPreferida.setVisible(true);
        }
    }

    /**
     * Valida, instancia y registra una nueva especie (carnívora o herbívora) en el sistema.
     */
    @FXML
    public void AgregarEspecie(ActionEvent event) {
        // ==========================================
        // BLOQUE 6: VALIDACIÓN Y REGISTRO DE ESPECIE
        // ==========================================
        try {
            // 1. Validar selección de tipo general
            String tipo = cbTipo.getValue();
            if (tipo == null || tipo.isEmpty()) {
                throw new DatoInvalidoException("Debe seleccionar un tipo de especie (Carnívoro o Herbívoro).");
            }

            // 2. Capturar y validar campos comunes obligatorios
            String codigo = txtCodigo.getText();
            String nombreEspecie = txtNombreEspecie.getText();
            String periodo = cbPeriodo.getValue();

            if (codigo == null || codigo.trim().isEmpty() ||
                    nombreEspecie == null || nombreEspecie.trim().isEmpty() ||
                    periodo == null) {
                throw new DatoInvalidoException("Todos los campos de texto y el periodo son obligatorios.");
            }

            // 3. Validar y parsear el peso numérico de manera segura
            String pesoStr = txtPeso.getText();
            if (pesoStr == null || pesoStr.trim().isEmpty()) {
                throw new DatoInvalidoException("El campo peso no puede estar vacío.");
            }

            float peso;
            try {
                peso = Float.parseFloat(pesoStr);
                if (peso <= 0) {
                    throw new DatoInvalidoException("El peso debe ser un valor numérico mayor a 0.");
                }
            } catch (NumberFormatException e) {
                throw new DatoInvalidoException("El peso ingresado tiene un formato inválido (debe ser un número).");
            }

            // 4. Polimorfismo: Instanciar la subclase adecuada según la categoría elegida
            EspecieDinosaurio nuevaEspecie = null;

            if ("Carnívoro".equals(tipo)) {
                Integer peligrosidad = cbPeligrosidad.getValue();
                if (peligrosidad == null) {
                    throw new DatoInvalidoException("Debe seleccionar un nivel de peligrosidad para el carnívoro.");
                }
                nuevaEspecie = new EspecieCarnivoro(codigo, nombreEspecie, periodo, peso, peligrosidad);

            } else if ("Herbívoro".equals(tipo)) {
                String plantaPreferida = txtPlantaPreferida.getText();
                if (plantaPreferida == null || plantaPreferida.trim().isEmpty()) {
                    throw new DatoInvalidoException("Debe ingresar la planta preferida para el herbívoro.");
                }
                nuevaEspecie = new EspecieHerbivoro(codigo, nombreEspecie, periodo, peso, plantaPreferida);
            }

            // 5. Registrar en memoria mediante el Gestor y actualizar la TableView
            gestor.agregarEspecie(nuevaEspecie);
            listaObservable.setAll(gestor.getListaEspecies());

            // Invocación automática del DAO para persistir de inmediato
            try {
                especieDao.guardarEspecies(gestor.getListaEspecies());
            } catch (Exception e) {
                lblMensaje.setText("Aviso: No se pudo respaldar en el archivo JSON: " + e.getMessage());
            }

            // 6. Limpiar formulario y reportar éxito al usuario
            limpiarFormulario();
            lblMensaje.setText("¡Especie registrada correctamente en el sistema!");

        } catch (DatoInvalidoException e) {
            // Manejo de reglas de negocio personalizadas
            lblMensaje.setText("Validación: " + e.getMessage());
        } catch (Exception e) {
            // Manejo de errores genéricos imprevistos
            lblMensaje.setText("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Limpia todos los campos del formulario y restablece el mensaje del sistema.
     */
    private void limpiarFormulario() {
        // ==========================================
        // BLOQUE 7: MÉTODO AUXILIAR DE LIMPIEZA
        // ==========================================
        cbTipo.setValue(null);
        txtCodigo.clear();
        txtNombreEspecie.clear();
        cbPeriodo.setValue(null);
        txtPeso.clear();
        cbPeligrosidad.setValue(null);
        txtPlantaPreferida.clear();
        lblMensaje.setText("[mensaje_sistema]");
    }
}