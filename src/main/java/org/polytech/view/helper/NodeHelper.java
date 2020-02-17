package org.polytech.view.helper;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.polytech.agent.Agent;
import org.polytech.environment.Movable;
import org.polytech.environment.block.Block;
import org.polytech.environment.block.BlockValue;
import org.polytech.system.SystemMA;

/**
 * Classe utilitaire pour l'instanciations d'objets {@link Node} afin de remplir la grille de la vue JavaFX.
 */
public class NodeHelper {

    public static Node instantiateNode(Pane parent, SystemMA system, int i, int j) {
        Rectangle rectangle = new Rectangle();
        rectangle.getStyleClass().add("case");

        // Automatic square size.
        rectangle.widthProperty().bind(parent.widthProperty().divide(system.getEnvironment().getNbColumns()));
        rectangle.heightProperty().bind(parent.heightProperty().divide(system.getEnvironment().getNbColumns()));

        // Remplissage selon le type d'entityé
        Movable entity = system.getEnvironment().getEntity(i, j);
        if (entity != null) {
            if (entity instanceof Block) {
                if (((Block) entity).getValue() == BlockValue.A) rectangle.getStyleClass().add("blockA");
                else if (((Block) entity).getValue() == BlockValue.B) rectangle.getStyleClass().add("blockB");
            } else if (entity instanceof Agent) {
                rectangle.getStyleClass().add("agent");
                if (((Agent) entity).isHolding()) {
                    if (((Agent) entity).getHolding().getValue() == BlockValue.A) {
                        rectangle.getStyleClass().add("holdingA");
                    } else if (((Agent) entity).getHolding().getValue() == BlockValue.B) {
                        rectangle.getStyleClass().add("holdingB");
                    }
                }
            }
        } else {

        }


        return rectangle;
    }

}
