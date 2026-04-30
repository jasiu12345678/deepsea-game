package pt.isec.pa.deepsea.model.data.elements;

import pt.isec.pa.deepsea.model.data.actors.Drone;

public class Artifact extends Collectible {
    private static final long serialVersionUID = 1L;

    public Artifact() {
        super();
    }

    @Override
    public ElementType onInteract(Drone drone) {
        return ElementType.ARTIFACT;
    }
    @Override
    public ElementType getType() {return ElementType.ARTIFACT;}

}
