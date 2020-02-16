package org.polytech.view;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import org.polytech.system.SystemMA;
import org.polytech.view.helper.AlertHelper;
import org.polytech.view.helper.ImageWriterHelper;
import org.polytech.view.helper.NodeHelper;

import java.net.URL;
import java.util.ResourceBundle;

import static org.polytech.SMApplicationV1.*;

public class ControllerSystemMA implements Initializable {

    private TaskSystemMA task;

    @FXML
    private BorderPane root;

    @FXML
    private GridPane grid;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button startButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Slider sliderFrequency;


    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Statistiques sommaires du système
        System.out.println(String.format("Grille remplie à %.2f%% d'entités dont %.2f%% d'agents et %.2f%% de blocs.",
                (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B + NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                (double) (NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B) / (GRID_COLUMNS * GRID_COLUMNS) * 100));

        System.out.println();
    }

    private void initTask() {
        task = new TaskSystemMA();

        // A chaque fois que la tâche envoie une notification, mise à jour de l'UI
        task.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                refresh(newValue);
            }
        }));

        // A chaque nouvelle itération de l'algorithme, mise à jour de la bar de progression
        progressBar.progressProperty().bind(task.progressProperty());

        // Quand l'algorithme est terminé, on réinitialise les boutons de l'UI
        task.setOnSucceeded((event) -> {
            startButton.setDisable(false);
            cancelButton.setDisable(true);
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, grid.getScene().getWindow(),
                    "Exécution du système",
                    "Fin de simulation",
                    "L'exécution s'est terminée avec succès !");
        });

        // Mise à jour de la fréquence d'affichage selon l'input de l'utilisateur
        sliderFrequency.valueProperty().bindBidirectional(task.frequencyUpdateUIProperty());
    }

    private void initGrid(SystemMA system) {
        Node node;
        for (int i = 0; i < system.getEnvironment().getNbRows(); i++) {
            for (int j = 0; j < system.getEnvironment().getNbColumns(); j++) {
                // Instantie un node selon le contenu de la case dans l'environnement
                node = NodeHelper.instantiateNode(grid, system, j, i);

                // Index du noeud dans la grille
                GridPane.setRowIndex(node, i);
                GridPane.setColumnIndex(node, j);

                grid.add(node, i, j);
            }
        }
    }

    // ACTION ----------------------------------------------------------------------------------------------------------

    private void clean() {
        grid.getChildren().clear();
    }

    private void refresh(SystemMA system) {
        clean();
        initGrid(system);
    }

    @FXML
    private void start(ActionEvent mouseEvent) {
        progressBar.setVisible(true);
        sliderFrequency.setVisible(true);
        startButton.setDisable(true);
        cancelButton.setDisable(false);
        saveButton.setDisable(false);

        initTask();
        initGrid(task.getSystem());
        try {
            new Thread(task).start();
        } catch (Exception e) {
            Window owner = startButton.getScene().getWindow();
            AlertHelper.showError(owner, "Exécution de l'algorithme", e.getMessage());
        }
    }

    @FXML
    private void cancel(ActionEvent actionEvent) {
        startButton.setDisable(false);
        cancelButton.setDisable(true);

        try {
            task.cancel();
        } catch (Exception e) {
            Window owner = cancelButton.getScene().getWindow();
            AlertHelper.showError(owner, "Interruption de l'algorithme", e.getMessage());
        }
    }

    @FXML
    private void save(ActionEvent actionEvent) {
        Window owner = saveButton.getScene().getWindow();
        saveAsPng(owner);
    }

    /**
     * Sauvegarde la grille de l'environnement dans son état actuel dans un fichier png.
     *
     * @param owner Fenêtre dans laquelle éventuellement afficher une alerte d'erreur
     * @see ImageWriterHelper
     */
    private void saveAsPng(Window owner) {
        // Sauvegarde de l'image sur un Thread en background.
        Platform.runLater(new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ImageWriterHelper.save(grid, owner);
                return null;
            }
        }));
    }

}

