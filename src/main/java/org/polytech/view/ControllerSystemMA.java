package org.polytech.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import org.polytech.agent.Agent;
import org.polytech.environnement.Movable;
import org.polytech.environnement.block.Block;
import org.polytech.environnement.block.BlockValue;
import org.polytech.system.SystemMA;
import org.polytech.system.SystemMAFactory;
import org.polytech.view.helper.NodeHelper;

import java.net.URL;
import java.util.ResourceBundle;

import static org.polytech.SMApplicationV1.*;

public class ControllerSystemMA implements Initializable {

    private SystemMA system;

    @FXML
    public ScrollPane scroll;

    @FXML
    public GridPane grid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSystemMA();

        initGrid();
    }

    private void initSystemMA() {
        // Génération du système
        system = SystemMAFactory.instantiateRandom(
                GRID_ROWS, GRID_COLUMNS, NUMBER_AGENTS, NUMBER_BLOCKS_A, NUMBER_BLOCKS_B,
                SUCCESSIVE_MOVEMENTS, MEMORY_SIZE, K_PLUS, K_MINUS, ERROR);

        // Statistiques sommaires
        System.out.println(String.format("Grille remplie à %.2f%% d'entités dont %.2f%% d'agents et %.2f%% de blocs.",
                (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B + NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                (double) (NUMBER_AGENTS) / (GRID_COLUMNS * GRID_COLUMNS) * 100,
                (double) (NUMBER_BLOCKS_A + NUMBER_BLOCKS_B) / (GRID_COLUMNS * GRID_COLUMNS) * 100));

        System.out.println();
    }

    private void initGrid() {
        Node node;
        for (int i = 0; i < system.getEnvironnement().getNbRows(); i++) {
            for (int j = 0; j < system.getEnvironnement().getNbColumns(); j++) {
                node = NodeHelper.instantiateNode(scroll, system, i, j); // Instantie un node selon le contenu de la case dans l'environnement

                // Index
                GridPane.setRowIndex(node, i);
                GridPane.setColumnIndex(node, j);

                grid.add(node, i, j);
            }
        }
    }


}

