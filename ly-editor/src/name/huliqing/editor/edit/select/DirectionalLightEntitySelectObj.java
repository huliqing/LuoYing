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
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.scene.SceneEditForm;
import name.huliqing.editor.tiles.AutoScaleControl;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.object.entity.impl.DirectionalLightEntity;
import name.huliqing.luoying.shape.QuadXYC;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * 直射光Entity的操作物体
 * @author huliqing
 */
public class DirectionalLightEntitySelectObj extends EntitySelectObj<DirectionalLightEntity> {

    private final Node controlObj = new Node();
    private final Spatial flag;
    private final Spatial line;
    
    public DirectionalLightEntitySelectObj() {
        flag = createFlag(AssetConstants.INTERFACE_ICON_SUN);
        controlObj.attachChild(flag);
        
        line = createLine();
        controlObj.attachChild(line);
        
        // 添加一个box，这样更容易被选中
        controlObj.attachChild(createBox()); 
        
        AutoScaleControl asc = new AutoScaleControl(0.05f);
        controlObj.addControl(asc);
    }
    
    @Override
    public void initialize(SceneEditForm form) {
        form.getEditRoot().attachChild(controlObj);
        controlObj.setLocalScale(10);
        
        Quaternion rot = line.getLocalRotation();
        rot.lookAt(entity.getDirection(), Vector3f.UNIT_Y);
        setLocalRotation(rot);
    }

    @Override
    public void cleanup() {
        controlObj.removeFromParent();
    }

    @Override
    public void setLocalScale(Vector3f scale) {
        super.setLocalScale(scale); 
    }

    @Override
    public void setLocalRotation(Quaternion rotation) {
        super.setLocalRotation(rotation); 

        Vector3f dir = this.entity.getDirection().set(Vector3f.UNIT_Z);
        rotation.mult(dir, dir);
        this.entity.setDirection(dir);
        line.setLocalRotation(rotation);
    }

    @Override
    public void setLocalTranslation(Vector3f location) {
        super.setLocalTranslation(location); 
        controlObj.setLocalTranslation(location);
    }
    
    @Override
    public Float distanceOfPick(Ray ray) {
        Float dist = PickManager.distanceOfPick(ray, controlObj);
        return dist;
    }
    
    private Spatial createFlag(String icon) {
        Material mat = MaterialUtils.createUnshaded();
        mat.setTexture("ColorMap", Jfx.getJmeApp().getAssetManager().loadTexture(icon));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        Geometry flagObj = new Geometry("DirectionalLightEntitySelectObj", new QuadXYC(1, 1));
        flagObj.setMaterial(mat);
//        flagObj.setLocalTranslation(-0.5f, -0.5f, 0);
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
