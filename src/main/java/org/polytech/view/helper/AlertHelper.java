package org.polytech.view.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

/**
 * Classe utilitaire pour l'affichage d'alertes JavaFX.
 */
public class AlertHelper {

    /**
     * Affiche une alerte.
     *
     * @param alertType  Type d'alerte
     * @param owner      Fenêtre dans laquelle afficher l'erreur
     * @param title      Titre de l'alerte
     * @param headerText En-tête de l'alerte
     * @param message    Message de l'alerte
     * @return Boutton de l'alerte éventuellement cliquée par l'utilisateur
     */
    public static Optional<ButtonType> showAlert(Alert.AlertType alertType, Window owner, String title, String headerText, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.initOwner(owner);
        return alert.showAndWait();
    }

    /**
     * Affiche une alerte d'erreur.
     *
     * @param owner   Fenêtre dans laquelle afficher l'erreur
     * @param title   Titre de l'alerte
     * @param message Message de l'alerte
     */
    public static void showError(Window owner, String title, String message) {
        showAlert(Alert.AlertType.ERROR, owner,
                title,
                "Une erreur imprévue est survenue...",
                message);
    }

    /**
     * Affiche une alerte de confirmation.
     *
     * @param owner   Fenêtre dans laquelle afficher l'erreur
     * @param title   Titre de l'alerte
     * @param message Message de l'alerte
     * @return True si la confirmation a été validée par l'utilisateur, false sinon
     */
    public static boolean showConfirmation(Window owner, String title, String message) {
        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, owner,
                title,
                "En êtes-vous sûr(e) ?",
                message);

        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
