package pt.isec.pa.deepsea.model.data;

public class MarineAnimal extends Obstacle {
    public MarineAnimal() {
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
    @Override
    public String getImageName() {
        return "marineanimal.png";
    }
}
