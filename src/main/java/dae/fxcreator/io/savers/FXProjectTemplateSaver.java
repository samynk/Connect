package dae.fxcreator.io.savers;

import dae.fxcreator.io.FXProjectTemplate;
import dae.fxcreator.io.FXProjectTemplateGroup;
import dae.fxcreator.io.FXProjectTemplates;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koen
 */
public class FXProjectTemplateSaver extends XMLSaver {
    FXProjectTemplates toSave;

    /**
     * Creates a new FXProjectTemplateSaver object.
     * @param template the template object to save.
     */
    public FXProjectTemplateSaver(FXProjectTemplates template){
        this.toSave = template;
    }

    /**
     * Saves the FXProjectTemplate
     */
    public void save(){
        try {
            BufferedWriter bw = this.createRelativeFileWriter("fxtemplates/default.daefx");
            this.writeHeader(bw,"projecttemplates");
            this.writeTabs(bw, 1);
            bw.write("<startproject ");
            FXProjectTemplate startTemplate = toSave.getStartProject();
            this.writeAttribute(bw,"template",startTemplate.getGroup()+"."+startTemplate.getName());
            bw.write("/>\n");
            for (FXProjectTemplateGroup group : toSave.getGroups())
            {
                this.writeTabs(bw,1);
                bw.write("<group ");
                writeAttribute(bw,"name",group.getName());
                writeAttribute(bw,"label",group.getUILabel());
                bw.write(">\n");

                for ( FXProjectTemplate template : group.getTemplates())
                {
                    this.writeTabs(bw,2);
                    bw.write("<template ");
                    writeAttribute(bw,"name",template.getName());
                    writeAttribute(bw,"label",template.getUILabel());
                    bw.write(">\n");
                    this.writeTabs(bw,3);
                    bw.write("<file><![CDATA[");
                    if ( template.hasRelativePath())
                        bw.write(template.getRelativePath().toString());
                    else
                        bw.write(template.getSourceFile().toString());
                    bw.write("]]></file>\n");
                    this.writeTabs(bw,3);
                    bw.write("<description><![CDATA[");
                    bw.write(template.getDescription());
                    bw.write("]]></description>\n");
                    this.writeTabs(bw,2);
                    bw.write("</template>\n");
                }
                this.writeTabs(bw,1);
                bw.write("</group>\n");
            }
            bw.write("</projecttemplates>");
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FXSettingSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
