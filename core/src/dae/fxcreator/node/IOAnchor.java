package dae.fxcreator.node;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public enum IOAnchor {
    NORTHWEST("NW"), NORTHEAST("NE"), SOUTHWEST("SW"), SOUTHEAST("SE"), UNDEFINED("U");
    private final String shortDesc;

    IOAnchor(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }
}
