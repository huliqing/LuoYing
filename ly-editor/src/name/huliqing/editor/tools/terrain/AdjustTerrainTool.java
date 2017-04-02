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
package name.huliqing.editor.tools.terrain;

import com.jme3.gde.terraineditor.tools.AbstractTerrainToolAction;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.tiles.AutoScaleControl;
import name.huliqing.editor.toolbar.TerrainToolbar;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ToggleTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.luoying.manager.PickManager;

/**
 * 地形工具的基类
 * @author huliqing
 */
public abstract class AdjustTerrainTool extends AbstractTerrainTool 
        implements ToggleTool<SimpleJmeEdit, TerrainToolbar>, ValueChangedListener<Number>{
    private static final Logger LOG = Logger.getLogger(AbstractTerrainTool.class.getName());
    
    // 工具的基本大小
    private final float baseSize = 0.05f;
    
    private boolean modifying;
    // how frequently (in seconds) it should update to throttle down the tool effect
    private final float toolModifyRate = 0.05f; 
    // last time the tool executed
    private float lastModifyTime; 
    
    private Geometry controlObj;
    private NumberValueTool radiusTool;
    private NumberValueTool weightTool;
    
    private final static String EVENT_ACTION = "actionEvent";
    private final List<AbstractTerrainToolAction> actions = new ArrayList();
    
    public AdjustTerrainTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public void initialize(SimpleJmeEdit edit, TerrainToolbar toolbar) {
        super.initialize(edit, toolbar);
        
        radiusTool = toolbar.getSizeTool();
        weightTool = toolbar.getWeightTool();
        
        if (controlObj == null) {
            controlObj = createMesh();
            controlObj.addControl(new AutoScaleControl());
        }
        edit.getEditRoot().attachChild(controlObj);
        updateMarkerSize(radiusTool.getValue().floatValue());
        
        radiusTool.addValueChangeListener(this);
    }

    @Override
    public void cleanup() {
        if (!initialized) 
            return;
        
        if (modifying) {
            doEndAction();
        }
        controlObj.removeFromParent();
        radiusTool.removeValueChangeListener(this);
        super.cleanup(); 
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
    
    protected void doAction() {
        float radius = controlObj.getWorldScale().x;
        float weight = weightTool.getValue().floatValue();
        if (radius <= 0 || weight == 0) {
            return;
        }
        EntityControlTile terrain = getTerrainControl(); 
        if (terrain == null) 
            return;
        
        int radiusStepsX = (int)(radius / terrain.getTarget().getSpatial().getLocalScale().x);
        if (radiusStepsX <= 0) {
            LOG.log(Level.WARNING, "Radius to small! radius={0} terrainScale={1}, radiusSteps={2}"
                    , new Object[] {radius, terrain.getTarget().getSpatial().getLocalScale().x, radiusStepsX});
            return;
        }
        
        AbstractTerrainToolAction action = createAction(radius, weight, controlObj.getWorldTranslation(), terrain);
        if (action != null) {
            action.doAction();
            actions.add(action);
        }
    }
    
    protected void doEndAction() {
        if (actions.isEmpty()) {
            return;
        }
        // record undo action
        List<AbstractTerrainToolAction> actionList = new ArrayList(actions);
        edit.addUndoRedo(new ToolActionUndoRedo(actionList));
        actions.clear();
        edit.setModified(true);
    }
    
    @Override
    public void onValueChanged(ValueTool<Number> vt, Number oldValue, Number newValue) {
        updateMarkerSize(newValue.floatValue());
    }
    
    protected abstract AbstractTerrainToolAction createAction(float radius, float weight, Vector3f markerWorldLoc
            , EntityControlTile terrain);
    
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

    private void updateMarkerSize(float size) {
        AutoScaleControl asc = controlObj.getControl(AutoScaleControl.class);
        asc.setSize(size * baseSize); // 0.15为基本大小
    }
    
    /**
     * 获取鼠标在地形上的点击点，如果不存在这个点则返回null.
     * @return 
     */
    private Vector3f getTerrainCollisionPoint() {
        EntityControlTile eso = getTerrainControl();
        if (eso == null)
            return null;
        
        Vector3f result = PickManager.pickPoint(editor.getCamera(), editor.getInputManager().getCursorPosition(), eso.getTarget().getSpatial());
        return result;
    }
    
    private class ToolActionUndoRedo implements UndoRedo {
        
        private final List<AbstractTerrainToolAction> actionList;
        
        public ToolActionUndoRedo(List<AbstractTerrainToolAction> actionList) {
            this.actionList = actionList;
        }
        
        @Override
        public void undo() {
            for (int i = actionList.size() - 1; i >= 0; i--) {
                AbstractTerrainToolAction action = actionList.get(i);
                action.undo();
            }
        }

        @Override
        public void redo() {
            for (int i = 0; i < actionList.size(); i++) {
                AbstractTerrainToolAction action = actionList.get(i);
                action.redo();
            }
        }
        
    }
}
