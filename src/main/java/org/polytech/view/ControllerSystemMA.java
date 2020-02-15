package org.polytech.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import org.polytech.system.SystemMA;
import org.polytech.view.helper.AlertHelper;
import org.polytech.view.helper.ImageWriterHelper;
import org.polytech.view.helper.NodeHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    public ProgressBar progressBar;

    @FXML
    private Button startButton;

    @FXML
    public Button saveButton;

    @FXML
    public Button cancelButton;



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
        // Instanciation de la tâche
        task = new TaskSystemMA();

        // Configuration de la tâche
        task.valueProperty().addListener(((observable, oldValue, newValue) -> {
            // A chaque fois que la tâche envoie une notification, mise à jour de l'UI
            if (newValue != null) {
                refresh(newValue);
            }
        }));

        progressBar.progressProperty().bind(task.progressProperty());

        task.setOnSucceeded((event) ->  {
            startButton.setDisable(false);
            cancelButton.setDisable(true);
        });
    }

    private void initGrid(SystemMA system) {
        Node node;
        for (int i = 0; i < system.getEnvironment().getNbRows(); i++) {
            for (int j = 0; j < system.getEnvironment().getNbColumns(); j++) {
                // Instantie un node selon le contenu de la case dans l'environnement
                node = NodeHelper.instantiateNode(root, system, j, i);

                // Index du noeud dans la grille
                GridPane.setRowIndex(node, i);
                GridPane.setColumnIndex(node, j);

                grid.add(node, i, j);
            }
        }
    }

    // ACTION ----------------------------------------------------------------------------------------------------------

    public void clean() {
        grid.getChildren().clear();
    }

    private void refresh(SystemMA system) {
        clean();
        initGrid(system);
    }

    @FXML
    public void start(ActionEvent mouseEvent) {
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
    public void cancel(ActionEvent actionEvent) {
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
    public void save(ActionEvent actionEvent) {
        Window owner = saveButton.getScene().getWindow();
        ImageWriterHelper.save(grid, owner);
    }
}

