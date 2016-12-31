/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.math.Ray;

/**
 *
 * @author huliqing
 */
public class ScaleControlObj extends ControlObj {

//    private static final Logger LOG = Logger.getLogger(ScaleControlObj.class.getName());
    
    private final ScaleAxis controlAxis; 
    
    public ScaleControlObj() {
        controlAxis = new ScaleAxis();
        controlAxis.addControl(new AutoScaleControl());
        attachChild(controlAxis);
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible); 
        // 优化：当物体激活时，强制刷新一次，计算大小。
        controlAxis.getControl(AutoScaleControl.class).forceUpdate();
    }

    @Override
    public void setAxisVisible(boolean visible) {
        super.setAxisVisible(visible); 
        controlAxis.getCenter().setCullHint(visible ? CullHint.Never : CullHint.Always);
    }
    
    @Override
    public AxisNode getAxisX() {
        return controlAxis.getAxisX();
    }

    @Override
    public AxisNode getAxisY() {
        return controlAxis.getAxisY();
    }

    @Override
    public AxisNode getAxisZ() {
        return controlAxis.getAxisZ();
    }
    
    public boolean isPickCenter(Ray ray) {
        return controlAxis.getCenter().getWorldBound().intersects(ray);
    }
    
}
