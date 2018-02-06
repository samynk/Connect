package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class VarID implements Expression{
    private String id;
    public VarID(String id){
        this.id = id;
    }
    
    public String getId(){
        return id;
    }
    
    @Override
    public String toString(){
        return id;
    }

    @Override
    public Object evaluate(ExportTask context) {
        Object o = context.getVar(id);
        return o;
    }
}
