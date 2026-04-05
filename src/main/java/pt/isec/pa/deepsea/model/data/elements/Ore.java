package pt.isec.pa.deepsea.model.data.elements;

public class Ore extends GridElement {
    private final int quantity;

    public Ore(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

}