/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.select;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.tiles.AutoScaleControl;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.object.entity.impl.DirectionalLightEntity;
import name.huliqing.luoying.shape.QuadXYC;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * 直射光Entity的操作物体
 * @author huliqing
 */
public class DirectionalLightEntitySelectObj extends EntitySelectObj<DirectionalLightEntity> {

    private final Node controlSpatial = new Node();
    private final Spatial flag;
    private final Spatial line;
    
    public DirectionalLightEntitySelectObj() {
        flag = createSunFlag(AssetConstants.INTERFACE_ICON_SUN);
        controlSpatial.attachChild(flag);
        
        line = createLine();
        controlSpatial.attachChild(line);
        
        // 添加一个box，这样更容易被选中
        controlSpatial.attachChild(createBox()); 
        
        AutoScaleControl asc = new AutoScaleControl(0.05f);
        controlSpatial.addControl(asc);
    }
    
    @Override
    public void initialize(Node form) {
        super.initialize(form);
        form.attachChild(controlSpatial);
        controlSpatial.setLocalScale(10);
        
        Quaternion rot = controlSpatial.getLocalRotation();
        rot.lookAt(target.getDirection(), Vector3f.UNIT_Y);
        setLocalRotation(rot);
    }

    @Override
    public void cleanup() {
        controlSpatial.removeFromParent();
        super.cleanup();
    }

    @Override
    protected void onLocationUpdated(Vector3f location) {
        target.getSpatial().setLocalTranslation(location);
    }

    @Override
    protected void onRotationUpdated(Quaternion rotation) {
        Vector3f dir = target.getDirection().set(Vector3f.UNIT_Z);
        rotation.mult(dir, dir);
        this.target.setDirection(dir);
        notifyPropertyChanged("direction", dir);
    }

    @Override
    protected void onScaleUpdated(Vector3f scale) {
        // ignore
    }

    @Override
    public Spatial getControlSpatial() {
        return controlSpatial;
    }
    
    private Spatial createSunFlag(String icon) {
        Material mat = MaterialUtils.createUnshaded();
        mat.setTexture("ColorMap", Jfx.getJmeApp().getAssetManager().loadTexture(icon));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        Geometry flagObj = new Geometry("DirectionalLightEntitySelectObj", new QuadXYC(1, 1));
        flagObj.setMaterial(mat);
        flagObj.setQueueBucket(RenderQueue.Bucket.Translucent);
        
        // 让flag始终朝向镜头
        BillboardControl bc = new BillboardControl();
        bc.setAlignment(BillboardControl.Alignment.Screen);
        flagObj.addControl(bc);
        
        return flagObj;
    }
    
    private Spatial createBox() {
        Box box = new Box(0.5f, 0.5f, 0.5f);
        Geometry boxGeo = new Geometry("", box);
        boxGeo.setMaterial(MaterialUtils.createUnshaded());
        boxGeo.setCullHint(Spatial.CullHint.Always);
        return boxGeo;
    }
    
    private Spatial createLine() {
        Geometry geo = new Geometry("line", new Line(new Vector3f(), new Vector3f(0, 0, 1f)));
        Material mat = MaterialUtils.createUnshaded(ColorRGBA.Black);
        geo.setMaterial(mat);
        geo.setLocalScale(1000);
        return geo;
    }

}
