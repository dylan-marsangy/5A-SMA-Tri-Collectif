package org.polytech.agent.strategies;

import org.polytech.agent.Agent;
import org.polytech.environment.Direction;
import org.polytech.environment.Movable;

import java.util.Map;

/**
 * Design pattern Strategy (externaliser la logique de traitements d'un agent dans des classes).
 */
public interface Strategy {

    Direction execute(Agent agent, Map<Direction, Movable> perception);

}
