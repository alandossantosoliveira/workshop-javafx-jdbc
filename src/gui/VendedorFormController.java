package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DadoAlteradoListener;
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
import modelo.entidades.Vendedor;
import modelo.excessoes.ValidacaoExcessao;
import modelo.servicos.VendedorServico;

public class VendedorFormController implements Initializable {

	private Vendedor entidade;

	private VendedorServico servico;
	
	private List<DadoAlteradoListener> dadoAlteradoListener = new ArrayList<>();

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

	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}

	public void setVendedorServico(VendedorServico servico) {
		this.servico = servico;
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
		}catch(ValidacaoExcessao e) {
			setErroMenssagens(e.getErros());
			txtNome.requestFocus();
		}

	}

	private void notificaDadoAlteradoListener() {
		for(DadoAlteradoListener listener : dadoAlteradoListener) {
			listener.onDadoAlterado();
		}
	}

	private Vendedor getDadosForm() {
		Vendedor obj = new Vendedor();
		
		ValidacaoExcessao excessao = new ValidacaoExcessao("Validação erro");

		obj.setId(Utils.tentaParseToInt(txtId.getText()));
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excessao.addErro("nome", "Campo nome vazio");
		}
		obj.setNome(txtNome.getText());
		
		if(excessao.getErros().size() > 0) {
			throw excessao;
		}
		
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
	
	private void setErroMenssagens(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		if(campos.contains("nome")) {
			labelErroNome.setText(erros.get("nome"));
		}		
	}
}
