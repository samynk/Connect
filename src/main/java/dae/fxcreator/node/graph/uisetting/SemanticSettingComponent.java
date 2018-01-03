/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.templates.SemanticSetting;
import dae.fxcreator.node.graph.model.SemanticDataModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Allows the user to set a semantic for a JPanel and also to
 * add semantics to the list of semantics.
 * @author Koen
 */
public class SemanticSettingComponent extends JPanel implements ItemListener {
    private JLabel lblLabel = new JLabel();
    private SemanticDataModel model;
    private SemanticSetting setting;

    /**
     * Creates a new SemanticSettingComponent.
     */
    public SemanticSettingComponent(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx=0.0;
        gbc.gridx=0;
        gbc.gridy=0;

        add(lblLabel,gbc);

        gbc.weightx = 1.0;
        gbc.gridx=1;

        model = new SemanticDataModel();
        JComboBox cboSemantics = new JComboBox();
        cboSemantics.setModel(model);
        add(cboSemantics,gbc);

        cboSemantics.addItemListener(this);
    }

    public void setSemanticSetting(SemanticSetting setting){
        lblLabel.setText(setting.getLabel()+ " : ");
        this.setting = setting;
        model.setSelectedItem(setting.getSettingValue());
    }

    public void itemStateChanged(ItemEvent e) {
        if ( e.getStateChange() == ItemEvent.SELECTED){
            setting.setSettingValue(e.getItem().toString());
        }
    }
}
