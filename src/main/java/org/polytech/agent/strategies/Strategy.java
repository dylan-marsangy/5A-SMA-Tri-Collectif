package org.polytech.agent.strategies;

import org.polytech.agent.Agent;
import org.polytech.environnement.Direction;
import org.polytech.environnement.Movable;

import java.util.Map;

/**
 * Design pattern Strategy (externaliser la logique de traitements d'un agent dans des classes).
 */
public interface Strategy {

    Direction execute(Agent agent, Map<Direction, Movable> perception);

}
