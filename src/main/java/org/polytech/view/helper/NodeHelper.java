package org.polytech.view.helper;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
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

public class NodeHelper {

    public static Node instantiateNode(ScrollPane scroll, SystemMA system, int i, int j) {
        Rectangle rectangle = new Rectangle();

        // Quadrillage
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeType(StrokeType.INSIDE);
        // Automatic square size.
        rectangle.widthProperty().bind(scroll.widthProperty().divide(system.getEnvironnement().getNbColumns()));
        rectangle.heightProperty().bind(scroll.heightProperty().divide(system.getEnvironnement().getNbColumns()));

        // Remplissage selon le type d'entityé
        Movable entity = system.getEnvironnement().getEntity(i, j);
        if (entity != null) {
            if (entity instanceof Block) {
                if (((Block) entity).getValue() == BlockValue.A) rectangle.setFill(Color.BLUE);
                else if (((Block) entity).getValue() == BlockValue.B) rectangle.setFill(Color.RED);
            } else if (entity instanceof Agent) {
                rectangle.setFill(Color.WHITE);

                // On ajoute en plus un label désignant l'ID de l'agent
                Text text = new Text(((Agent) entity).getID().toString());

                // On intègre le tout dans un pane pour permettre l'alignement central
                StackPane pane = new StackPane();
                pane.getChildren().addAll(rectangle, text);
                StackPane.setAlignment(text, Pos.CENTER);
                return pane;
            }
        } else {
            rectangle.setFill(Color.WHITE);
        }

        return rectangle;
    }

}