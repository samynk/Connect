package dae.fxcreator.node.graph.math;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * A container for mathfields with a specific layout.
 * @author Koen
 */
public class MathContainer extends javax.swing.JPanel implements MathGUIElement, MouseListener, FocusListener, Runnable {

    private ArrayList<MathGUIElement> mathComponents = new ArrayList<MathGUIElement>();
    protected GridBagConstraints gbc = new GridBagConstraints();
    private Border selectedBorder;
    private Border emptyBorder;
    private MathContainer parentContainer;
    private boolean selected;
    protected MathContext context;
    private boolean rootnode = false;

    private Image snapshot;
    private boolean locked = false;

    /** Creates new form MathContainer */
    public MathContainer(MathContext context) {
        //initComponents();
        setLayout(new GridBagLayout());

        gbc.weightx = 0.0f;
        gbc.weighty = 0.0f;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 2, 2, 2);


        selectedBorder = BorderFactory.createLineBorder(Color.orange, 1);
        emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

        setBorder(emptyBorder);

        this.context = context;
        this.setOpaque(false);

        addMouseListener(this);
        //addFocusListener(this);
        this.setFocusable(true);
        setBackground(Color.white);
        setOpaque(false);
    }
    
    public void setLocked(){
        this.locked = true;
    }
    
    public boolean isLocked(){
        return locked;
    }

    public Image getSnapShot(){
        return snapshot;
    }

    public void setAsRootNode() {
        rootnode = true;
        setOpaque(true);
    }

    @Override
    public boolean isValidateRoot() {
        return rootnode;
    }

    @Override
    public void removeAll() {
        mathComponents.clear();
        super.removeAll();
    }

    /**
     * Returns a base line to use for a row layout.
     * @return the baseline in pixels.
     */
    public int getBaseLine() {
        return getHeight() / 2;
    }

    public void addMathField(MathGUIElement field) {
        addMathField(field, getConstraints(field));
    }

    public void invalidateAll() {
        this.invalidate();
        for (Component c : this.getComponents()) {
            c.invalidate();
        }
        revalidate();
        adjustBaseLine();
    }

    public void replaceField(MathGUIElement toReplace, MathGUIElement newElement) {
        GridBagLayout layout = (GridBagLayout) this.getLayout();
        Object constraints = layout.getConstraints((Component) toReplace);
        if (constraints != null) {
            removeMathField(toReplace);
            this.addMathField(newElement, constraints);
        }
        invalidateAll();
        context.elementChanged(this);
    }

    public void addMathField(MathGUIElement field, Object constraints) {
        JComponent jc = (JComponent) field;
        mathComponents.add(field);
        add(jc, constraints);
        field.setMathContext(context);
        field.setParentContainer(this);
        invalidateAll();
    }

    public void removeMathField(MathGUIElement field) {
        JComponent jc = (JComponent) field;
        mathComponents.remove(field);
        remove(jc);
        invalidateAll();
        field.setParentContainer(null);
    }

    public Object getConstraints(MathGUIElement field) {
        LayoutManager layout = this.getLayout();
        if (layout instanceof GridBagLayout) {
            GridBagLayout gbl = (GridBagLayout) layout;
            int gridy = 0;
            for (Component c : this.getComponents()) {
                GridBagConstraints gbc = (GridBagConstraints) gbl.getConstraints(c);
                gridy = Math.max(gridy, gbc.gridy);
            }
            gbc.gridy = this.mathComponents.size();
        }
        return gbc;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public MathContainer getParentContainer() {
        return parentContainer;
    }

    public MathGUIElement getFirstChild() {
        if (mathComponents.size() > 0) {
            return mathComponents.get(0);
        } else {
            return null;
        }
    }

    public Iterable<MathGUIElement> getMathElements(){
        return mathComponents;
    }

    @Override
    public void setParentContainer(MathContainer parent) {
        this.parentContainer = parent;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.setBorder(selectedBorder);
        } else {
            this.setBorder(emptyBorder);
        }
        repaint();
    }

    public void setMathContext(MathContext context) {
        this.context = context;
    }

    public void removeFromParent() {
        if (parentContainer != null) {
            parentContainer.removeMathField(this);
        }
    }

    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        context.setCurrentElement(this);
    }

    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void adjustBaseLine() {
        for (Component c : this.getComponents()) {
            if (c instanceof MathGUIElement) {
                MathGUIElement me = (MathGUIElement) c;
                me.adjustBaseLine();
            }
        }
    }

    public void focusGained(FocusEvent e) {
        context.setCurrentElement(this);
    }

    public void focusLost(FocusEvent e) {
    }

    public void convertToCode(StringBuffer sb) {
        for (MathGUIElement me : this.mathComponents) {
            me.convertToCode(sb);
        }
    }

    public void updateLayout() {
        SwingUtilities.invokeLater(this);
        //run();
    }

    public void run() {
        adjustBaseLine();
        invalidateAll();
        doLayout();
        
        //invalidateAll();
        //doLayout();
        repaint();
    }
    private boolean isGroup = false;

    public boolean isGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    /**
     * Create a lightweight object representation of the formula.
     * @return the MathFormula object.
     */
    public MathElement createFormula() {

        // only create a formula if the number of children is one.
        if (mathComponents.size() == 1) {
            MathElement root = getFirstChild().createFormula();
            return root;
        }
        return null;
    }

    public void removeAncestorOf(MathGUIElement current) {
        Component currentC = (Component) current;
        Component ancestor = null;
        for (MathGUIElement element : this.mathComponents) {
            Component c = (Component) element;
            if (isAncestor(c, currentC)) {
                ancestor = c;
            }
        }
        if ( ancestor != null ){
            this.removeMathField((MathGUIElement)ancestor);
            this.repaint();
        }
    }

    private boolean isAncestor(Component ancestor, Component child) {
        Component parent = child.getParent();
        while (this != parent) {
            if (parent == ancestor) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
