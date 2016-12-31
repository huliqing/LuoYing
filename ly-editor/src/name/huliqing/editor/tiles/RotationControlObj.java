/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

/**
 * 旋转控制
 * @author huliqing
 */
public class RotationControlObj extends ControlObj {
    
    private final RotationAxis controlAxis;
    
    public RotationControlObj() {
        controlAxis = new RotationAxis();
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

}
