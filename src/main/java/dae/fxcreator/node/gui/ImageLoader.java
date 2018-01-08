package dae.fxcreator.node.gui;

import java.awt.*;

/**
 * Loads images from the classpath
 * @author samynk
 */
public class ImageLoader {
    private final MediaTracker tracker = new MediaTracker(new Label());
    private static final ImageLoader INSTANCE = new ImageLoader();
    private int count=0;
    
    public static ImageLoader getInstance(){
        return INSTANCE;
    }
    
    public Image getImage(String resource){
        if (resource == null ){
            return null;
        }
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
