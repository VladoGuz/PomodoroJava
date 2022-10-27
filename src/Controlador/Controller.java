package Controlador;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.sound.sampled.*;

import ClasesApoyo.*;

import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class Controller implements Initializable {

    @FXML
    public Button boton_reiniciar;

    @FXML
    public Button boton_cambiante;


    @FXML
    public Label txt_Tiempo;
    @FXML
    public TextField txt_TextField_MostrarTiempo;

    @FXML
    public TextField TextField_IngresarMinutosTrabajo;
    @FXML
    public TextField TextField_IngresarMinutosDescanso;

    public Label label_accionActiva;

    public Label Pomodoro;




    @FXML
    public Button boton_Config;
    @FXML
    public Pane opcionesPane;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public Button boton_saltarActividad;

    public javafx.scene.layout.AnchorPane AnchorPane;

    public CheckBox checkbox_AutoStart;

    private boolean mostrarOpcion;

    @FXML
    public void opciones_MostrarOcultar(){

       opcionesPane.setVisible(!mostrarOpcion);
        mostrarOpcion =! mostrarOpcion;
    }

    private void barraProgreso(){
        int conversionSegundosActuales =(( minutos * 60) + segundos) ;
        int conversionSegundosEstablecido = (minutosIngresadorSinCambiar*60) ;

        double progreso =(double)(conversionSegundosEstablecido - conversionSegundosActuales) / conversionSegundosEstablecido;


        progressBar.setProgress(progreso);
    }

    @FXML
    public void saltarActividad(){
        time.pause();
        segundos = 0;
        cambiarImagenIcono();
        time.stop();//Sin esta opcion inicia en automatico la siguiente task
        actividad_A_Realizar();
        descansoActivo = !descansoActivo;
        actualizarLabel();



    }
    Integer minutosIngresadorSinCambiar = -1;
    private boolean descansoActivo = false;
    private Timeline time;
    private int minutos = 0, segundos = 0;




    @FXML
    public void decidirAccion() {


        if (!isNumCorrecto()) validarMinutos(TextField_IngresarMinutosTrabajo);

        if (isNumCorrecto()) {

            String accion = boton_cambiante.getText();

            switch (accion) {
                case "Iniciar":
                    clickIniciar();
                    boton_cambiante.setText("Pausar");
                    boton_cambiante.setGraphic(new ImageView(new Image("/Recursos/pause.png")));


                    break;
                case "Pausar":
                    clickPausar();
                    boton_cambiante.setText("Continuar");
                    boton_cambiante.setGraphic(new ImageView(new Image("/Recursos/despausar.png")));

                    break;
                case "Continuar":
                    clickContinuar();
                    boton_cambiante.setText("Pausar");
                    boton_cambiante.setGraphic(new ImageView(new Image("/Recursos/pause.png")));

                    break;
                default:
                    break;

            }
        }


    }

    private void actividad_A_Realizar() {
        if (!descansoActivo) {

            validarMinutos(TextField_IngresarMinutosDescanso);
            label_accionActiva.setText("Descanso");

        } else {

            validarMinutos(TextField_IngresarMinutosTrabajo);
            label_accionActiva.setText("Enfocado");

        }
    }


    @FXML// puedo validar aceptando solo numeros, cambiar
    public void clickIniciar() {
        validarMinutos();

        if (isNumCorrecto()) { time.play();}
    }

    private void validarMinutos(){
        if (!descansoActivo) { validarMinutos(TextField_IngresarMinutosTrabajo);

        } else validarMinutos(TextField_IngresarMinutosDescanso);

    }

    @FXML
    public void clickPausar() { time.pause(); }
    @FXML
    public void clickContinuar() { time.play();}
    @FXML
    public void clickReiniciar() {
        validarMinutos();
        /*
       if(label_accionActiva.getText().equals("Enfocado")){
           validarMinutos(TextField_IngresarMinutosTrabajo);

       } else validarMinutos(TextField_IngresarMinutosDescanso);
         */


        if (isNumCorrecto()) {

            time.stop();
            cambiarImagenIcono();
            minutos = minutosIngresadorSinCambiar;
            segundos = 0;
            actualizarLabel();
        }

    }

    @FXML
    public void actualizarLabel() {
        String time = String.format("%02d:%02d", minutos, segundos);
        txt_TextField_MostrarTiempo.setText(time);

    }

    public void validarMinutos(TextField ingresarMinutos) {
        try {

            int min = Integer.parseInt(ingresarMinutos.getText());
            if (min > 0 && min < 60) {
                minutosIngresadorSinCambiar = min;
                minutos = minutosIngresadorSinCambiar;
            } else showError("Fuera de rango", "Ingresa un número");
        } catch (NumberFormatException e) {
            showError("Valor erroneo", "Ingresa un número");

        }

    }

    public boolean isNumCorrecto() {

        return minutosIngresadorSinCambiar != null && minutosIngresadorSinCambiar != -1;
    }



    private void alertaFinalizado() {
        Alert alertaFinalizado = new Alert(Alert.AlertType.INFORMATION);
        alertaFinalizado.initStyle(StageStyle.DECORATED);
        alertaFinalizado.setContentText("Ha finalizado el tiempo uwu");

        alertaFinalizado.show();
    }
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);

        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


       //notificacion();
        temporizador();
    }

    public Controller() {



    }
    private void temporizador(){
        time = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (segundos > 0) {
                segundos--;
            } else if (minutos > 0) {
                minutos--;
                segundos = 59;
            }

            barraProgreso();
            actualizarLabel();

            if (segundos == 0 && minutos == 0) {


                notificacioLibreria();
               // notificacion();
               // alertaFinalizado();
                cambiarImagenIcono();
                if (!checkbox_AutoStart.isSelected()){

                    time.stop();//Sin esta opcion inicia en automatico la siguiente task

                }

                actividad_A_Realizar();
                descansoActivo = !descansoActivo;
                actualizarLabel();
                //playSound();

            }


        })
        );



        time.setCycleCount(Timeline.INDEFINITE);


    }

    public void cambiarImagenIcono() {
        boton_cambiante.setGraphic(new ImageView(new Image("/Recursos/play.png")));
        boton_cambiante.setText("Iniciar");


    }

    @FXML
    private void closePanePressEsc(KeyEvent ke) {

        if (ke.getCode().toString().equals("ENTER") && !mostrarOpcion) {
            decidirAccion();

        }

         if (mostrarOpcion){

            if (ke.getCode().toString().equals("ESCAPE")){
                 cancelarConfiguracion();
             }

            if(ke.getCode().toString().equals("ENTER")) {
                ocultarVentana();
             }


         }


    }
    @FXML
    private void ocultarVentana(){
        validarMinutos();
        segundos = 0;
        actualizarLabel();
        mostrarOpcion = false;
        opcionesPane.setVisible(false);

    }

    @FXML
    private void cancelarConfiguracion(){

        mostrarOpcion = false;
        opcionesPane.setVisible(false);

    }
    @FXML
    public void notificarConBoton(){
        notificacion();
    }

    private void notificacioLibreria(){
        String texto = String.format("Ha finalizado el tiempo de %s :3",
                label_accionActiva.getText());
        TrayNotification tray = new TrayNotification("Pomodoro"
                , texto,
                NotificationType.SUCCESS);

        playSound();
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.millis(1000));
        playSound();
    }

    private void notificacion(){

        String title = " POMODORO";
        String toastMsg = "\n Ha finalizado el tiempo UwU";
        int toastMsgTime = 2500; //3.5 seconds
        int fadeInTime = 1000; //0.5 seconds
        int fadeOutTime= 800; //0.5 seconds
        Toast.makeText(new Stage(), toastMsg, title, toastMsgTime,fadeInTime, fadeOutTime);

    }

        private void playSound(){
        try {
            AudioInputStream audioInputStream =
                    AudioSystem.
                            getAudioInputStream(
                            new File("E:/Downloads//Telegram Desktop/ring.wav")

                                    .getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        }

        @FXML
        public void validarSoloNumeros(KeyEvent ke){

            /*
            //Character.isDigit(ke.getText().charAt(0))
            if( !ke.getText().equals("") && !ke.getCode().isDigitKey() ){

                   System.out.println(ke.getText() + " la tecla es");
                System.out.println(TextField_IngresarMinutosTrabajo.getText());
                   TextField_IngresarMinutosTrabajo.setText(
                           TextField_IngresarMinutosTrabajo.getText()
                                   .replaceAll("[^\\d]+", "")
                   );


               }
            */

        }



}
