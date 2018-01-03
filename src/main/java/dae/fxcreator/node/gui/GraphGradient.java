/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.gui;

import java.awt.Color;

/**
 *
 * @author Koen
 */
public class GraphGradient {

    private String name;
    private Color c1;
    private Color c2;

    /**
     * Creates a new gradient with the first color white, and the second color black.
     * The name is set to undefined.
     */
    public GraphGradient() {
        this.name="undefined";
        this.c1 = new Color(Color.white.getRGB());
        this.c2 = new Color(Color.black.getRGB());
    }

    /**
     * Creates a new GraphGradient object with a name and 2 gradient color.
     * @param name the name for the GraphGradient object.
     * @param c1 the first color.
     * @param c2 the second color.
     */
    public GraphGradient(String name, Color c1, Color c2){
        this.name = name;
        this.c1 = c1;
        this.c2 = c2;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the c1
     */
    public Color getC1() {
        return c1;
    }

    /**
     * @param c1 the c1 to set
     */
    public void setC1(Color c1) {
        this.c1 = c1;
    }

    /**
     * @return the c2
     */
    public Color getC2() {
        return c2;
    }

    /**
     * @param c2 the c2 to set
     */
    public void setC2(Color c2) {
        this.c2 = c2;
    }

    private String expand(String toExpand){
        if ( toExpand.length() == 1)
            return "0"+toExpand;
        else
            return toExpand;
    }

    public String getAsString(){
         String r1 = expand(Integer.toHexString(c1.getRed()));
        String g1 = expand(Integer.toHexString(c1.getGreen()));
        String b1 = expand(Integer.toHexString(c1.getBlue()));
        String r2 = expand(Integer.toHexString(c2.getRed()));
        String g2 = expand(Integer.toHexString(c2.getGreen()));
        String b2 = expand(Integer.toHexString(c2.getBlue()));

        return "[#"+r1+g1+b1+",#"+r2+g2+b2+"]";
    }

    @Override
    public String toString(){
        return name;
        /**
        String r1 = expand(Integer.toHexString(c1.getRed()));
        String g1 = expand(Integer.toHexString(c1.getGreen()));
        String b1 = expand(Integer.toHexString(c1.getBlue()));
        String r2 = expand(Integer.toHexString(c2.getRed()));
        String g2 = expand(Integer.toHexString(c2.getGreen()));
        String b2 = expand(Integer.toHexString(c2.getBlue()));

        return "[#"+r1+g1+b1+",#"+r2+g2+b2+"]";
         * */
    }

}
