package application;

import util.Alerts;
import util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listeners.DataChangeListener;
import model.entities.Turma;
import model.services.TurmaService;

/**
 * FXML Controller class
 *
 * @author Jonas create 26/12/2020
 */
public class TurmaController implements Initializable, DataChangeListener {

    //Atributos are do turma
    private Turma entity;

    private TurmaService service; //Injetando o turma service

    private ObservableList<Turma> obsList;

    @FXML
    private TableView<Turma> tableViewTurma;

    @FXML
    private TableColumn<Turma, Integer> tableColumnCodigo;

    @FXML
    private TableColumn<Turma, String> tableColumnSala;

    @FXML
    private TableColumn<Turma, Integer> tableColumnProfessorId;

    @FXML
    private TableColumn<Turma, Date> tableColumnDataAbertura;

    @FXML
    private TableColumn<Turma, Date> tableColumnDataFechamento;

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
        Turma obj = new Turma();
        createDialogForm(obj, "TurmaForm.fxml", parenStage);
    }

    //TRATANDO EVENTO DO BOTAO NEW
    @FXML
    public void onBtEditAction(ActionEvent event) {
        Stage parenStage = Utils.currentStage(event);
        Turma obj = tableViewTurma.getSelectionModel().getSelectedItem();
        createDialogForm(obj, "TurmaForm.fxml", parenStage);

    }

    @FXML
    public void onBtPesquisarAction(ActionEvent event) {
        tableViewTurma.setItems(busca());
    }

    //TRATANDO EVENTO DO BOTAO DELETE
    @FXML
    public void onBtDeleteAction(ActionEvent event) {
        Stage parenStage = Utils.currentStage(event);
        Turma obj = tableViewTurma.getSelectionModel().getSelectedItem();
        removeEntity(obj);
    }

    //SET PARA CLASSE TURMA SERVICE
    public void setTurmaService(TurmaService service) {
        this.service = service;
    }

    //Metodo que vai iniciar a table view dos turmas
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();

    }

    private void initializeNodes() {
        //Iniciar o comportamento das colunas
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tableColumnSala.setCellValueFactory(new PropertyValueFactory<>("sala"));
        tableColumnProfessorId.setCellValueFactory(new PropertyValueFactory<>("professorId"));
        tableColumnDataAbertura.setCellValueFactory(new PropertyValueFactory<>("dataAbertura"));
        Utils.formatTableColumnDate(tableColumnDataAbertura, "yyyy-MM-dd");// Metodo formatTbaleDouble vai formata a data da tabela
        tableColumnDataFechamento.setCellValueFactory(new PropertyValueFactory<>("dataFechamento"));
        Utils.formatTableColumnDate(tableColumnDataFechamento, "yyyy-MM-dd");// Metodo formatTbaleDouble vai formata a data da tabela

        //AJUSTANDO A TABLE VIEW AO TAMANHO DA TELA
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewTurma.prefHeightProperty().bind(stage.heightProperty());
    }

    //Metodo responsavel por carregar o servico e jogar o Turma no ObservableList
    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Turma> list = service.findyAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewTurma.setItems(obsList);

        busca();
    }

    //Metodo da janela de dialogo, para salvar os dados do turma
    private void createDialogForm(Turma obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            //instancia um novo stage , criando um novo stage

            TurmaFormController controller = loader.getController();
            controller.setTurma(obj);
            controller.setTurmaService(new TurmaService());
            controller.subscribeDataChangeListener(this);//Inscrevendo para receber o metodo onDataChange, obseervable list
            controller.updateFormData(); //Carrega a Turma no formulario

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Turma data");
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
    private void removeEntity(Turma obj) {
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
    private ObservableList<Turma> busca() {
        ObservableList<Turma> turmaPesquisa = FXCollections.observableArrayList();
        for (int x = 0; x < obsList.size(); x++) {
            if (obsList.get(x).getSala().toLowerCase().contains(txtPesquisar.getText().toLowerCase())) {
                turmaPesquisa.add(obsList.get(x));
            }
        }
        return turmaPesquisa;
    }

}
