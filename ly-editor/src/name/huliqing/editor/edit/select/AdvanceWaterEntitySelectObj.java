/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.select;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.util.TempVars;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.object.entity.impl.AdvanceWaterEntity;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * 高级水体的操作物体
 * @author huliqing
 */
public class AdvanceWaterEntitySelectObj extends EntitySelectObj<AdvanceWaterEntity>{
    
    private final Spatial pickObj = createControlObj(ColorRGBA.Red);
    private final Spatial controlObj = createControlObj(ColorRGBA.Blue);
    private final float MAX_RADIUS = Integer.MAX_VALUE;
    
    private final float[] tempAngles  = new float[3];
    
    @Override
    public void initialize(Node parent) {
        super.initialize(parent);
        parent.attachChild(controlObj);
        parent.attachChild(pickObj);
        Vector3f location = new Vector3f(target.getSpatial().getLocalTranslation());
        location.setY(target.getWaterHeight());
        setLocalTranslation(location);
        if (target.getCenter() != null) {
            setLocalScale(new Vector3f(target.getRadius(), target.getRadius(), target.getRadius()));
        } else {
            setLocalScale(new Vector3f(MAX_RADIUS, MAX_RADIUS, MAX_RADIUS));
        }
        Quaternion qua = new Quaternion();
        Vector3f windDir = new Vector3f(target.getWindDirection().x, 0, target.getWindDirection().y).normalizeLocal();
        qua.lookAt(windDir, Vector3f.UNIT_Y);
        setLocalRotation(qua);
    }
    
    @Override
    public void cleanup() {
        controlObj.removeFromParent();
        pickObj.removeFromParent();
        super.cleanup();
    }

    @Override
    public Float pickCheck(Ray ray) {
        Vector3f point = PickManager.pickPoint(ray, pickObj);
        if (point == null) {
            return null;
        }
        // 点击的时候是用pickObj,但是控制的时候是用controlObj,所以这里需要把controlObj移动到点击处的位置。
        if (target.getCenter() == null) {
            controlObj.setLocalTranslation(point);
        } else {
            controlObj.setLocalTranslation(pickObj.getLocalTranslation());
        }
        Float dist = point.distance(ray.getOrigin());
        return dist;
    }
    
    @Override
    protected void onLocationUpdated(Vector3f location) {
        pickObj.setLocalTranslation(location);
        target.getSpatial().setLocalTranslation(location);
        if (target.getCenter() != null) {
            target.setWaterHeight(location.y);
            target.setCenter(new Vector3f(location));
            target.updateDatas();
            notifyPropertyChanged("location", location);
            notifyPropertyChanged("waterHeight", location.y);
            notifyPropertyChanged("center", target.getCenter());
        } else {
            // 不能改自动改变这个参数，这会让海水变成有限的，而用户可能不一定要通过移动海水来改变类型。
            target.setWaterHeight(location.y);
            target.updateDatas();
            notifyPropertyChanged("location", location);
            notifyPropertyChanged("waterHeight", location.y);
        }
    }
    
    @Override
    protected void onRotationUpdated(Quaternion rotation) {
        rotation.toAngles(tempAngles);
        // 去掉x和z轴的旋转
        tempAngles[0] = 0;
        tempAngles[2] = 0;
        TempVars tv = TempVars.get();
        Quaternion qua = tv.quat1;
        qua.fromAngles(tempAngles);
        target.getSpatial().setLocalRotation(qua);
        
        Vector3f windDir = tv.vect1.set(0,0,1);
        qua.mult(windDir, windDir);
        target.setWindDirection(new Vector2f(windDir.x, windDir.z).normalizeLocal());
        tv.release();
        
        target.updateDatas();
        notifyPropertyChanged("rotation", qua);
        notifyPropertyChanged("windDirection", target.getWindDirection());
    }
    
    @Override
    protected void onScaleUpdated(Vector3f scale) {
        if (target.getCenter() != null) {
            controlObj.setLocalScale(scale); // controlObj不需要缩放
            pickObj.setLocalScale(scale.x);
            target.setRadius(scale.x);
            target.updateDatas();
            notifyPropertyChanged("radius", scale.x);
        } else {
            // pickObj需要用于点击获取海水对象，所以必须设置尽量和海水一样大小
            controlObj.setLocalScale(1,1,1);
            pickObj.setLocalScale(MAX_RADIUS);
        }
    }

    @Override
    public Spatial getControlSpatial() {
        return controlObj;
    }
    
    private Spatial createControlObj(ColorRGBA color) {
        Quad quad = new Quad(1,1);
        Geometry geo = new Geometry("", quad);
        geo.rotate(-FastMath.HALF_PI, 0, 0);
        geo.setLocalTranslation(-0.5f, 0, 0.5f);
        Material mat = MaterialUtils.createUnshaded();
        if (color != null) {
            mat.setColor("Color", color);
        }
        geo.setMaterial(mat);
        geo.setCullHint(Spatial.CullHint.Always);
        
        Node root = new Node();
        root.attachChild(geo);
        return root;
    }

}
