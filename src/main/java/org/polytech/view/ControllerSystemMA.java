package org.polytech.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import org.polytech.SMAConstants;
import org.polytech.system.SystemMA;
import org.polytech.view.helper.AlertHelper;
import org.polytech.view.helper.ImageWriterHelper;
import org.polytech.view.helper.NodeHelper;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import static org.polytech.SMAConstants.*;

public class ControllerSystemMA implements Initializable {

    private ServiceSystemMA service;

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

    @FXML
    private TextField inputIterations;

    // INITIALIZATION --------------------------------------------------------------------------------------------------

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initInputs();

        // Statistiques sommaires du système
        System.out.println(String.format("Grille remplie à %.2f%% d'entités dont %.2f%% d'agents et %.2f%% de blocs.",
                (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B + NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                (double) (NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B) / (GRID_COLUMNS * GRID_COLUMNS) * 100));

        System.out.println();
    }

    private void initService() {
        service = new ServiceSystemMA();

        // A chaque fois que le service envoie une notification de progression, mise à jour de la grille
        service.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                refresh(newValue);
            }
        }));

        // A chaque nouvelle itération de l'algorithme, mise à jour de la bar de progression
        progressBar.progressProperty().bind(service.progressProperty());

        // Quand l'algorithme est terminé, on réinitialise les boutons de l'UI
        service.setOnSucceeded((event) -> {
            startButton.setDisable(false);
            cancelButton.setDisable(true);
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, grid.getScene().getWindow(),
                    "Exécution du système",
                    "Fin de simulation",
                    "L'exécution s'est terminée avec succès !");
        });

        // Mise à jour de la fréquence d'affichage selon l'input de l'utilisateur
        sliderFrequency.valueProperty().bindBidirectional(service.frequencyUpdateUIProperty());

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

    private void initInputs() {
        // Autoriser uniquement les inputs de chiffres. Tout autre input sera ignoré.
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        IntegerStringConverter converter = new IntegerStringConverter();
        inputIterations.setTextFormatter(new TextFormatter<>(
                converter,
                SMAConstants.ITERATION_LOOPS,
                integerFilter));
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
        inputIterations.setVisible(true);
        startButton.setDisable(true);
        cancelButton.setDisable(false);
        saveButton.setDisable(false);

        initService();
        try {
            inputIterations.setText(Integer.toString(ITERATION_LOOPS));
            service.restart();
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
            service.cancel();
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

    @FXML
    public void onIterationChanged(ActionEvent inputMethodEvent) {
        // Mise à jour de nombre maximal d'itérations selon l'input de l'utilisateur
        service.setMaxIterations(Integer.parseInt(inputIterations.getText()));
    }
}

