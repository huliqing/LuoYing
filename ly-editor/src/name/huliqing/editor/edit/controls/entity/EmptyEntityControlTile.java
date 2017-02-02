/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.controls.entity;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 空的操作物体，相关的Entity不能被直接操作，该类主要用于所有那些不能直接可视化操作的实体Entity
 * @author huliqing
 * @param <T>
 */
public class EmptyEntityControlTile <T extends Entity> extends EntityControlTile<T> {

    @Override
    public Spatial getControlSpatial() {
        return null;
    }

    @Override
    protected void onLocationUpdated(Vector3f location) {
        // ignore
    }

    @Override
    protected void onRotationUpdated(Quaternion rotation) {
        // ignore
    }

    @Override
    protected void onScaleUpdated(Vector3f scale) {
        // ignore
    }
    
}
