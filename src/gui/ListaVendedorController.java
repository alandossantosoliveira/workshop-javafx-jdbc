package gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DadoAlteradoListener;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.entidades.Vendedor;
import modelo.servicos.VendedorServico;

public class ListaVendedorController implements Initializable, DadoAlteradoListener {

	private VendedorServico servicoDep;

	@FXML
	private TableView<Vendedor> tableViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> colunaId;

	@FXML
	private TableColumn<Vendedor, String> colunaNome;

	@FXML
	private TableColumn<Vendedor, Vendedor> colunaEditar;

	@FXML
	private TableColumn<Vendedor, Vendedor> colunaExcluir;

	@FXML
	private Button btIncluir;

	private ObservableList<Vendedor> obsLista;

	@FXML
	public void onBtIncluirAcao(ActionEvent evento) {
		Stage paiStage = Utils.stageAtual(evento);
		Vendedor obj = new Vendedor();
		criarDialogForm(obj, "/gui/VendedorForm.fxml", paiStage);
	}

	public void setVendedorServico(VendedorServico servicoDep) {
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
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void atualizaTableView() {
		
		if (servicoDep == null) {
			throw new IllegalStateException("Servi�o estava nulo");
		}
		List<Vendedor> lista = servicoDep.busqueTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewVendedor.setItems(obsLista);		
		
		iniciaEditButtons();
		iniciaRemoveButtons();
	}

	private void criarDialogForm(Vendedor obj, String nomeAbsolutoTela, Stage paiStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsolutoTela));
//			Pane pane = loader.load();
//
//			VendedorFormController controller = loader.getController();
//			controller.setVendedor(obj);
//			controller.setVendedorServico(new VendedorServico());
//			controller.subscreverDadoAlteradoListener(this);
//			controller.atualizaDadosForm();
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Entre com os dados do departamento");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(paiStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//		} catch (IOException e) {
//			Alertas.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDadoAlterado() {
		atualizaTableView();
	}

	private void iniciaEditButtons() {
		colunaEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaEditar.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarDialogForm(obj, "/gui/VendedorForm.fxml", Utils.stageAtual(event)));
			}
		});
	}

	private void iniciaRemoveButtons() {
		colunaExcluir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaExcluir.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("excluir");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removerEntidade(obj));
			}
		});
	}

	protected void removerEntidade(Vendedor obj) {
		Optional<ButtonType> result = Alertas.showConfirmation("Confirma��o", "Tem certeza que quer deletar?");
		
		if(result.get() == ButtonType.OK) {
			if (servicoDep == null) {
				throw new IllegalStateException("Servi�o estava nulo");
			}
			try {
				servicoDep.remover(obj);
				atualizaTableView();
			}catch(DbIntegrityException e) {
				Alertas.showAlert("Erro remover", null, e.getMessage(), AlertType.ERROR);
			}			
		}
	}

}