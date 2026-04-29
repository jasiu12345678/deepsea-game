package pt.isec.pa.deepsea.model.data.elements;

public class MarineAnimal extends Obstacle {
    public MarineAnimal() {
        super();
    }
    public int damage() {
        return 5;
    }
    public int fuelMultiplier() {
        return 1;
    }
    @Override
    public ElementType getType() {return ElementType.MARINE_ANIMAL;}
}
