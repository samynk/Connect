package dae.fxcreator.node;

/**
 * Defines where an input or output can be placed.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public enum IOAnchor {
    NORTHWEST("NW"), NORTHEAST("NE"), SOUTHWEST("SW"), SOUTHEAST("SE"), UNDEFINED("U");
    private final String shortDesc;

    /**
     * Creates the enum object with a short description.
     * @param shortDesc the short description of the game.
     */
    IOAnchor(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    /**
     * The short description of the game.
     * @return the short description of the game.
     */
    public String getShortDesc() {
        return shortDesc;
    }
}
