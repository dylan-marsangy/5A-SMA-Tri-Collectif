package org.polytech.view.helper;

import javafx.scene.control.Alert;
import javafx.stage.Window;

public class AlertHelper {

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String headerText, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public static void showAlertError(Window owner, String message) {
        showAlert(Alert.AlertType.ERROR, owner,
                "Erreur",
                "Une erreur imprévue est survenue...",
                message);
    }

}
