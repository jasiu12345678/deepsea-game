package pt.isec.pa.deepsea.model.data.grids;

import pt.isec.pa.deepsea.model.data.elements.GridElement;

public interface IGrid {
    int getWidth();
    int getHeight();
    GridElement getElement(int x, int y);

    default boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= getWidth() || y < 0 || y >= getHeight();
    }
}