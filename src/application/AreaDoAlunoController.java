package application;

import util.Alerts;
import util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listeners.DataChangeListener;
import model.entities.Aluno;
import model.services.AlunoService;


/**
 * FXML Controller class
 *
 * @author Jonas create 20/12/2020
 */
public class AreaDoAlunoController implements Initializable, DataChangeListener {

    //Atributos are do aluno
    @FXML
    private AlunoService service; //Injetando o aluno service
    
    private ObservableList<Aluno> obsList;
    
    @FXML
    private TableView<Aluno> tableViewAluno;

    @FXML
    private TableColumn<Aluno, Integer> tableColumnMatricula;

    @FXML
    private TableColumn<Aluno, Integer> tableColumnNome;

    @FXML
    private TableColumn<Aluno, Integer> tableColumnCodigo;

    @FXML
    private Button btNew;

    //TRATANDO EVENTO DO BOTAO NEW
    @FXML
    public void onBtNewAction(ActionEvent event) {
        Stage parenStage = Utils.currentStage(event);
        Aluno obj = new Aluno();
        createDialogForm(obj, "AlunoForm.fxml", parenStage);
    }
    
    //SET PARA CLASSE DEPARTMENT SERVICE
    public void setAlunoService(AlunoService service) {
        this.service = service;
    }

    //Metodo que inicia ao iniciar a classe
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }
    
    private void initializeNodes() {
        //Iniciar o comportamento das colunas
        tableColumnMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        //AJUSTANDO A TABLE VIEW AO TAMANHO DA TELA
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewAluno.prefHeightProperty().bind(stage.heightProperty());
    }
    
        //Metodo responsavel por carregar o servico  ejogar o Department
    //Na observableList
    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Aluno> list = service.findyAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewAluno.setItems(obsList);
        
    }
    
    //Metodo da janela de dialogo, para salvar os dados do aluno
    private void createDialogForm(Aluno obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            //instancia um novo stage , criando um novo stage

            AlunoFormController controller = loader.getController();
            controller.setAluno(obj);
            controller.setAlunoService(new AlunoService());
            controller.subscribeDataChangeListener(this);//Inscrevendo para receber o metodo onDataChange
            controller.updateFormData(); //Carrea o Department no formulario

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Aluno data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false); //Resizable: Diz se janela pode ser redimencionada
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);//Trava a janela
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Error loading view ", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    //Qaudno dispara o evento, a o UpdateTableVie e chamado
    @Override
    public void onDataChanged() {
        updateTableView();
    }
    
    

    

}
