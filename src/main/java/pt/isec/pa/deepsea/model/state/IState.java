package pt.isec.pa.deepsea.model.state;

public interface IState {
    void handleGoTrench();
    void handleGoSeabed();
    void handleGoSurface();
    void handleStartMinigame();
    void handleGameOver();
    void handleOpenWorkshop();

    void entry();
    void exit();

    State getState();
}
