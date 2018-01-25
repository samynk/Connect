package dae.fxcreator.node.graph.math;

import dae.fxcreator.node.graphmath.MathVariable;
import dae.fxcreator.node.graphmath.MathElement;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author Koen
 */
public class MathField extends JTextField implements FocusListener, MathGUIElement, KeyListener {

    private Border selectedBorder;
    private Border emptyBorder;
    private Border emptyVectorBorder;
    private Border selectedVectorBorder;
    private MathContainer parent;
    private boolean selected;
    private MathContext context;
    private static char currentChar = 'a';
    private static int currentIndex = 0;
    private boolean isVector = false;
    
    private boolean locked = false;

    public MathField() {
        this("");
        setOpaque(false);
    }

    public MathField(String text) {
        if (text == null || text.length() == 0) {
            text = new String();
            text += currentChar;
            if (currentIndex > 0) {
                text += currentIndex;
            }
            currentChar++;
            if (currentChar == 'z') {
                currentChar = 'a';
                currentIndex++;
            }
        }
        selectedBorder = BorderFactory.createLineBorder(Color.orange, 1);
        emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

        Border empty = BorderFactory.createEmptyBorder(5, 0, 0, 0);
        selectedVectorBorder = BorderFactory.createCompoundBorder(selectedBorder, empty);
        emptyVectorBorder = BorderFactory.createEmptyBorder(6, 1, 1, 1);


        setBorder(emptyBorder);
        addFocusListener(this);
        addKeyListener(this);
        this.setText(text);
        this.setHorizontalAlignment(JTextField.CENTER);

        formatText();
        setOpaque(false);
    }

    public void setLocked() {
        this.locked = true;
    }

    public boolean isLocked() {
        return locked;
    }

    public void focusGained(FocusEvent e) {
        this.setBorder(selectedBorder);
        context.setCurrentElement(this);
        selectAll();
    }

    public void focusLost(FocusEvent e) {
        if (isVector) {
            this.setBorder(emptyVectorBorder);
        } else {
            this.setBorder(emptyBorder);
        }
    }

    @Override
    public MathContainer getParentContainer() {
        return parent;
    }

    @Override
    public void setParentContainer(MathContainer parent) {
        this.parent = parent;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            if (isVector) {
                this.setBorder(selectedVectorBorder);
            } else {
                this.setBorder(selectedBorder);
            }
        } else {
            if (isVector) {
                this.setBorder(emptyVectorBorder);
            } else {
                this.setBorder(emptyBorder);
            }
        }
        invalidate();
        repaint();
    }

    private void formatText() {
        String text = this.getText();
        try {
            int number = Integer.parseInt(text);
            this.setForeground(Color.BLUE);
        } catch (NumberFormatException ex) {
            this.setForeground(Color.black);
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setMathContext(MathContext context) {
        this.context = context;
    }

    public void removeFromParent() {
        if (parent != null) {
            parent.removeMathField(this);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        context.elementChanged(this);
        formatText();
    }

    public int getBaseLine() {
        return this.getBaseline(getWidth(), getMinimumSize().height);
    }

    public void adjustBaseLine() {
    }

    public void convertToCode(StringBuffer sb) {
        String text = this.getText();
        sb.append(text);
    }

    /**
     * @return the isVector
     */
    public boolean isIsVector() {
        return isVector;
    }

    /**
     * @param isVector the isVector to set
     */
    public void setIsVector(boolean isVector) {
        this.isVector = isVector;
        setSelected(this.selected);
        if (context != null) {
            context.elementChanged(this);
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isVector) {
            g.drawLine(0, 6, getWidth(), 6);
            g.drawLine(getWidth(), 6, getWidth() - 7, 4);
            g.drawLine(getWidth(), 6, getWidth() - 7, 8);
        }
    }

    @Override
    public void setText(String text) {
        if (text.length() == 0) {
            text = "a";
        }
        super.setText(text);
        if (context != null) {
            context.elementChanged(this);
        }
    }

    public void toggleIsVector() {
        this.isVector = !isVector;
        setSelected(this.selected);
        context.elementChanged(this);
        repaint();
    }

    public boolean isGroup() {
        return false;
    }

    /**
     * Creates a MathElement from this MathField object.
     *
     * @return
     */
    public MathElement createFormula() {
        MathVariable mathvar = new MathVariable();
        mathvar.setIsVector(this.isVector);
        mathvar.setVarName(this.getText());
        return mathvar;
    }
}
