package dae.fxcreator.node.graph.math;

/**
 *
 * @author Koen
 */
public interface MathContext {
    public void setCurrentElement(MathGUIElement element);
    public MathGUIElement getCurrentElement();
    public void elementChanged(MathGUIElement element);
}
