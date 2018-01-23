package dae.fxcreator.node;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.tree.TreeNode;

/**
 * The definition of a field inside the shader struct.
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ShaderField implements Cloneable, TreeNode {

    public static boolean nameChanged(ShaderField field, ShaderField oldValue) {
        return !field.name.equals(oldValue.name);
    }

    public static boolean semanticChanged(ShaderField field, ShaderField oldValue) {
        return !field.semantic.equals(oldValue.semantic);
    }
    /**
     * The name of the shader field.
     */
    private String name;
    /**
     * The semantic of the shader field.
     */
    private final Semantic semantic = new Semantic();
    /**
     * The type of the shader field.
     */
    private ShaderType type;
    /**
     * The ShaderStruct that is the parent of this ShaderField.
     */
    private ShaderStruct parent;
    /**
     * A pattern for the string representation of this field.
     */
    private static final Pattern VALUE_PATTERN = Pattern.compile("(.*?)\\s([^\\:]*?)(?:\\:(.*?))?");

    /**
     * Creates a new ShaderField object.
     * @param name the name for the shaderfield.
     * @param semantic the semantic for the shaderfield.
     * @param type the type of the shaderfield.
     */
    public ShaderField(String name, String semantic, ShaderType type) {
        this.name = name;
        this.semantic.setValue(semantic);
        this.type = type;
    }

    /**
     * Returns a clone of this object.
     *
     * @return the clone of this object.
     * @throws java.lang.CloneNotSupportedException if clone is not supported.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Set the ShaderStruct object that is the parent of this struct object.
     *
     * @param parent the parent of this ShaderField object.
     */
    public void setParent(ShaderStruct parent) {
        this.parent = parent;
    }

    /**
     * Returns the name of this shader field.
     *
     * @return the name of the shader field.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the shader field.
     *
     * @param name the name of the shader field.
     */
    public void setName(String name) {
        if (!name.equals(this.name)) {
            if (parent != null) {
                try {
                    ShaderField oldValue = (ShaderField) clone();
                    this.name = name;
                    parent.notifyFieldChanged(this, oldValue);
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(ShaderField.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                this.name = name;
            }
        }
    }

    /**
     * Returns the semantic of this shader field.
     *
     * @return the semantic of the shader field.
     */
    public Semantic getSemantic() {
        return semantic;
    }

    /**
     * Sets the semantic of this shader field.
     *
     * @param semantic the new semantic value.
     */
    public void setSemantic(String semantic) {
        if (!this.semantic.eq(semantic)) {
            if (parent != null) {
                try {

                    ShaderField oldValue = (ShaderField) clone();
                    this.semantic.setValue(semantic);
                    parent.notifyFieldChanged(this, oldValue);
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(ShaderField.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                this.semantic.setValue(semantic);
            }
        }
    }

    /**
     * Returns the type of the shader field.
     *
     * @return the type.
     */
    public ShaderType getType() {
        return type;
    }

    /**
     * Sets the type of the shader field.
     *
     * @param type the new type for the shader field.
     */
    public void setType(ShaderType type) {
        if (this.type != type) {
            if (parent != null) {
                try {

                    ShaderField oldValue = (ShaderField) clone();
                    this.type = type;
                    parent.notifyFieldChanged(this, oldValue);

                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(ShaderField.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                this.type = type;
            }
        }
    }

    /**
     * Returns the child at a specific position.
     *
     * @param childIndex the index of the child.
     * @return always null, because this is a tree node.
     */
    @Override
    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    /**
     * Returns the number of children of this node.
     *
     * @return always 0, this is a leaf node.
     */
    @Override
    public int getChildCount() {
        return 0;
    }

    /**
     * Returns the ShaderStruct object this ShaderField belongs to.
     *
     * @return the TreeNode object that is the parent.
     */
    @Override
    public TreeNode getParent() {
        return this.parent;
    }

    /**
     * Returns the index of a TreeNode object.
     *
     * @param node the index of the node object.
     * @return -1, this node does not support child objects.
     */
    @Override
    public int getIndex(TreeNode node) {
        return -1;
    }

    /**
     * Checks if this node supports children.
     *
     * @return always false.
     */
    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    /**
     * Checks if this node is a leaf node.
     *
     * @return always true.
     */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /**
     * Not supported because Enumeration objects are ancient evils.
     *
     * @return always null.
     */
    @Override
    public Enumeration children() {
        return null;
    }

    /**
     * Return a String representation of this field.
     *
     * @return a String representation of this field.
     */
    @Override
    public String toString() {
        String result = getType() + " " + getName();
        if (semantic.isValid()) {
            result += ":" + getSemantic().getValue();
        }
        return result;
    }

    /**
     * Delete a field from the struct.
     *
     */
    public void deleteFromStruct() {
        if (parent != null) {
            parent.removeShaderField(this);
        }
    }

    /**
     * Decodes field values in the format : type name<:semantic>
     *
     * @param value the string to decode.
     * @return true if the value has the correct format, false otherwise.
     */
    public boolean decode(String value) {
        Matcher m = VALUE_PATTERN.matcher(value);
        if (m.matches()) {
            if (m.groupCount() > 1) {
                String dtype = m.group(1);
                String dname = m.group(2);
                try {
                    ShaderType dst = parent.typeLibrary.getType(dtype);
                    this.setType(dst);
                } catch (IllegalArgumentException ex) {
                }
                this.setName(dname);
            }
            switch (m.groupCount()) {
                case 2:
                    return true;
                case 3:
                    String dsemantic = m.group(3);
                    this.setSemantic(dsemantic);
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}
