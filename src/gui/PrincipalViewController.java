package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("onMenuItemDepartamentoAcao");
	}
	
	@FXML
	public void onMenuItemSobreAcao() {
		System.out.println("onMenuItemSobreAcao");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {		
	}
}
