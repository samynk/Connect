package dae.fxcreator.io.loaders;

import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Koen
 */
public abstract class XMLHandler extends DefaultHandler{
    String rootElementName;

    public abstract void reset();

    public abstract Object getResult();

    public String getRootElementName(){
        return rootElementName;
    }

    public void setRootElementName(String rootElementName){
        this.rootElementName = rootElementName;
    }

}
