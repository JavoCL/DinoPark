module cl.duoc.dinopark {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    // 🟢 Permite que JavaFX FXML acceda a todos tus controladores
    opens cl.duoc.dinopark.controller to javafx.fxml;

    // 🟢 Permite que JavaFX TableView lea los atributos del modelo (indispensable para las tablas)
    opens cl.duoc.dinopark.model to javafx.base;

    exports cl.duoc.dinopark;
    opens cl.duoc.dinopark to javafx.base;
}