package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.IONode;
import dae.fxcreator.node.NodeIO;
import dae.fxcreator.node.transform.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class ObjectPortExpression implements Expression {
    private final String portName;
    private final boolean root;
    
    public ObjectPortExpression(String portName, boolean root){
        this.portName = portName;
        this.root = root;
    }
    
    @Override
    public Object evaluate(ExportTask context) {
        Object top;
        if ( root ){
            top = context.getVar("node");
        }else{
            top = context.popChainStack();
        }
        if ( top instanceof IONode){
            IONode node = (IONode)top;
            NodeIO port = node.getPort(portName);
            return port;
        }else{
            return null;
        }
    }
}
