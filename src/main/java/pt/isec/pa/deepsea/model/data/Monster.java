package pt.isec.pa.deepsea.model.data;

public class Monster extends Obstacle {
    public Monster() {
        super();
    }

    @Override
    public boolean interact(Drone drone) {
        if (drone != null) {
            drone.takeDamage();
            return true;
        }
        return false;
    }
}
