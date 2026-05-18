package pt.isec.pa.deepsea.model.data;

public class Water extends Obstacle{
    private static final long serialVersionUID = 1L;
    public Water() {
        super();
    }

    @Override
    public boolean interact(Drone drone) {
        return false;
    }

    @Override
    public boolean isEmpty() {return true;}

    @Override
    public String getImageName() {
        return "water.png";
    }
}
