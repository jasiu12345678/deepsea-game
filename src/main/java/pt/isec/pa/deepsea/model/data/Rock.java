package pt.isec.pa.deepsea.model.data;

public class Rock extends Obstacle{
    private final int damage = 5;

    public Rock() {
        super();
    }

    @Override
    public boolean interact(Drone drone) {
        drone.takeDamage();
        return true;
    }
}
