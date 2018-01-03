/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.templates.GradientSetting;
import dae.fxcreator.node.gui.GraphGradient;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Allows the user to define a gradient.
 * @author Koen
 */
public class GradientSettingComponent extends JPanel implements ActionListener {

    private JLabel label;
    private GradientPanel pnlGradient;
    private GradientSetting setting;
    private GraphGradient gradient;

    public GradientSettingComponent() {
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        GridBagConstraints gbc = new GridBagConstraints();
        label = new JLabel();
        pnlGradient = new GradientPanel();
        pnlGradient.setOpaque(true);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.insets.top = 0;
        gbc.insets.bottom = 0;
        gbc.insets.left = 2;
        gbc.insets.right = 2;
        add(label, gbc);

        JButton btnColor1 = new JButton("...");
        btnColor1.setActionCommand("color1");
        btnColor1.addActionListener(this);
        gbc.gridx = 1;
        add(btnColor1, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        gbc.ipadx = 75;
        gbc.insets.top = 2;
        gbc.insets.bottom = 2;
        gbc.insets.left = 2;
        gbc.insets.right = 2;
        add(pnlGradient, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        gbc.ipadx = 0;
        gbc.insets.top = 0;
        gbc.insets.bottom = 0;
        gbc.insets.left = 2;
        gbc.insets.right = 2;
        JButton btnColor2 = new JButton("...");
        btnColor2.setActionCommand("color2");
        btnColor2.addActionListener(this);
        add(btnColor2, gbc);


    }

    public void setGradientSetting(GradientSetting setting) {
        this.setting = setting;
        this.gradient = null;
        pnlGradient.setColor1(setting.getColor1());
        pnlGradient.setColor2(setting.getColor2());
        label.setText(setting.getLabel() + " : ");
    }

    public void setGraphGradient(GraphGradient gg) {
        this.setting = null;
        this.gradient = gg;
        pnlGradient.setColor1(gg.getC1());
        pnlGradient.setColor2(gg.getC2());
        System.out.println(gg);
        label.setText(gg.getName());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("color1")) {
            Color startColor = setting == null ? gradient.getC1() : setting.getColor1();
            Color selected = JColorChooser.showDialog(this, "select color 1", startColor);
            if (selected == null) {
                return;
            }
            if (setting != null) {
                setting.setColor1(selected);
            } else if (gradient != null) {
                gradient.setC1(selected);
            }
        } else {
            Color startColor = setting == null ? gradient.getC2() : setting.getColor2();
            Color selected = JColorChooser.showDialog(this, "select color 2", startColor);
            if (selected == null) {
                return;
            }
            if (setting != null) {
                setting.setColor2(selected);
            } else if (gradient != null) {
                gradient.setC2(selected);
            }
        }
        if (setting != null) {
            pnlGradient.setColor1(setting.getColor1());
            pnlGradient.setColor2(setting.getColor2());
        }else{
            pnlGradient.setColor1(gradient.getC1());
            pnlGradient.setColor2(gradient.getC2());
        }
        pnlGradient.repaint();
    }
}

