package pt.isec.pa.deepsea.model.data;

/**
 * Kind of content a grid cell can have. It lets views decide how to render a
 * cell without knowing anything about the concrete model classes.
 */
public enum CellType {
    WATER, ROCK, ORE, ARTIFACT, MONSTER, MARINE_ANIMAL, CURRENT
}
