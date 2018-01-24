package dae.fxcreator.io.savers;

import dae.fxcreator.node.NodeGroup;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.NodeContainer;
import dae.fxcreator.node.SettingsGroup;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.node.ShaderOutput;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class saves a NodeContainer object into a separate file.
 * @author Koen
 */
public class FXMethodSaver extends XMLSaver {
    private final NodeContainer container;
    private final Path file;

    /**
     * Creates a new FXMethodSaver object.
     * @param filePath
     * @param container the container to save.
     */
    public FXMethodSaver(Path filePath, NodeContainer container){
        this.container = container;
        this.file = filePath;
    }

     public void save() {
        FileOutputStream fos = null;
        try {
            BufferedWriter bw = Files.newBufferedWriter(file);
            writeHeader(bw,"group");
            writeNodeContainer(bw,container);
           
            bw.write("</group>\n");
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FXProjectSaver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(FXProjectSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

     private void writeNodeContainer(BufferedWriter bw,NodeContainer container) throws IOException
     {
            bw.write("<method");
            if ( container.isInputStructSet())
                writeAttribute(bw, "inputStruct", container.getInputStruct().getId());
            if ( container.isOutputStructSet())
                writeAttribute(bw, "outputStruct", container.getOutputStruct().getId());
            Point pin = container.getInputNode().getPosition();
            writeAttribute(bw, "inputPosition", "[" + pin.x + "," + pin.y + "]");
            Point pout = container.getOutputNode().getPosition();
            writeAttribute(bw, "outputPosition", "[" + pout.x + "," + pout.y + "]");
            bw.write(">\n");

            ShaderNode inputNode = container.getInputNode();
            for (ShaderOutput output : inputNode.getOutputs()) {
                writeTabs(bw, 2);
                bw.write("<input");
                writeAttribute(bw, "name", output.getName());
                writeAttribute(bw, "connection", output.getConnectionString());
                writeAttribute(bw, "type", output.getType().toString());
                writeAttribute(bw, "semantic", output.getSemantic().getValue());
                bw.write("/>\n");
            }

            ShaderNode outputNode = container.getOutputNode();
            for (ShaderInput input : outputNode.getInputs()) {
                writeTabs(bw, 2);
                bw.write("<output");
                writeAttribute(bw, "name", input.getName());
                writeAttribute(bw, "connection", input.getConnectionString());
                writeAttribute(bw, "type", input.getType().toString());
                writeAttribute(bw, "semantic", input.getSemantic().getValue());
                bw.write("/>\n");
            }

            for (IONode node : container.getNodes()) {
                writeShaderNode(bw, node, 2);
            }
            bw.write("\t</method>\n");
     }

     private void writeShaderNode(BufferedWriter bw, IONode node, int numberOfTabs) throws IOException {
        writeTabs(bw, numberOfTabs);
        bw.write("<node");
        writeAttribute(bw, "id", node.getId());
        writeAttribute(bw, "name", node.getName());
        if (node instanceof ShaderNode) {
            ShaderNode sn = (ShaderNode) node;
            writeAttribute(bw, "type", sn.getType());
        }
        writeAttribute(bw, "ioEditable", Boolean.toString(node.isInputOutputEditable()));
        Point p = node.getPosition();
        writeAttribute(bw, "position", "[" + p.x + "," + p.y + "]");
        if ( node instanceof NodeGroup)
        {
            NodeContainer nc = (NodeContainer)node;
            writeAttribute(bw,"container",nc.getContainerType());
            Point pin = nc.getInputNode().getPosition();
            writeAttribute(bw, "inputPosition", "[" + pin.x + "," + pin.y + "]");
            Point pout = nc.getOutputNode().getPosition();
            writeAttribute(bw, "outputPosition", "[" + pout.x + "," + pout.y + "]");
        }else{
            writeAttribute(bw,"container","leaf");
        }
        bw.write(">\n");

        ArrayList<ShaderInput> inputs = node.getInputs();
        for (ShaderInput input : inputs) {
            writeTabs(bw, numberOfTabs + 1);
            bw.write("<input");
            writeAttribute(bw, "name", input.getName());
            writeAttribute(bw, "connection", input.getConnectionString());
            writeAttribute(bw, "type", input.getType().toString());
            writeAttribute(bw, "semantic", input.getSemantic().getValue());
            bw.write("/>\n");
        }

        ArrayList<ShaderOutput> outputs = node.getOutputs();
        for (ShaderOutput output : outputs) {
            writeTabs(bw, numberOfTabs + 1);
            bw.write("<output");
            writeAttribute(bw, "name", output.getName());
            writeAttribute(bw, "connection", output.getConnectionString());
            writeAttribute(bw, "type", output.getType().toString());
            writeAttribute(bw, "semantic", output.getSemantic().getValue());
            if ( output.getTypeRule() != null )
                writeAttribute(bw, "typerul", output.getTypeRule());
            bw.write("/>\n");
        }

        // write the settings
        for (SettingsGroup sg : node.getSettingsGroups()) {
            for (Setting s : sg.getSettings()) {
                if (sg.getName().equals("Node") && ("id".equals(s.getId()) || "name".equals(s.getId()))) {
                    continue;
                }
                writeTabs(bw, numberOfTabs + 1);
                bw.write("<setting");
                writeAttribute(bw, "group", s.getGroup());
                writeAttribute(bw, "id", s.getId());
                if (s.isWriteValueAsAttribute()) {
                    writeAttribute(bw, "value", s.getSettingValue());
                    bw.write("/>\n");
                } else {
                    bw.write(">\n");
                    writeTabs(bw, numberOfTabs + 2);
                    bw.write("<value>");
                    bw.write("<![CDATA[");
                    bw.write(s.getSettingValue());
                    bw.write("]]>");
                    bw.write("</value>\n");
                    writeTabs(bw, numberOfTabs + 1);
                    bw.write("</setting>\n");
                }
            }
        }

        // write the children if it is a group node.
        if ( node instanceof NodeGroup)
        {
            NodeGroup ng = (NodeGroup)node;
            for ( IONode childNode : ng.getNodes()){
                writeShaderNode(bw,childNode,numberOfTabs+1);
            }
        }

        writeTabs(bw, numberOfTabs);
        bw.write("</node>\n");
    }
}
