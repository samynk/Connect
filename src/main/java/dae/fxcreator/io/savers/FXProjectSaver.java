package dae.fxcreator.io.savers;

import dae.fxcreator.io.*;
import dae.fxcreator.io.templates.Setting;
import dae.fxcreator.node.IONode;
import dae.fxcreator.node.NodeContainer;
import dae.fxcreator.node.ReferenceNode;
import dae.fxcreator.node.SettingsGroup;
import dae.fxcreator.node.ShaderField;
import dae.fxcreator.node.ShaderInput;
import dae.fxcreator.node.ShaderNode;
import dae.fxcreator.node.ShaderOutput;
import dae.fxcreator.node.ShaderStruct;
import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Koen
 */
public class FXProjectSaver extends XMLSaver {

    FXProject project;

    public FXProjectSaver(FXProject project) {
        this.project = project;
    }

    /**
     * Saves the project.
     * @param saveFileInformation if set to true, the file information will be written.
     */
    public void save(boolean saveFileInformation) {
        FileOutputStream fos = null;
        try {
            Path projectPath = project.getFile();
            if ( !Files.exists(projectPath)){
                
                Path parentPath = projectPath.getParent();
                if ( Files.isDirectory(parentPath) && !Files.exists(parentPath))
                {
                    Files.createDirectories(parentPath);
                }
            }
            BufferedWriter bw = Files.newBufferedWriter(project.getFile());
            
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            bw.write("<project");
            writeAttribute(bw, "name", project.getName());
            bw.write(">\n");
            if ( project.getType() != null ){
                FXProjectType type = project.getProjectType();
                bw.write("\t<projecttype ");
                writeAttribute(bw, "name", type.getName());
                writeAttribute(bw, "version", type.getVersion());
                writeAttribute(bw, "minorVersion", type.getMinorVersion());
                bw.write("/>\n");
            }
            
            if ( saveFileInformation )
                writeExport(bw);
            writeGlobals(bw);
            writeStructs(bw);
            writeRasterizerStates(bw);
            writeStages(bw);
            writeTechniques(bw);
            bw.write("</project>\n");
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

    private void writeRasterizerStates(BufferedWriter bw) throws IOException {
        bw.write("\t<states>\n");
        for (ShaderNode node : project.getStateColllection().getStates()) {
            this.writeShaderNode(bw, node, 2);
        }
        bw.write("\t</states>\n");
    }

    private void writeExport(BufferedWriter bw) throws IOException {
        bw.write("\t<export>\n");
        for (String id : project.getExportDestinations()) {
            ExportFile destination = project.getExportDestination(id);
            bw.write("\t\t<file ");
            this.writeAttribute(bw, "exporterId", id);
            this.writeAttribute(bw, "dir", destination.getDirectory().getPath());
            this.writeAttribute(bw, "name", destination.getFilename());
            this.writeAttribute(bw, "extension", destination.getExtension());
            bw.write("><![CDATA[");
            bw.write(destination.getDirectory().getPath());
            bw.write("]]>");
            bw.write("</file>");
        }
        bw.write("\t</export>\n");
    }

    private void writeGlobals(BufferedWriter bw) throws IOException {
        bw.write("\t<global>\n");
        for (ShaderNode node : project.getGlobalNodes()) {
            writeShaderNode(bw, node, 2);
        }
        bw.write("\t</global>\n");
    }

    private void writeStructs(BufferedWriter bw) throws IOException {
        bw.write("\t<structs>\n");
        for (ShaderStruct struct : project.getStructs()) {
            bw.write("\t\t<struct ");
            writeAttribute(bw, "id", struct.getId());
            bw.write(">\n");
            for (ShaderField field : struct.getFields()) {
                bw.write("\t\t\t<field ");
                writeAttribute(bw, "name", field.getName());
                writeAttribute(bw, "semantic", field.getSemantic().getValue());
                writeAttribute(bw, "type", field.getType().toString());
                bw.write("/>\n");
            }
            bw.write("\t\t</struct>\n");
        }
        bw.write("\t</structs>\n");
    }

    private void writeStages(BufferedWriter bw) throws IOException {
        for (ShaderStage stage : project.getStages()) {
            bw.write("\t<stage");
            writeAttribute(bw, "name", stage.getName());
            writeAttribute(bw, "type", stage.getType());
            if (stage.isInputStructSet()) {
                writeAttribute(bw, "inputStruct", stage.getInputStruct().getId());
            }
            if (stage.isOutputStructSet()) {
                writeAttribute(bw, "outputStruct", stage.getOutputStruct().getId());
            }
            Point pin = stage.getInputNode().getPosition();
            writeAttribute(bw, "inputPosition", "[" + pin.x + "," + pin.y + "]");
            Point pout = stage.getOutputNode().getPosition();
            writeAttribute(bw, "outputPosition", "[" + pout.x + "," + pout.y + "]");
            bw.write(">\n");

            ShaderNode inputNode = stage.getInputNode();
            for (ShaderOutput output : inputNode.getOutputs()) {
                writeTabs(bw, 2);
                bw.write("<input");
                writeAttribute(bw, "name", output.getName());
                writeAttribute(bw, "connection", output.getConnectionString());
                writeAttribute(bw, "type", output.getType().toString());
                writeAttribute(bw, "semantic", output.getSemantic().getValue());
                bw.write("/>\n");
            }

            ShaderNode outputNode = stage.getOutputNode();
            for (ShaderInput input : outputNode.getInputs()) {
                writeTabs(bw, 2);
                bw.write("<output");
                writeAttribute(bw, "name", input.getName());
                writeAttribute(bw, "connection", input.getConnectionString());
                writeAttribute(bw, "type", input.getType().toString());
                writeAttribute(bw, "semantic", input.getSemantic().getValue());
                bw.write("/>\n");
            }

            for (IONode node : stage.getNodes()) {
                writeShaderNode(bw, node, 2);
            }

            for (ReferenceNode rn : stage.getReferenceNodes()){
                writeReferenceNode(bw, rn, 2);
            }
            bw.write("\t</stage>\n");
        }
    }

    private void writeTechniques(BufferedWriter bw) throws IOException {
        for (Technique technique : project.getTechniques()) {
            bw.write("\t<technique");
            writeAttribute(bw, "name", technique.getName());
            bw.write(">\n");
            ArrayList<Pass> passes = technique.getPasses();
            for (Pass p : passes) {
                bw.write("\t\t<pass ");
                writeAttribute(bw, "name", p.getName());
                bw.write(">\n");

                ArrayList<ShaderStage> stages = p.getStages();
                for (ShaderStage stage : stages) {
                    bw.write("\t\t\t<stageid");
                    writeAttribute(bw, "id", stage.getId());
                    bw.write("/>\n");
                }
                if (p.hasRasterizerState()) {
                    ShaderNode state = p.getRasterizerState();
                    bw.write("\t\t\t<rasterizerState ");
                    writeAttribute(bw, "id", state.getId());
                    bw.write("/>\n");
                }
                bw.write("\t\t</pass>\n");
            }
            bw.write("\t</technique>\n");
        }
    }

    private void writeReferenceNode(BufferedWriter bw, ReferenceNode node, int numberOfTabs) throws IOException{
        writeTabs(bw,numberOfTabs);
        bw.write("<refnode");
        writeAttribute(bw,"id",node.getReferencedNode().getId());
        writeAttribute(bw,"position","["+node.getX()+","+node.getY()+"]");
        bw.write("/>\n");
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
        if (node instanceof NodeGroup) {
            NodeContainer nc = (NodeContainer) node;
            writeAttribute(bw, "container", nc.getContainerType());
            writeAttribute(bw, "subtype", nc.getSubType());
            Point pin = nc.getInputNode().getPosition();
            writeAttribute(bw, "inputPosition", "[" + pin.x + "," + pin.y + "]");
            Point pout = nc.getOutputNode().getPosition();
            writeAttribute(bw, "outputPosition", "[" + pout.x + "," + pout.y + "]");
        } else {
            writeAttribute(bw, "container", "leaf");
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
            if (output.getTypeRule() != null) {
                writeAttribute(bw, "typerule", output.getTypeRule());
            }
            bw.write("/>\n");
        }

        // write the settings
        for (SettingsGroup sg : node.getSettingsGroups()) {
            for (Setting s : sg.getSettings()) {
                if (sg.getName().equals("Node") && ("id".equals(s.getId()) || "name".equals(s.getId()))) {
                    continue;
                }
                if ( s.isDefaultValue())
                    continue;
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
                    if ( !s.isValueAsXML() )
                        bw.write("<![CDATA[");
                    bw.write(s.getSettingValue());
                    if ( !s.isValueAsXML() )
                        bw.write("]]>");
                    bw.write("</value>\n");
                    writeTabs(bw, numberOfTabs + 1);
                    bw.write("</setting>\n");
                }
            }
        }

        // write the children if it is a group node.
        if (node instanceof NodeGroup) {
            NodeGroup ng = (NodeGroup) node;
            if (!"group".equals(ng.getContainerType())) {
                for (IONode childNode : ng.getNodes()) {
                    writeShaderNode(bw, childNode, numberOfTabs + 1);
                }
            }
        }

        writeTabs(bw, numberOfTabs);
        bw.write("</node>\n");
    }
}
