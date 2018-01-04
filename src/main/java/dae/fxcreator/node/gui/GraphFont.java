package dae.fxcreator.node.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Stores all the properties about a font, such as size, font family,
 * font styles and also the font metrics.
 * @author Koen
 */
public class GraphFont {

    private String name;

    private int size;
    private String fontFamily;
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private Color color;
    private FontMetrics fm;
    private Font f;

    /**
     * Creates a new GraphFont object
     * @param fontFamily the name for the font.
     * @param size the size of the font.
     * @param bold is the font bold or not.
     * @param italic is the font italic or not.
     * @param underlined is the font underlined.
     */
    public GraphFont(String name,String fontFamily, int size, boolean bold, boolean italic, boolean underlined, Color color) {
        this.name = name;
        this.fontFamily = fontFamily;
        this.size = size;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.color = color;
        createFont();
    }

    private void createFont(){
        int style = 0;
        if ( bold )
            style |= Font.BOLD;
        if (italic)
            style |= Font.ITALIC;
        f = new Font(fontFamily,style, size);
    }

    /**
     * Returns the color to use for this graph font.
     * @return the color to use.
     */
    public Color getColor(){
        return color;
    }

    /**
     * Returns the name of the font.
     * @return the name of the font.
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the font to use.
     * @return the font.
     */
    public Font getFont(){
        return f;
    }

    /**
     * Draws a text on the graphics object.
     * @param g the graphics object.
     * @param x the x coordinate to draw the text.
     * @param y the y coordinate to draw the text.
     * @param size the out parameter to store the width and height of the resulting
     * text object in.
     */
    public void drawText(Graphics g, String text, int x, int y, Dimension size){
        g.setFont(f);
        fm = g.getFontMetrics(f);
        size.width = fm.stringWidth(text);
        size.height =fm.getHeight();
        g.drawString(text, x, y+fm.getAscent());
    }

    @Override
    public String toString(){
        return name;
    }

    /**
     * Change the font family of this GraphFont object.
     * @param fontFamily the new font family of this GraphFont object.
     */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
        createFont();
    }

    /**
     * Returns the font family of this font.
     * @return the font family.
     */
    public String getFontFamily(){
        return fontFamily;
    }

    /**
     * Sets the font size of this GraphFont object.
     * @param size the new size for the font.
     */
    public void setFontSize(int size){
        this.size = size;
        createFont();
    }

    /**
     * Returns the font size of this GraphFont object.
     * @return the font size.
     */
    public int getFontSize(){
        return size;
    }

    /**
     * Sets the bold value for this font.
     * @param bold true if this font is bold, false otherwise.
     */
    public void setBold(boolean bold)
    {
        this.bold = bold;
        createFont();
    }

    /**
     * Checks if this font is bold.
     * @return true if the font is bold, false otherwise.
     */
    public boolean isBold(){
        return bold;
    }

    /**
     * Sets the italic value for this font.
     * @param italic true if this font is italic, false otherwise.
     */
    public void setItalic(boolean italic)
    {
        this.italic = italic;
        createFont();
    }

    /**
     * Checks if this font is italic.
     * @return true if the font is italic, false otherwise.
     */
    public boolean isItalic(){
        return italic;
    }

    /**
     * Sets the font color for this GraphFont object.
     * @param color the color for the GraphFont object.
     */
    public void setFontColor(Color color) {
        this.color = color;
    }
}
