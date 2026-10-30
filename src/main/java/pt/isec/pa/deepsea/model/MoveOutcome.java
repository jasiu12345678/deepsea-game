package pt.isec.pa.deepsea.model;

/**
 * Result of the last drone movement, so views can react (for example with
 * sound effects) without having to inspect or re-derive the game rules.
 */
public enum MoveOutcome {
    NONE, MOVED, HIT_OBSTACLE, HIT_CREATURE, DRONE_DESTROYED
}
