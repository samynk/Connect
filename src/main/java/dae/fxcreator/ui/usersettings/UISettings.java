package dae.fxcreator.ui.usersettings;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;

/**
 * The user interface settings.
 * @author Koen
 */
public class UISettings {

    private Point location = new Point();
    private Dimension size=new Dimension();
    private String resolution;
    private boolean fullscreen;
    private HashMap<String,Integer> dividerLocations = new HashMap<String,Integer>();

    public UISettings() {
        
    }

    public UISettings(String resolution) {
        this.resolution = resolution;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setLocation(int x, int y) {
        location.x = x;
        location.y = y;
    }

    public Point getLocation() {
        return location;
    }

    public void setSize(int width, int height) {
        size.width = width;
        size.height = height;
    }

    public Dimension getSize() {
        return size;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    void setDividerLocation(String name, int location) {
       dividerLocations.put(name,location);
    }

    public int getDividerLocation(String name){
        return dividerLocations.get(name);
    }

    public boolean hasDividerLocation(String name) {
        return dividerLocations.containsKey(name);
    }

    public String toString(){
        String output = new String();
        output +=  "UI Settings" ;
        output += "\nDimension : " + this.getResolution();
        output += "\nDividers";
        for ( String key : dividerLocations.keySet()){
            output += "\n"+key +":"+dividerLocations.get(key);
        }
        return output;
    }
}
