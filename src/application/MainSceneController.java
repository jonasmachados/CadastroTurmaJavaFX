/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import model.services.AlunoService;
import util.Alerts;

/**
 * FXML Controller class
 *
 * @author Jonas create 20/12/2020
 * update 
 */
public class MainSceneController implements Initializable {
    
    //Atributos
    @FXML
    private MenuItem menuItemAbout;
    
    @FXML
    private MenuItem menuItemAreaDoAluno;
    
    @FXML
    private MenuItem menuItemAreaDoProfessor;
    
    @FXML
    private MenuItem menuItemTurma;
    
    //Metodo para tratar os eventos, tratando botao ABOUT
    @FXML
    public void onMenuItemAboutAction() {
        loadView("About.fxml", x -> {});
    }
    
    //Metodo para tratar os eventos do menu item Aluno
    @FXML
    public void onMenuItemAlunoAction() {
        loadView("AreaDoAlunoController.fxml", (AreaDoAlunoController controller) -> {
                controller.setAlunoService(new AlunoService()); //AlunoService tem metodo que recebe colecao de Aluno
                controller.updateTableView(); //Metodo updateView recebe 
        });
    }
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    //Criando um FXMLLoader para carregar a tela,e sincronizandod evitando as threads
    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); //GETROOT pega o primeiro elemento da View

            //Manipulacao da tela principal
            Node mainMenu = mainVBox.getChildren().get(0); //Creia um node recebe o Vbox
            mainVBox.getChildren().clear();//Limpa todos os filhos do Main VBox
            mainVBox.getChildren().add(mainMenu);//Pega os filhos do Main Nebu
            mainVBox.getChildren().addAll(newVBox.getChildren()); //Adicionando uma colecao < os filhos do Vbox
            
            //Executa a funcao
            T controller = loader.getController();
            initializingAction.accept(controller);
        } catch (Exception e) {
            //Caso ocorra a execao, o sistema ira msotrar uma Janela de alerta com o Erro
            //Alerts.showAlert("IOException", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }

    }

   
}
    

