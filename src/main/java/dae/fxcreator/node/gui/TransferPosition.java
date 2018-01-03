/*
 * TransferPosition.java
 *
 * Created on 15 december 2005, 18:05
 */

package dae.fxcreator.node.gui;

/**
 *
 * @author samynk
 */
public interface TransferPosition {
    public void transferDX(float dx);
    public void transferDY(float dy);

    public void transferDP(float dx, float dy);
}