package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Aluno;
import util.Utils;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.services.AlunoService;


/**
 * FXML Controller class
 *
 * @author Jonas create 20/12/2020
 */
public class AreaDoAlunoController implements Initializable {

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

    

}
