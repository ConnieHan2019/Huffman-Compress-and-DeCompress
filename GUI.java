import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GUI extends Application {
    public static Scene startScene;
    public static Scene loadingScene;
    public static Scene finishScene;

    public static GridPane startPane = new GridPane();
    public static BorderPane loadingPane = new BorderPane();
    public static BorderPane finishPane = new BorderPane();

    public void start(Stage primaryStage){

        startPane.setAlignment(Pos.BASELINE_CENTER);
        //startPane.setPadding(new Insets(11.5,12.5,13.5,13.5));
        startPane.setHgap(5.5);
        startPane.setVgap(5.5);

        Label name = new Label("Ablsolute file path : ");
        TextField path = new TextField();
        startPane.add(name,0,0);
        startPane.add(path,1,0);
        
       Button depressBt = new Button("depress");
       Button pressBt = new Button("press");
        startPane.add(depressBt,0,1);
        startPane.add(pressBt,1,1);
        GridPane.setHalignment(depressBt, HPos.RIGHT);
        GridPane.setHalignment(pressBt, HPos.RIGHT);




       startScene = new Scene(startPane,700,100);
        primaryStage.setTitle("Press and Depress");


        Label loading = new Label("loading");
       loadingPane.setCenter(loading);
        loadingScene = new Scene(loadingPane,700,100);

        Label finish = new Label("finish");
        finishPane.setCenter(finish);
         finishScene = new Scene(finishPane,700,100);
        primaryStage.setScene(startScene);
        primaryStage.show();
        pressBt.setOnAction( e ->{
            String sourcepath = path.getText();
            primaryStage.setScene(loadingScene);
            if(press(sourcepath)){
                primaryStage.setScene(finishScene);
            }

        });
        depressBt.setOnAction( e ->{
            String sourcepath = path.getText();
           primaryStage.setScene(loadingScene);
           if(depress(sourcepath)) {
               primaryStage.setScene(finishScene);
           }
        });



    }
    public static boolean press(String sourcepath){
        henc press = new henc(sourcepath);
       // String sourcepath = "E:/Project 1/Test Cases/4/1.jpeg";
  if (press.success == true)
    return press.success;
  else{
      return false;
  }
    }

    public static boolean depress(String sourcepath){
        hdec depress = new hdec(sourcepath);
        if(depress.success == true)
       return depress.success;
        else{
            return false;
        }
    }
    /**
     * E:/Project 1/Test Cases/4
     * E:/Project 1/Test Cases/4/1.jpeg
     * E:/Project 1/Test Cases/test3 - empty file and empty folder/empty_folder
     * E:/Project 1/Test Cases/test2 - folder
     */
}
