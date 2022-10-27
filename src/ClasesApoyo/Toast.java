package ClasesApoyo;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public final class Toast
{
    public static void makeText(Stage ownerStage, String toastMsg,String titleToasMsg
            , int toastDelay, int fadeInDelay, int fadeOutDelay)
    {
        Stage toastStage=new Stage();

        toastStage.initOwner(ownerStage);
        toastStage.setAlwaysOnTop(true);
        toastStage.setHeight(60);
        toastStage.setWidth(350);
        toastStage.setX(1475);
        toastStage.setY(1010);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(toastMsg);

        text.setFont(Font.font("Monospaced", FontWeight.BOLD , 18));
        text.setFill(Color.BLACK);

        Text textTitle = new Text(titleToasMsg);
        textTitle.setTextAlignment(TextAlignment.CENTER);
        textTitle.setFont(Font.font("Unispace",  24));
        textTitle.setFill(Color.BLACK);

        VBox contenedorTitulo = new VBox();
        contenedorTitulo.getChildren().addAll(textTitle);
        contenedorTitulo.setAlignment(Pos.TOP_LEFT);

        VBox contenedorTexto = new VBox();
        contenedorTexto.getChildren().addAll(text);
        contenedorTexto.setAlignment(Pos.CENTER_LEFT);




        StackPane root = new StackPane(contenedorTitulo, contenedorTexto);

       // root.setStyle("-fx-background-radius: 20; -fx-border-color: #5e6472; -fx-background-color: rgba(255, 255, 255, 0.8); -fx-padding: 50px;");


        root.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        root.setStyle(" -fx-border-color: #000000; -fx-border-width: 1;  -fx-background-color: rgba(255, 255, 255, 1);  ");
        root.setOpacity(0);


        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        toastStage.setScene(scene);
        toastStage.show();

        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);
        fadeInTimeline.setOnFinished((ae) ->
        {
            new Thread(() -> {
                try
                {
                    Thread.sleep(toastDelay);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Timeline fadeOutTimeline = new Timeline();
                KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 0));
                fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                fadeOutTimeline.play();
            }).start();
        });

        fadeInTimeline.play();

        root.setOnMouseClicked(mouseEvent -> {

            root.setVisible(false);
        });

    }

}
