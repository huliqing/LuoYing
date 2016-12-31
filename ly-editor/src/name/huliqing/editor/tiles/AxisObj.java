/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.math.ColorRGBA;

/**
 *
 * @author huliqing
 */
public interface AxisObj {
    
    public final static ColorRGBA AXIS_COLOR_X = new ColorRGBA(1.0f, 0.1f, 0.1f, 1.0f);
    public final static ColorRGBA AXIS_COLOR_Y = new ColorRGBA(0.1f, 1.0f, 0.1f, 1.0f);
    public final static ColorRGBA AXIS_COLOR_Z = new ColorRGBA(0.1f, 0.1f, 1.0f, 1.0f);
    
    AxisNode getAxisX();
    
    AxisNode getAxisY();
    
    AxisNode getAxisZ();
    
}
