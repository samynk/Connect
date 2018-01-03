package dae.fxcreator.io.savers;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class offers basic functionality to write xml files.
 *
 * @author Koen
 */
public class XMLSaver {

    /**
     * Writes a header for the xml file
     *
     * @param bw the BufferedWriter to write the header to.
     * @param documentRoot the root of the xml document.
     */
    public void writeHeader(BufferedWriter bw, String documentRoot) throws IOException {
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        bw.write("<");
        bw.write(documentRoot);
        bw.write(">\n");
    }

    /**
     * Creates a file relative to the directory of the application.
     *
     * @param fileName the file name relative to the directory of the
     * application.
     * @return a BufferedWriter object.
     */
    public BufferedWriter createRelativeFileWriter(String fileName) throws IOException {
        File file = new File(System.getProperty("user.dir"), fileName);
        FileWriter fw = new FileWriter(file);
        return new BufferedWriter(fw);
    }

    /**
     * Creates a file relative to the home director of the user.
     *
     * @param fileName the filename relative to the home directory of the user.
     * @return a BufferedWriter object.
     * @throws IOException
     */
    public BufferedWriter createHomeFileWriter(String fileName) throws IOException {
        File file = new File(System.getProperty("user.home"), fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        FileWriter fw = new FileWriter(file);
        return new BufferedWriter(fw);
    }

    /**
     * Writes a number of tabs at the beginning of a line.
     *
     * @param bw the BufferedWriter object.
     * @param tabs the number of tabs to write.
     * @throws IOException
     */
    public void writeTabs(BufferedWriter bw, int tabs) throws IOException {
        for (int i = 0; i < tabs; ++i) {
            bw.write("\t");
        }
    }

    /**
     * Writes an attribute to the output stream.
     *
     * @param bw the bufferedwrite to write to.
     * @param name the name of the attribute.
     * @param value the value for the attribute.
     * @throws IOException
     */
    public void writeAttribute(BufferedWriter bw, String name, int value) throws IOException {
        bw.write(" ");
        bw.write(name);
        bw.write("=\"");
        bw.write(Integer.toString(value));
        bw.write("\"");
    }

    /**
     * Writes an attribute to the output stream.
     *
     * @param bw the bufferedwrite to write to.
     * @param name the name of the attribute.
     * @param value the value for the attribute.
     * @throws IOException
     */
    public void writeAttribute(BufferedWriter bw, String name, String value) throws IOException {
        if (value == null) {
            return;
        }
        bw.write(" ");
        bw.write(name);
        bw.write("=\"");
        bw.write(value);
        bw.write("\"");
    }

    /**
     * Writes an attribute to the output stream.
     *
     * @param bw the bufferedwrite to write to.
     * @param name the name of the attribute.
     * @param value the value for the attribute.
     * @throws IOException
     */
    public void writeAttribute(BufferedWriter bw, String name, Color value) throws IOException {
        if (value == null) {
            return;
        }
        bw.write(" ");
        bw.write(name);
        bw.write("=\"");
        String r1 = Integer.toHexString(value.getRed());
        if (r1.length() == 1) {
            r1 = "0" + r1;
        }
        String g1 = Integer.toHexString(value.getGreen());
        if (g1.length() == 1) {
            g1 = "0" + g1;
        }
        String b1 = Integer.toHexString(value.getBlue());
        if (b1.length() == 1) {
            b1 = "0" + b1;
        }
        bw.write("#" + r1 + g1 + b1);
        bw.write("\"");
    }
}
