/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.controls;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.editor.edit.SimpleJmeEdit;

/**
 *
 * @author huliqing
 */
public class SpatialControlTile extends ControlTile<Spatial, SimpleJmeEdit> {

    @Override
    public Spatial getControlSpatial() {
        return target;
    }

    @Override
    protected void onLocationUpdated(Vector3f locaton) {
    }

    @Override
    protected void onRotationUpdated(Quaternion rotation) {
    }

    @Override
    protected void onScaleUpdated(Vector3f scale) {
    }

    @Override
    protected void onChildUpdated(ControlTile childUpdated, Type type) {
    }


}
