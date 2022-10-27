package Main;


//import ClasesApoyo.Toast;
import Controlador.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Vista/Vista.fxml"));
            Pane ventana = loader.load();


            // Show the scene containing the root layout.
            Scene scene = new Scene(ventana);
            Font.loadFont(getClass().getResourceAsStream("/Estilos/GOGOIARegular"), 12);
            Font.loadFont(getClass().getResourceAsStream("/Estilos/barbaro.ttf"), 18);
            Font.loadFont(getClass().getResourceAsStream("/Estilos/VanillaExtractRegular.ttf"), 18);
            scene.getStylesheets().add(getClass().getResource("/Estilos/style.css").toExternalForm());



            primaryStage.setScene(scene);
            primaryStage.setTitle("POMODORO");
            primaryStage.setResizable(false);
            primaryStage.setX(1220);
            primaryStage.setY(590);
            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
