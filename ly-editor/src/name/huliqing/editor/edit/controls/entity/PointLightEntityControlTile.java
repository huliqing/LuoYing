/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.editor.edit.controls.entity;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Box;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.tiles.AutoScaleControl2;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.object.entity.impl.PointLightEntity;
import name.huliqing.luoying.shape.QuadXYC;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * PointLightEntity的操作物体
 * @author huliqing
 */
public class PointLightEntityControlTile extends EntityControlTile<PointLightEntity> {

    private final Node controlSpatial = new Node();
    
    public PointLightEntityControlTile() {
        Node flag = new Node("flag");
        flag.attachChild(createSunFlag(AssetConstants.INTERFACE_ICON_LIGHT_POINT));
        flag.attachChild(createBox()); // 添加一个box，这样更容易被选中
        flag.addControl(new AutoScaleControl2(0.05f));
        controlSpatial.attachChild(flag); 
    }
    
    @Override
    public void initialize(SimpleJmeEdit edit) {
        super.initialize(edit);
        edit.getEditRoot().attachChild(controlSpatial);
        updateState();
    }
    
    @Override
    public void updateState() {
        super.updateState();
        controlSpatial.setLocalTranslation(target.getLight().getPosition());
        controlSpatial.setLocalScale(target.getLight().getRadius());
    }

    @Override
    public void cleanup() {
        controlSpatial.removeFromParent();
        super.cleanup();
    }

    @Override
    protected void onLocationUpdated(Vector3f location) {
        controlSpatial.setLocalTranslation(location);
        target.getSpatial().setLocalTranslation(location);
        target.getLight().setPosition(location);
        target.updateDatas();
        notifyPropertyChanged("location", target.getSpatial().getLocalTranslation());
    }
    
    @Override
    protected void onRotationUpdated(Quaternion rotation) {
        // ignore
    }

    @Override
    protected void onScaleUpdated(Vector3f scale) {
        // 找出最大的一个缩放
        float radius = Math.max(scale.x, scale.y);
        radius = Math.max(radius, scale.z);
        if (radius < 0) {
            radius = 0.0001f;
        }
        controlSpatial.setLocalScale(radius);
        target.getSpatial().setLocalScale(radius);
        target.getLight().setRadius(radius);
        target.updateDatas();
        notifyPropertyChanged("radius", radius);
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
    
}
