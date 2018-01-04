package dae.fxcreator.node.graph.uisetting;

import dae.fxcreator.io.templates.Setting;

/**
 * Interface that all Visualizers must implement.
 * @author Koen
 */
public interface Visualizer {
    public Setting getSetting();

    public void updateVisual();
    public void updateVisual(Setting s);
}
