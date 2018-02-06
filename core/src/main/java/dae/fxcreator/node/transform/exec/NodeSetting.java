package dae.fxcreator.node.transform.exec;

import dae.fxcreator.node.IONode;
import dae.fxcreator.node.settings.Setting;
import dae.fxcreator.node.transform.ExportTask;

/**
 *
 * @author Koen Samyn (samyn.koen@gmail.com)
 */
public class NodeSetting implements Expression{
    private final VarID varID;
    private final String groupID;
    private final String settingID;
    
    /**
     *
     * @param varID
     * @param groupID
     * @param settingID
     */
    public NodeSetting( String varID, String groupID, String settingID)
    {
        this.varID = new VarID(varID);
        this.groupID = groupID;
        this.settingID = settingID;
    }

    @Override
    public Object evaluate(ExportTask context) {
        Object node = varID.evaluate(context);
        if ( node != null && node instanceof IONode)
        {
            IONode n = (IONode)node;
            Setting s = n.getSetting(groupID, settingID);
            return s.getSettingValueAsObject();
        }else{
            return null;
        }
    }

}
