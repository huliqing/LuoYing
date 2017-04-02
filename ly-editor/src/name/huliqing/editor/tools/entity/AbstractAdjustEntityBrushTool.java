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
package name.huliqing.editor.tools.entity;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.tiles.AutoScaleControl;
import name.huliqing.editor.toolbar.EntityBrushToolbar;
import name.huliqing.editor.tools.IntegerValueTool;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ToggleTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.luoying.object.entity.TerrainEntity;

/**
 * 没有特别的方法，只是把实体刷工具标记为ToggleTool类型的工具,方便JFX工具渲染
 * @author huliqing
 */
public abstract class AbstractAdjustEntityBrushTool extends AbstractEntityBrushTool 
        implements ToggleTool<SimpleJmeEdit, EntityBrushToolbar>, ValueChangedListener<Number>{
    private final static String EVENT_ACTION = "actionEvent";
    
    // 工具的基本大小
    private final float baseSize = 0.05f;
    
    private boolean modifying;
//    private final float toolModifyRate = 0.05f;
    private final float toolModifyRate = 0.2f;
    private float lastModifyTime; 
    
    protected Geometry controlObj;
    protected NumberValueTool sizeTool;
    protected IntegerValueTool densityTool;

    public AbstractAdjustEntityBrushTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public void initialize(SimpleJmeEdit edit, EntityBrushToolbar toolbar) {
        super.initialize(edit, toolbar);
        
        sizeTool = toolbar.getSizeTool();
        densityTool = toolbar.getDensityTool();
        
        if (controlObj == null) {
            controlObj = createMesh();
            controlObj.addControl(new AutoScaleControl());
        }
        edit.getEditRoot().attachChild(controlObj);
        updateMarkerSize(sizeTool.getValue().floatValue());
        
        sizeTool.addValueChangeListener(this);
    }

    @Override
    public void cleanup() {
        if (!initialized) 
            return;
        
        if (modifying) {
            doEndAction();
        }
        controlObj.removeFromParent();
        sizeTool.removeValueChangeListener(this);
        super.cleanup(); 
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        Vector3f pos = getTerrainCollisionPoint();
        if (pos != null && controlObj != null) {
            controlObj.setLocalTranslation(pos);
        }
        
        if (modifying) {
            lastModifyTime += tpf;
            if (lastModifyTime >= toolModifyRate) {
                lastModifyTime = 0;
                doAction();
            }
        }
    }
    
    public JmeEvent bindActionEvent() {
        return this.bindEvent(EVENT_ACTION);
    }

    @Override
    protected void onToolEvent(Event e) {
        if (e.getName().equals(EVENT_ACTION)) {
            if (e.isMatch()) {
                modifying = true;
                lastModifyTime = 0;
                doAction();
                e.setConsumed(true);
            } else {
                modifying = false;
                doEndAction();
            }
        }
    }
    
    /**
     * 处理工具行为
     */
    protected abstract void doAction();
    
    /**
     * 结束工具行为,例如保存历史记录。
     */
    protected abstract void doEndAction();
    
    @Override
    public void onValueChanged(ValueTool<Number> vt, Number oldValue, Number newValue) {
        updateMarkerSize(newValue.floatValue());
    }
    
    private void updateMarkerSize(float size) {
        AutoScaleControl asc = controlObj.getControl(AutoScaleControl.class);
        asc.setSize(size * baseSize); // 0.15为基本大小
    }

    private Geometry createMesh() {
        Geometry marker = new Geometry();
        marker.setMesh(new Sphere(8, 8, 1));
        Material mat = new Material(editor.getAssetManager(), AssetConstants.MATERIAL_UNSHADED);
        mat.getAdditionalRenderState().setWireframe(true);
        marker.setMaterial(mat);
        marker.setLocalTranslation(0,0,0);
        mat.setColor("Color", ColorRGBA.Green);
        return marker;
    }
    
    /**
     * 获取当前编辑场景中的被选择的地形物体，如果当前未选择任何物体或者这个物体不是Terrain物体，则将返回null.
     * @return 
     */
    protected TerrainEntity getSelectTerrain() {
        ControlTile selected = edit.getSelected();
        if (selected == null)
            return null;
        
        if (!(selected.getTarget() instanceof TerrainEntity)) {
            return null;
        }
        return (TerrainEntity) selected.getTarget();
    }
    
    protected Vector3f getTerrainCollisionPoint() {
        TerrainEntity eso = getSelectTerrain();
        if (eso == null)
            return null;
        
        Vector3f result = PickManager.pickPoint(editor.getCamera(), editor.getInputManager().getCursorPosition(), eso.getSpatial());
        return result;
    }
}
