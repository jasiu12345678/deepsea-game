package pt.isec.pa.deepsea.model.data.elements;

public class OceanCurrent extends Obstacle {
    public OceanCurrent() {
        super();
    }
    public int damage() {
        return 0;
    }
    public int fuelMultiplier() {
        return 2;
    }
    public ElementType getType() {return ElementType.CURRENT;}
}