package dae.fxcreator.node;

/**
 * Describes a shader type that can be set as type for an input or output port.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ShaderType {

    /**
     * The type name
     */
    private final String type;
    /**
     * The icon for the type.
     */
    private final String icon;
    /**
     * The order of the type
     */
    private final int order;
    /**
     * Indicates that the shader type is an actual value.
     */
    private final boolean valueType;

    /**
     * Creates a new ShaderType object
     *
     * @param type the type
     * @param order the order for the type, for sorting
     */
    public ShaderType(String type, int order) {
        this(type, order, true, null);
    }

    /**
     * Creates a new ShaderType object
     *
     * @param type the type
     * @param order the order for the type, for sorting
     * @param valueType indicates that this shader type describes an actual
     * value that can set on a variable or returned as a result of a function.
     */
    public ShaderType(String type, int order, boolean valueType) {
        this(type, order, valueType, null);
    }

    /**
     * Creates a new ShaderType object
     *
     * @param type the type
     * @param order the order for the type, for sorting
     * @param valueType indicates that this shader type describes an actual
     * value that can set on a variable or returned as a result of a function.
     * @param icon the icon for this shader type.
     */
    public ShaderType(String type, int order, boolean valueType, String icon) {
        this.type = type;
        this.order = order;
        this.valueType = valueType;
        this.icon = icon;
    }

    /**
     * Returns the type of this ShaderType object.
     *
     * @return the type.
     */
    public String getType() {
        return type;
    }

    /**
     * Return the icon for this ShaderType object.
     *
     * @return the icon for the shadertype object.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return the order for the type.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Returns true if this shader type describes a value that can be set on an
     * object or can be returned as a result of a function, otherwise false.
     *
     * @return true if the type describes a value type, false otherwise.
     */
    public boolean isValueType() {
        return valueType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ShaderType other = (ShaderType) obj;
        return !((this.type == null) ? (other.type != null) : !this.type.equals(other.type));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }

    /**
     * Returns the string representation for this type.
     *
     * @return the String representation for this type.
     */
    @Override
    public String toString() {
        return this.type;
    }

}
