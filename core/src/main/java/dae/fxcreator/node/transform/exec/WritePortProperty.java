package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.IONode;
import dae.fxcreator.node.NodeIO;
import dae.fxcreator.node.transform.ExportTask;
import dae.fxcreator.node.transform.RuntimeUtil;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class WritePortProperty implements Executable {

    private final String portName;
    private final String property;

    public WritePortProperty(String port, String property) {
        this.portName = port;
        if (property != null) {
            this.property = RuntimeUtil.convertToMethod(property);
        }else{
            this.property = null;
        }
    }

    @Override
    public void execute(Object codeObject, ExportTask context) {
        if ( codeObject instanceof IONode ){
            IONode io = (IONode)codeObject;
            NodeIO port = io.findInput(portName);
            if ( port == null ){
                port = io.findOutput(portName);
            }
            if(port !=null){
                if ( property != null ){
                    Object value = RuntimeUtil.invokeGet(port, property);
                    context.peekBuffer().append(value.toString());
                }else{
                    context.peekBuffer().append(port.getName());
                }
            }
        }
    }
}
