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
    public CellType getType() {
        return CellType.MARINE_ANIMAL;
    }
}
