package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class PrincipalViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	public void onMenuItemVendedorAcao() {
		System.out.println("onMenuItemVendedorAcao");
	}
	
	@FXML
	public void onMenuItemDepartamentoAcao() {
		carregarView("/gui/ListaDepartamento.fxml");
	}
	
	@FXML
	public void onMenuItemSobreAcao() {
		carregarView("/gui/Sobre.fxml");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {		
	}
	
	private synchronized void carregarView(String nomeAbsoluto) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			VBox newVbox = loader.load();
			
			Scene cenaPrincipal = Main.getCenaPrincipal();
			VBox mainVBox = (VBox) ((ScrollPane) cenaPrincipal.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
		    mainVBox.getChildren().clear();
		    mainVBox.getChildren().add(mainMenu);
		    mainVBox.getChildren().addAll(newVbox.getChildren());
					
		}catch(IOException e) {
			Alertas.showAlert("IO Exception", "Erro ao carregar view", e.getMessage(), AlertType.ERROR);
		}
		
	}
}
