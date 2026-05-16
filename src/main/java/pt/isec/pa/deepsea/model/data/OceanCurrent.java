package pt.isec.pa.deepsea.model.data;

public class OceanCurrent extends Obstacle {
    public OceanCurrent() {
        super();
    }
    @Override
    public boolean interact(Drone drone) {
        if (drone != null) {
            drone.fuelMultuplier(2);
            return true;
        }
        return false;
    }
}