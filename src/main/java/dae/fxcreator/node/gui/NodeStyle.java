/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.node.gui;

/**
 * Contains the visual style for a node.
 * @author Koen
 */
public class NodeStyle {
    private String name;
    private String gradientName;
    private String titleFontName;
    private String semanticFontName;
    private String portFontName;

    /**
     * Creates a new NodeStyle object.
     */
    public NodeStyle(String name){
        this.name = name;
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
     * @return the gradientName
     */
    public String getGradientName() {
        return gradientName;
    }

    /**
     * @param gradientName the gradientName to set
     */
    public void setGradientName(String gradientName) {
        this.gradientName = gradientName;
    }

    /**
     * @return the titleFontName
     */
    public String getTitleFontName() {
        return titleFontName;
    }

    /**
     * @param titleFontName the titleFontName to set
     */
    public void setTitleFontName(String titleFontName) {
        this.titleFontName = titleFontName;
    }

    /**
     * @return the semanticFontName
     */
    public String getSemanticFontName() {
        return semanticFontName;
    }

    /**
     * @param semanticFontName the semanticFontName to set
     */
    public void setSemanticFontName(String semanticFontName) {
        this.semanticFontName = semanticFontName;
    }

    /**
     * @return the portFontName
     */
    public String getPortFontName() {
        return portFontName;
    }

    /**
     * @param portFontName the portFontName to set
     */
    public void setPortFontName(String portFontName) {
        this.portFontName = portFontName;
    }

    @Override
    public String toString(){
        return name;
    }
    
}
