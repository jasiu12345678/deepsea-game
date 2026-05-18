package pt.isec.pa.deepsea.model.data;

public interface IGrid {
    int getWidth();
    int getHeight();
    boolean processInteraction(int x, int y, Drone drone);
    Collectible processCollection(int x, int y, Drone drone);
    void removeElement(int x, int y);
    default boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= getWidth() || y < 0 || y >= getHeight();
    }
    String getElementImage(int x, int y);
}