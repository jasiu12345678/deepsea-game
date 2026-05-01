package pt.isec.pa.deepsea.model.data.elements;

public class Monster extends Obstacle {
    public Monster() {
        super();
    }
    public int damage() {
        return 5;
    }
    public int fuelMultiplier() {
        return 1;
    }
    public ElementType getType() {return ElementType.MONSTER;}
}
