package pt.isec.pa.deepsea.model;

import java.io.Serializable;

/**
 * Contract for each game state. Defines which actions are allowed in that part of the game.
 */
public interface IState extends Serializable {
    boolean navigate(Directions dir);
    boolean openWorkshop();
    boolean closeWorkshop();
    boolean repairDrone(int droneId);
    boolean refuelDrone(int droneId);
    boolean upgradeDrone(int droneId);
    boolean startDescent();
    boolean startAscent();
    boolean collectItem();
    boolean selectDrone(int droneId);
    boolean resolveMiniGame(boolean won);
    boolean confirmCollection(boolean confirm);

    void entry();
    void exit();

    State getState();

}
