package application;

import db.DbException;
import util.Alerts;
import util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listeners.DataChangeListener;
import model.entities.Aluno;
import model.exceptions.ValidationException;
import model.services.AlunoService;

/**
 * FXML Controller class
 *
 * @author Jonas create 20/12/2020
 */
public class AreaDoAlunoController implements Initializable, DataChangeListener {

    //Atributos are do aluno
    private Aluno entity;

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

    @FXML
    private Button btEdit;

    @FXML
    private Button btDelete;
    
    @FXML
    private Button btPesquisar;

    @FXML
    private TextField txtPesquisar;

    //TRATANDO EVENTO DO BOTAO NEW
    @FXML
    public void onBtNewAction(ActionEvent event) {
        Stage parenStage = Utils.currentStage(event);
        Aluno obj = new Aluno();
        createDialogForm(obj, "AlunoForm.fxml", parenStage);
    }

    //TRATANDO EVENTO DO BOTAO NEW
    @FXML
    public void onBtEditAction(ActionEvent event) {
        Stage parenStage = Utils.currentStage(event);
        Aluno obj = tableViewAluno.getSelectionModel().getSelectedItem();
        createDialogForm(obj, "AlunoForm.fxml", parenStage);

    }
    
    @FXML
    public void onBtPesquisarAction(ActionEvent event) {
        tableViewAluno.setItems(busca());

    }

    //TRATANDO EVENTO DO BOTAO DELETE
    @FXML
    public void onBtDeleteAction(ActionEvent event) {
        Stage parenStage = Utils.currentStage(event);
        Aluno obj = tableViewAluno.getSelectionModel().getSelectedItem();
        removeEntity(obj);
    }
      
    //SET PARA CLASSE DEPARTMENT SERVICE
    public void setAlunoService(AlunoService service) {
        this.service = service;
    }

    //Metodo que vai iniciar a table view dos alunos
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();

//        //listener
//        tableViewAluno.getSelectionModel().selectedItemProperty().addListener(
//                (observable, odlValue, newValue) -> selecionarItemTableViewClientes(newValue));
    }

    private void initializeNodes() {
        //Iniciar o comportamento das colunas
        tableColumnMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        //AJUSTANDO A TABLE VIEW AO TAMANHO DA TELA
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewAluno.prefHeightProperty().bind(stage.heightProperty());
    }

    //Metodo responsavel por carregar o servico e jogar o Aluno no ObservableList
    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Aluno> list = service.findyAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewAluno.setItems(obsList);

//        //Add outro listener outro observalList
//        tableViewAluno.getSelectionModel().selectedItemProperty().addListener(
//                (observable, odlValue, newValue) -> selecionarItemTableViewClientes(newValue));
         busca();     
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
            controller.subscribeDataChangeListener(this);//Inscrevendo para receber o metodo onDataChange, obseervable list
            controller.updateFormData(); //Carrega o Department no formulario

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Aluno data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false); //Resizable: Diz se janela pode ser redimencionada
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);//Trava a janela
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view ", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    //Quando dispara o evento, a o UpdateTableVie e chamado
    @Override
    public void onDataChanged() {
        updateTableView();
    }

    //Remocao do Department
    private void removeEntity(Aluno obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure that you want remove?");

        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was null");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (db.DbIntegrityException e) {
                e.printStackTrace();
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    //Observable LIST que realiza a busca por nome e joga em um Lista
    private ObservableList<Aluno> busca() {
        ObservableList<Aluno> alunoPesquisa = FXCollections.observableArrayList();
        for (int x = 0; x < obsList.size(); x++) {
           if(obsList.get(x).getNome().toLowerCase().contains(txtPesquisar.getText())) {
               alunoPesquisa.add(obsList.get(x));
           }
        }
        return alunoPesquisa;
    }
    
}
