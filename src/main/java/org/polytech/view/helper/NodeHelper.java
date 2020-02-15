package org.polytech.view.helper;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import org.polytech.agent.Agent;
import org.polytech.environment.Movable;
import org.polytech.environment.block.Block;
import org.polytech.environment.block.BlockValue;
import org.polytech.system.SystemMA;

/**
 * Classe utilitaire pour l'instanciations d'objets {@link Node} afin de remplir la grille de la vue JavaFX.
 */
public class NodeHelper {

    //TODO: Appliquer le style depuis le CSS plutôt que dans le code
    public static Node instantiateNode(Pane parent, SystemMA system, int i, int j) {
        Rectangle rectangle = new Rectangle();

        // Quadrillage
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeType(StrokeType.INSIDE);
        // Automatic square size.
        rectangle.widthProperty().bind(parent.widthProperty().divide(system.getEnvironment().getNbColumns()));
        rectangle.heightProperty().bind(parent.heightProperty().divide(system.getEnvironment().getNbColumns()));

        // Remplissage selon le type d'entityé
        Movable entity = system.getEnvironment().getEntity(i, j);
        if (entity != null) {
            if (entity instanceof Block) {
                if (((Block) entity).getValue() == BlockValue.A) rectangle.setFill(Color.BLUE);
                else if (((Block) entity).getValue() == BlockValue.B) rectangle.setFill(Color.RED);
            } else if (entity instanceof Agent) {
                rectangle.setFill(Color.WHITE);

                Agent agent = (Agent) entity;
                // On ajoute en plus un label désignant l'ID de l'agent
                Text text = new Text(((Agent) entity).getID().toString());
                text.setFill(
                        agent.isHolding() ?
                                agent.getHolding().getValue() == BlockValue.A ?
                                        Color.BLUE
                                        : Color.RED
                                : Color.BLACK);

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
