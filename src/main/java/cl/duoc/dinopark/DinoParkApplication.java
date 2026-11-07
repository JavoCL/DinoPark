package cl.duoc.dinopark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DinoParkApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DinoParkApplication.class.getResource("vista-principal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());//, 320, 240);
        stage.sizeToScene();
        stage.setTitle("Dino Park");
        stage.setScene(scene);
        stage.show();
    }
}
