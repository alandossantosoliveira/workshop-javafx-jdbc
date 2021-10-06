package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alertas;
import gui.util.Limitadores;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.entidades.Departamento;
import modelo.servicos.DepartamentoServico;

public class DepartamentoFormController implements Initializable {

	private Departamento entidade;

	private DepartamentoServico servico;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private Label labelErroNome;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}

	public void setDepartamentoServico(DepartamentoServico servico) {
		this.servico = servico;
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
			Utils.stageAtual(evento).close();
		} catch (DbException e) {
			Alertas.showAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private Departamento getDadosForm() {
		Departamento obj = new Departamento();

		obj.setId(Utils.tentaParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());

		return obj;
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
		Limitadores.setTextFieldMaxLength(txtNome, 30);
	}

	public void atualizaDadosForm() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade está vazio");
		} else {
			txtId.setText(String.valueOf(entidade.getId()));
			txtNome.setText(entidade.getNome());
		}

	}

}
