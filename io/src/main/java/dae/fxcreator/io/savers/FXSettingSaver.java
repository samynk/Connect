package dae.fxcreator.io.savers;

import dae.fxcreator.gui.model.FXSettings;
import dae.fxcreator.gui.model.GraphFont;
import dae.fxcreator.gui.model.NodeStyle;
import dae.fxcreator.node.Semantic;
import dae.fxcreator.gui.model.GraphGradient;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListModel;

/**
 * Creates a saver object for the settings file.
 *
 * @author Koen
 */
public class FXSettingSaver extends XMLSaver {

    FXSettings fxSettings;

    /**
     * Create a new FXSettingSaver object.
     *
     * @param settings the settings object to save.
     */
    public FXSettingSaver(FXSettings settings) {
        fxSettings = settings;
    }

    /**
     * Saves the settings object to file.
     */
    public void save() {
        if (!this.fxSettings.isLoaded()) {
            return;
        }
        try {
            BufferedWriter bw = this.createHomeFileWriter("/.umbrafx/templates/settings.xml");
            this.writeHeader(bw, "settings");
            writeUI(bw);
            bw.write("</settings>");
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FXSettingSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeUI(BufferedWriter bw) throws IOException {
        bw.append("\t<ui>\n");
        ListModel fonts = fxSettings.getAllFonts();
        for (int i = 0; i < fonts.getSize(); ++i) {
            GraphFont font = (GraphFont) fonts.getElementAt(i);
            bw.write("\t\t<font ");
            writeAttribute(bw, "name", font.getName());
            writeAttribute(bw, "size", Integer.toString(font.getFontSize()));
            writeAttribute(bw, "family", font.getFontFamily());
            writeAttribute(bw, "bold", Boolean.toString(font.isBold()));
            writeAttribute(bw, "italic", Boolean.toString(font.isItalic()));
            writeAttribute(bw, "color", font.getColor());
            bw.write("/>\n");

        }
        for (String gradient : fxSettings.getGradients()) {
            GraphGradient gg = fxSettings.getGradient(gradient);
            bw.write("\t\t<gradient ");
            writeAttribute(bw, "name", gg.getName());
            writeAttribute(bw, "color1", gg.getC1());
            writeAttribute(bw, "color2", gg.getC2());
            bw.write("/>\n");
        }

        for (NodeStyle style : fxSettings.getAllStylesAsList()) {
            bw.write("\t\t<style ");
            writeAttribute(bw, "name", style.getName());
            writeAttribute(bw, "gradient", style.getGradientName());
            writeAttribute(bw, "titleFont", style.getTitleFontName());
            writeAttribute(bw, "portFont", style.getPortFontName());
            writeAttribute(bw, "semanticFont", style.getSemanticFontName());
            bw.write("/>\n");
        }
        bw.append("\t</ui>\n");
    }
}
