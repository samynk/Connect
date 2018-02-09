package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.transform.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class StringValue implements Expression{
    private String value;
    
    public StringValue(String value){
        this.value = value;
    }

    @Override
    public Object evaluate(ExportTask context) {
        return value;
    }
}
