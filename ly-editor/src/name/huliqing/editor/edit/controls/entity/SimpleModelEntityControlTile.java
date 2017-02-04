/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.controls.entity;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 普通模型的操作物体，直接操作实体本身的模型节点, 使用这类操作物体时必须确保实体存在网格模型，
 * 否则物体将无法通过鼠标来选择。
 * @author huliqing
 */
public class SimpleModelEntityControlTile extends EntityControlTile<Entity> {

    @Override
    public Spatial getControlSpatial() {
        return target.getSpatial();
    }

    @Override
    public void initialize(Node parent) {
        super.initialize(parent); 
        updateState();
    }

    @Override
    public void updateState() {
        super.updateState(); 
        // 当选择的是地形的时候，,注意：地形在载入的时候需要重新设置材质，使用地形中的所有分块指定同一个材质实例，
        // 否则指定刷到特定的材质上。
        if (target.getSpatial() instanceof Terrain) {
            Terrain terrain = (Terrain) target.getSpatial();
            target.getSpatial().setMaterial(terrain.getMaterial());
        }
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
