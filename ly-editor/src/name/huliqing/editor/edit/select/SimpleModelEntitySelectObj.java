/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.select;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 普通模型的操作物体，直接操作实体本身的模型节点, 使用这类操作物体时必须确保实体存在网格模型，
 * 否则物体将无法通过鼠标来选择。
 * @author huliqing
 */
public class SimpleModelEntitySelectObj extends EntitySelectObj<Entity> {

    @Override
    public Spatial getControlSpatial() {
        return target.getSpatial();
    }

    @Override
    protected void onLocationUpdated(Vector3f location) {
        RigidBodyControl control = target.getSpatial().getControl(RigidBodyControl.class);
        if (control != null) {
            control.setPhysicsLocation(location);
        }
        BetterCharacterControl character = target.getSpatial().getControl(BetterCharacterControl.class);
        if (character != null) {
            character.warp(location);
        }
        target.getSpatial().setLocalTranslation(location);
        target.updateDatas();
        notifyPropertyChanged("location", location);
    }

    @Override
    protected void onRotationUpdated(Quaternion rotation) {
        RigidBodyControl control = target.getSpatial().getControl(RigidBodyControl.class);
        if (control != null) {
            control.setPhysicsRotation(rotation);
        }
        target.getSpatial().setLocalRotation(rotation);
        target.updateDatas();
        notifyPropertyChanged("rotation", rotation);
    }

    @Override
    protected void onScaleUpdated(Vector3f scale) {
        target.getSpatial().setLocalScale(scale);
        target.updateDatas();
        notifyPropertyChanged("scale", scale);
    }
    
}
