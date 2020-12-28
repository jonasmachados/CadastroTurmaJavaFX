package application;

import db.DbException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import listeners.DataChangeListener;
import model.entities.Turma;
import model.exceptions.ValidationException;
import model.services.TurmaService;
import util.Alerts;
import util.Constraints;
import util.Utils;

/**
 * FXML Controller class
 *
 * @author Jonas create 26/12/2020
 */
public class TurmaFormController implements Initializable {

    //Atributos
    private Turma entity;

    private TurmaService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtSala;

    @FXML
    private DatePicker txtDataAbertura;

    @FXML
    private DatePicker txtDataFechamento;

    @FXML
    private Label labelErrorSala;

    @FXML
    private Label labelErrorDataAbertura;

    @FXML
    private Button btSalve;

    @FXML
    private Button btCancel;

    //SET PARA O ALUNO SERVICE
    public void setTurmaService(TurmaService service) {
        this.service = service;
    }

    //Metodo que adiciona um novo obejto na lista, para isso a classe que recebe o evento deve implementar a interface dataChageListener
    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    //Implementando metodo set do entity 
    public void setTurma(Turma entity) {
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
    private Turma getFormData() {
        Turma obj = new Turma();

        ValidationException exception = new ValidationException("Validaation errors");

        obj.setCodigo(Utils.tryParseToInt(txtCodigo.getText()));

        //If para verificar se o TextFild Nome esta vazio
        if (txtSala.getText() == null || txtSala.getText().trim().equals("")) {
            exception.addError("sala", "Field can't be empty");
        }
        obj.setSala(txtSala.getText());
        
//        if (txtDataAbertura.getValue() == null) {
//            exception.addError("dataAbertura", "Field can't be empty");
//        } else {
//            Instant instant = Instant.from(txtDataAbertura.getValue().atStartOfDay(ZoneId.systemDefault()));//Pegar o valor de um Date Picker
//            obj.setDataAbertura(Date.from(instant));
//        }
//        
//        if (txtDataFechamento.getValue() == null) {
//            exception.addError("dataFechamento", "Field can't be empty");
//        } else {
//            Instant instant = Instant.from(txtDataFechamento.getValue().atStartOfDay(ZoneId.systemDefault()));//Pegar o valor de um Date Picker
//            obj.setDataFechamento(Date.from(instant));
//        }

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
        Constraints.setTextFieldInteger(txtCodigo); //So pode ser inteiro
        Constraints.setTextFieldMaxLength(txtSala, 30); //Colocando o limite de caracteres no TXT
//        Utils.formatDatePicker(txtDataAbertura, "dd/MM/yyyy");
//        Utils.formatDatePicker(txtDataFechamento, "dd/MM/yyyy");
        
    }

    //Metodo responsavel para pegar os dados do Aluno e popualar a caixa de texto do formulario
    public void updateFormData() {
        //Testa se Entily esta nulo
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        txtCodigo.setText(String.valueOf(entity.getCodigo())); //Pega o ID digitado
        txtSala.setText(entity.getSala());//Pega o sala digitado
        if (entity.getDataAbertura() != null) {
            txtDataAbertura.setValue(LocalDate.ofInstant(entity.getDataAbertura().toInstant(), ZoneId.systemDefault()));
        }
//        if (entity.getDataFechamento()!= null) {
//            txtDataFechamento.setValue(LocalDate.ofInstant(entity.getDataFechamento().toInstant(), ZoneId.systemDefault()));
//        }
    }

    //Metodo para prencher a mensagen do erro na textLabel
    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("sala")) {
            //labelErrorName.setText(errors.get("sala"));
        }
    }

}
