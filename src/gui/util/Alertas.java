package gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alertas {

	public static void showAlert(String titulo, String cabecalho, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(titulo);
		alert.setHeaderText(cabecalho);
		alert.setContentText(content);
		alert.show();
	}
}
