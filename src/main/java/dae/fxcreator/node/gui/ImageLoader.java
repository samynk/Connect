/*
 * ImageLoader.java
 *
 * Created on 22 december 2005, 8:35
 */

package dae.fxcreator.node.gui;

import java.awt.*;

/**
 * Loads images from the classpath
 * @author samynk
 */
public class ImageLoader {
    private MediaTracker tracker = new MediaTracker(new Label());
    private static ImageLoader instance = new ImageLoader();
    private int count=0;
    
    public static ImageLoader getInstance(){
        return instance;
    }
    
    public Image getImage(String resource){
        try{
            //System.out.println("Loading : " + resource);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage(getClass().getResource(resource));
            tracker.addImage(image,count++);
            try{
                tracker.waitForID(count-1);
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
             //System.out.println("After : " + resource);
             return image;
        }catch(Exception ex){
            //System.out.println("Could not find : " + resource);
        }
        return null;
    }
}
