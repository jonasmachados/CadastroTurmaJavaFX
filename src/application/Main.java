package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

/**
 *
 * @author Jonas 07/12/2020 modified
 */
public class Main extends Application {

    private static Scene mainScene; //Atributo privado que esta recebendo a Scena

    public static void main(String[] args) {
        launch(args);
    }

    //Implementando o metodo start
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
            ScrollPane scrollPane = loader.load();
            //Ajusta janela  do ScrollPane
            scrollPane.setFitToHeight(true); //Ajusta Altura
            scrollPane.setFitToWidth(true);  //Ajustar Largura

            //Cria scena
            mainScene = new Scene(scrollPane);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("SISTEMA ESCOLA"); //Titulo para o palco
            primaryStage.show(); //mostrando o palco
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo que pega referencia da Scena
    public static Scene getMainScene() {
        return mainScene;
    }

}
