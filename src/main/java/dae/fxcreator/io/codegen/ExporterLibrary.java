package dae.fxcreator.io.codegen;

import java.util.HashMap;

/**
 * This class exports the node structure of a shader as an fx file.
 * @author Koen
 */
public class ExporterLibrary {
    private final HashMap<String,CodeTemplateLibrary> templateLibrary =
            new HashMap<>();

   

    /**
     * Creates a new ExporterLibrary object.
     */
    public ExporterLibrary(){
       
    }

    /**
     * Adds a CodeTemplateLibrary to this ExporterLibrary object.
     * @param library the library to add.
     */
    public void addCodeTemplateLibrary(CodeTemplateLibrary library){
        templateLibrary.put(library.getId(),library);
        System.out.println("Base library is : "+ library.getBase());
        for(String id : templateLibrary.keySet())
            System.out.println("Library with " + id + " found !");
        if ( library.getBase() != null ){
            CodeTemplateLibrary baseLibrary = templateLibrary.get(library.getBase());
            if ( baseLibrary != null ){
                library.setBaseLibrary(baseLibrary);
            }
        }
    }

    /**
     * Returns the codetemplatelibrary with the given exporter id.
     * @param exporterId the id for the exporter.
     * @return the CodeTemplateLibrary.
     */
    public  CodeTemplateLibrary getCodeTemplateLibrary(String exporterId){
        return templateLibrary.get(exporterId);
    }

     /**
      * Returns the list of CodeTemplateLibrary objects.
      * @return the list of CodeTemplateLibrary objects.
      */
    public Iterable<CodeTemplateLibrary> getLibraries() {
        return templateLibrary.values();
    }
}