package org.polytech.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import org.polytech.view.helper.AlertHelper;
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
    private Button startButton;

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTask();
        initGrid();

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
                clean();
                initGrid();
            }
        }));
    }

    private void initGrid() {
        Node node;
        for (int i = 0; i < task.getSystem().getEnvironment().getNbRows(); i++) {
            for (int j = 0; j < task.getSystem().getEnvironment().getNbColumns(); j++) {
                // Instantie un node selon le contenu de la case dans l'environnement
                node = NodeHelper.instantiateNode(root, task.getSystem(), j, i);

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

    @FXML
    public void start(ActionEvent mouseEvent) {
        // Autoriser le clic sur le bouton une seule fois.
        startButton.setDisable(true);

        try {
            new Thread(task).start();
        } catch (Exception e) {
            Window owner = startButton.getScene().getWindow();
            AlertHelper.showAlertError(owner, e.getMessage());
        }
    }
}

