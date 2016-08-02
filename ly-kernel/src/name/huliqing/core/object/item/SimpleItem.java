/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.item;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * @deprecated 20160310不再使用
 * 简单的物体类型的封装类。
 * @author huliqing
 */
public class SimpleItem implements Item{
    
    private Spatial spatial;
    
    // 逻辑控制器，只在给物体添加了逻辑的时候才需要创建logicControl的实例，并添
    // 加给spatial,否则不需要创建该实例，以节省资源
    private ItemLogicControl logicControl;
    
    public SimpleItem() {}
    
    public SimpleItem(Spatial spatial) {
        this.spatial = spatial;
    }

    @Override
    public Spatial getModel() {
        return this.spatial;
    }

    @Override
    public void addLogic(ItemLogic logic) {
        // 只有添加了逻辑才需要创建control.
        logic.setSelf(this);
        if (logicControl == null) {
            logicControl = new ItemLogicControl();
            spatial.addControl(logicControl);
        }
        logicControl.addLogic(logic);
    }

    @Override
    public void clearLogics() {
        spatial.removeControl(logicControl);
        if (logicControl != null) {
            logicControl.clearLogics();
        }
    }

    /**
     * 使用该方法来获得物体正确的世界坐标，部分物体由于加了rigidBodyControl
     * 而可能导致getWorldTranslation不能始终获得正确的位置。该方法处理该问
     * 题，以始终返回正确的世界坐标。
     * @return 
     */
    @Override
    public Vector3f getLocation() {
        RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
        if (rbc != null) {
            return rbc.getPhysicsLocation();
        } else {
            return spatial.getWorldTranslation();
        }
    }

    @Override
    public void setLocation(Vector3f location) {
        RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
        if (rbc != null) {
            rbc.setPhysicsLocation(location);
        }
        // 很多情况下必须同时设置localTransaction，即使设置了physicsLocation.
        // 否则可能无法实时获得正确位置。
        spatial.setLocalTranslation(location);
    }

    @Override
    public void setRotation(Quaternion quaternion) {
        RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
        if (rbc != null) {
            rbc.setPhysicsRotation(quaternion);
        }
    }

    
}
