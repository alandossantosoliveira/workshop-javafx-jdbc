package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DadoAlteradoListener;
import gui.util.Alertas;
import gui.util.Limitadores;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import modelo.entidades.Departamento;
import modelo.entidades.Vendedor;
import modelo.excessoes.ValidacaoExcessao;
import modelo.servicos.DepartamentoServico;
import modelo.servicos.VendedorServico;

public class VendedorFormController implements Initializable {

	private Vendedor entidade;

	private VendedorServico servico;

	private DepartamentoServico depServico;

	private List<DadoAlteradoListener> dadoAlteradoListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private Label labelErroNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpDataNascimento;

	@FXML
	private TextField txtSalarioBase;

	@FXML
	private Label labelErroEmail;

	@FXML
	private Label labelErroDataNascimento;

	@FXML
	private Label labelErroSalarioBase;

	@FXML
	private ComboBox<Departamento> comboBoxDepartamento;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	private ObservableList<Departamento> obsList;

	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}

	public void setServicos(VendedorServico servico, DepartamentoServico depServico) {
		this.servico = servico;
		this.depServico = depServico;
	}

	public void subscreverDadoAlteradoListener(DadoAlteradoListener listener) {
		dadoAlteradoListener.add(listener);
	}

	@FXML
	public void onBtSalvarAcao(ActionEvent evento) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade estava nulo");
		}
		if (servico == null) {
			throw new IllegalStateException("Serviço estava vazio");
		}
		try {
			entidade = getDadosForm();
			servico.salvarOuAtualizar(entidade);
			notificaDadoAlteradoListener();
			Utils.stageAtual(evento).close();
		} catch (DbException e) {
			Alertas.showAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidacaoExcessao e) {
			setErroMenssagens(e.getErros());
		}

	}

	private void notificaDadoAlteradoListener() {
		for (DadoAlteradoListener listener : dadoAlteradoListener) {
			listener.onDadoAlterado();
		}
	}

	private Vendedor getDadosForm() {
		Vendedor obj = new Vendedor();

		ValidacaoExcessao excessao = new ValidacaoExcessao("Validação erro");

		obj.setId(Utils.tentaParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excessao.addErro("nome", "Campo nome vazio");
		}
		obj.setNome(txtNome.getText());
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			excessao.addErro("email", "Campo email inválido");
		}
		obj.setEmail(txtEmail.getText());
		
		if(dpDataNascimento.getValue() == null) {
			excessao.addErro("dataNascimento", "Campo data nascimento inválido");
		}else {
			Instant instant = Instant.from(dpDataNascimento.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataNascimento(Date.from(instant));
		}
		
		if (txtSalarioBase.getText() == null || txtSalarioBase.getText().trim().equals("")) {
			excessao.addErro("salario", "Campo salario inválido");
		}
		obj.setSalarioBase(Utils.tentaParseToDouble( txtSalarioBase.getText()));
		
		if (excessao.getErros().size() > 0) {
			throw excessao;
		}
		obj.setDepartamento(comboBoxDepartamento.getValue());

		return obj;
	}

	public void carregaObjetosAssociados() {
		if (depServico == null) {
			throw new IllegalStateException("Serviço departamento estava nulo");
		}
		List<Departamento> list = depServico.busqueTodos();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartamento.setItems(obsList);
	}

	@FXML
	public void onBtCancelarAcao(ActionEvent evento) {
		Utils.stageAtual(evento).close();
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		inicializarNodes();
	}

	private void inicializarNodes() {
		Limitadores.setTextFieldInteger(txtId);
		Limitadores.setTextFieldMaxLength(txtNome, 70);
		Limitadores.setTextFieldDouble(txtSalarioBase);
		Limitadores.setTextFieldMaxLength(txtEmail, 70);
		Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");
		
		initializeComboBoxDepartamento();
	}

	public void atualizaDadosForm() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade está vazio");
		} else {
			txtId.setText(String.valueOf(entidade.getId()));
			txtNome.setText(entidade.getNome());
			txtEmail.setText(entidade.getEmail());
			Locale.setDefault(Locale.US);
			txtSalarioBase.setText(String.format("%.2f", entidade.getSalarioBase()));
			if (entidade.getDataNascimento() != null) {
				dpDataNascimento.setValue(
						LocalDate.ofInstant(entidade.getDataNascimento().toInstant(), ZoneId.systemDefault()));
			}
		}
		
		if(entidade.getDepartamento() == null) {
			comboBoxDepartamento.getSelectionModel().selectFirst();
		}else {
			comboBoxDepartamento.setValue(entidade.getDepartamento());
		}		
	}

	private void setErroMenssagens(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		labelErroNome.setText(campos.contains("nome") ? erros.get("nome") : "");		
		labelErroEmail.setText(campos.contains("email") ? erros.get("email") : "");		
		labelErroSalarioBase.setText(campos.contains("salario") ? erros.get("salario") : "");		
		labelErroDataNascimento.setText(campos.contains("dataNascimento") ? erros.get("dataNascimento") : "");
	}

	private void initializeComboBoxDepartamento() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}

}
