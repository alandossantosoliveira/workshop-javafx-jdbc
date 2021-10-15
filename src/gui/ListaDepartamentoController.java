package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DadoAlteradoListener;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.entidades.Departamento;
import modelo.servicos.DepartamentoServico;

public class ListaDepartamentoController implements Initializable, DadoAlteradoListener {

	private DepartamentoServico servicoDep;

	@FXML
	private TableView<Departamento> tableViewDepartamento;

	@FXML
	private TableColumn<Departamento, Integer> colunaId;

	@FXML
	private TableColumn<Departamento, String> colunaNome;

	@FXML
	private TableColumn<Departamento, Departamento> colunaEditar;

	@FXML
	private Button btIncluir;

	private ObservableList<Departamento> obsLista;

	@FXML
	public void onBtIncluirAcao(ActionEvent evento) {
		Stage paiStage = Utils.stageAtual(evento);
		Departamento obj = new Departamento();
		criarDialogForm(obj, "/gui/DepartamentoForm.fxml", paiStage);
	}

	public void setDepartamentoServico(DepartamentoServico servicoDep) {
		this.servicoDep = servicoDep;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializaNodes();
	}

	private void inicializaNodes() {
		colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		Stage stage = (Stage) Main.getCenaPrincipal().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}

	public void atualizaTableView() {
		if (servicoDep == null) {
			throw new IllegalStateException("Serviço estava nulo");
		}
		List<Departamento> lista = servicoDep.busqueTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewDepartamento.setItems(obsLista);
		iniciaEditeButtons();
	}

	private void criarDialogForm(Departamento obj, String nomeAbsolutoTela, Stage paiStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsolutoTela));
			Pane pane = loader.load();

			DepartamentoFormController controller = loader.getController();
			controller.setDepartamento(obj);
			controller.setDepartamentoServico(new DepartamentoServico());
			controller.subscreverDadoAlteradoListener(this);
			controller.atualizaDadosForm();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(paiStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alertas.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDadoAlterado() {
		atualizaTableView();
	}

	private void iniciaEditeButtons() {
		colunaEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaEditar.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Departamento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarDialogForm(obj, "/gui/DepartamentoForm.fxml", Utils.stageAtual(event)));
			}
		});
	}

}
