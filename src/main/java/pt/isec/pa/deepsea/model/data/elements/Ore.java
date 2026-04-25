package pt.isec.pa.deepsea.model.data.elements;

public class Ore extends Collectible {
    private final int quantity;

    public Ore(int quantity) {
        super();
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

}