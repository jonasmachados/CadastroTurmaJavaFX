package application;

import db.DbException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import listeners.DataChangeListener;
import model.entities.Professor;
import model.exceptions.ValidationException;
import model.services.ProfessorService;
import util.Alerts;
import util.Constraints;
import util.Utils;

/**
 * FXML Controller class
 *
 * @author Jonas create 26/12/2020
 */
public class ProfessorFormController implements Initializable {

    //Atributos
    private Professor entity;

    private ProfessorService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtIdNome;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtTitulacao;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorTitulacao;

    @FXML
    private Button btSalve;

    @FXML
    private Button btCancel;

    //SET PARA O ALUNO SERVICE
    public void setProfessorService(ProfessorService service) {
        this.service = service;
    }

    //Metodo que adiciona um novo obejto na lista, para isso a classe que recebe o evento deve implementar a interface dataChageListener
    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    //Implementando metodo set do entity 
    public void setProfessor(Professor entity) {
        this.entity = entity;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        //Try pois ira tenta salvar no banco de dados
        try {
            entity = getFormData();
            service.saveOrUpdate(entity); //salvandos os dados
            notifyDataChangeListeners();//Notificando os lsiteners, 
            Utils.currentStage(event).close();//Fechando a tela
        } catch (ValidationException e) { //ValidationException lanca uma excecao se o TextFiled estiver vazioe  voce tentar salvar
            setErrorMessages(e.getErrors());
        } catch (DbException e) {
            e.printStackTrace();
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    //Metodo notify, vai emitir o evento dataChange para todos os listeners
    private void notifyDataChangeListeners() {//Para cada objeto dataChange
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    //Metodo que verifica se o textFiled esta vazio
    private Professor getFormData() {
        Professor obj = new Professor();

        ValidationException exception = new ValidationException("Validaation errors");

        obj.setIdnome(Utils.tryParseToInt(txtIdNome.getText()));

        //If para verificar se o TextFild Nome esta vazio
        if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
            exception.addError("nome", "Field can't be empty");
        }
        obj.setNome(txtNome.getText());
        //If para verificar se o TextFild TITULACAO esta vazio
        if (txtTitulacao.getText() == null || txtTitulacao.getText().trim().equals("")) {
            exception.addError("titulacao", "Field can't be empty");
        }
        obj.setNome(txtTitulacao.getText());

        //Caso ocorra algum erro , lance a excessao
        if (exception.getErrors().size() > 0) {
            throw exception;
        }
        return obj;
    }

    //Metodo para tratar o botao cancel
    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();//Fechando a tela
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    //Metodo que recebe as validacoes
    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtIdNome); //So pode ser inteiro
        Constraints.setTextFieldMaxLength(txtNome, 30); //Colocando o limite de caracteres no TXT
        Constraints.setTextFieldMaxLength(txtTitulacao, 20); //Colocando o limite de caracteres no TXT
    }

    //Metodo responsavel para pegar os dados do Professor e popular a caixa de texto do formulario
    public void updateFormData() {
        //Testa se Entity esta nulo
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        txtIdNome.setText(String.valueOf(entity.getIdnome())); //Pega o ID digitado
        txtNome.setText(entity.getNome());//Pega o NOME digitado
        txtTitulacao.setText(entity.getTitulacao());//Pega a TITULACAO digitado
    }

    //Metodo para prencher a mensagen do erro na textLabel
    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        labelErrorName.setText((fields.contains("nome") ? errors.get("nome") : ""));
        labelErrorTitulacao.setText((fields.contains("titulacao") ? errors.get("titulacao") : ""));
    }

}
