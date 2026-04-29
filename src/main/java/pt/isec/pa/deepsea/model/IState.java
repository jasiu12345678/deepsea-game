package pt.isec.pa.deepsea.model;

public interface IState {
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

    void entry();
    void exit();

    State getState();

}
