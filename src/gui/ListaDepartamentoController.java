package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.entidades.Departamento;
import modelo.servicos.DepartamentoServico;

public class ListaDepartamentoController implements Initializable{
	
	private DepartamentoServico servicoDep;
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML
	private TableColumn<Departamento, Integer> colunaId;
	
	@FXML
	private TableColumn<Departamento, String> colunaNome;
	
	@FXML
	private Button btNovo;
	
	private ObservableList<Departamento> obsLista;
	
	@FXML
	public void onBtNovoAcao() {
		System.out.println("Botao clicado!!");
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
		if(servicoDep == null) {
			throw new IllegalStateException("Serviço estava nulo");
		}
		List<Departamento> lista = servicoDep.busqueTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewDepartamento.setItems(obsLista);
	}
	
}
