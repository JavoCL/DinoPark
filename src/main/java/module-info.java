module cl.duoc.dinopark {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    // Permite que JavaFX FXML acceda a todos tus controladores
    opens cl.duoc.dinopark.controller to javafx.fxml;

    // CRUCIAL: Permitir que Gson y JavaFX accedan por reflexión al modelo
    opens cl.duoc.dinopark.model to com.google.gson, javafx.base;

    // CRUCIAL: Permite que Gson acceda al paquete DAO (ContenedorEspecies)
    opens cl.duoc.dinopark.dao to com.google.gson;

    exports cl.duoc.dinopark;
    opens cl.duoc.dinopark to javafx.base;
}