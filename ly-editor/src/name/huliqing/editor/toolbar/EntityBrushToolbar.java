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
package name.huliqing.editor.toolbar;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.math.Vector3f;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.FloatValueTool;
import name.huliqing.editor.tools.IntegerValueTool;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ParamsTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.Vector3fValueTool;
import name.huliqing.editor.tools.entity.EraseTool;
import name.huliqing.editor.tools.entity.InstancedTool;
import name.huliqing.editor.tools.entity.PaintTool;
import name.huliqing.editor.tools.entity.SourceTool;

/**
 * 场景实体刷工具栏
 * @author huliqing
 */
public class EntityBrushToolbar extends EditToolbar<SimpleJmeEdit> {
    
    // 笔刷大小
    private FloatValueTool sizeTool;
    // density 密度int
    private IntegerValueTool densityTool;
    // 实体源
    private SourceTool sourceTool;
    
    private PaintTool paintTool;
    private ParamsTool paintToolParams;
    // Minimum height
    private FloatValueTool minHeight;
    // 保持法向量，即让刷到地面上的物体的向上方向与地面的法向量相同。
    private BooleanValueTool useNormal;
    // 位置偏移
    private Vector3fValueTool locationOffset;
    // scale adjust
    private Vector3fValueTool scaleMinAdjust;
    private Vector3fValueTool scaleMaxAdjust;
    // rotation adjust
    private Vector3fValueTool rotationMinAdjust;
    private Vector3fValueTool rotationMaxAdjust;
    // Instanced工具,用于实体刷支持instanced功能
    private InstancedTool instancedTool;
    
    
    private EraseTool eraseTool;

    public EntityBrushToolbar(SimpleJmeEdit edit) {
        super(edit);
    }

    @Override
    public String getName() {
        return Manager.getRes(ResConstants.EDIT_TOOLBAR_ENTITY_BRUSH);
    }

    @Override
    public void initialize() {
        super.initialize(); 
        sizeTool = new FloatValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_SIZE), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_SIZE_TIP), null);
        densityTool = new IntegerValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_DENSITY), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_DENSITY_TIP), null); 
        sourceTool = new SourceTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_SOURCE), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_SOURCE_TIP), null);
        
        paintTool = new PaintTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_PAINT)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_PAINT_TIP)
                , AssetConstants.INTERFACE_ICON_PAINT); 
        minHeight = new FloatValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_MIN_HEIGHT), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_MIN_HEIGHT_TIP), null); 
        useNormal = new BooleanValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_FACE_NORMAL), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_FACE_NORMAL_TIP), null); 
        locationOffset = new Vector3fValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_LOCATION_OFFSET), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_LOCATION_OFFSET_TIP), null); 
        scaleMinAdjust = new Vector3fValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_SCALE_MIN), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_SCALE_MIN_TIP), null); 
        scaleMaxAdjust = new Vector3fValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_SCALE_MAX), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_SCALE_MAX_TIP), null);
        rotationMinAdjust = new Vector3fValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_ROTATION_MIN), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_ROTATION_MIN_TIP), null); 
        rotationMaxAdjust = new Vector3fValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_ROTATION_MAX), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_ROTATION_MAX_TIP), null); 
        instancedTool = new InstancedTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_INSTANCED), Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_INSTANCED_TIP), null);
        paintToolParams = new ParamsTool("", "", null);
        paintToolParams.addChild(minHeight);
        paintToolParams.addChild(useNormal);
        paintToolParams.addChild(locationOffset);
        paintToolParams.addChild(scaleMinAdjust);
        paintToolParams.addChild(scaleMaxAdjust);
        paintToolParams.addChild(rotationMinAdjust);
        paintToolParams.addChild(rotationMaxAdjust);
        paintToolParams.addChild(instancedTool);
        
        eraseTool = new EraseTool(Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_ERASE)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BRUSH_ERASE_TIP)
                , AssetConstants.INTERFACE_ICON_BESOM); 
        
        sizeTool.setMinValue(0).setValue(1);
        sizeTool.setStepAmount(0.1f);
        sizeTool.bindIncreaseEvent().bindKey(KeyInput.KEY_RBRACKET, true);
        sizeTool.bindDecreaseEvent().bindKey(KeyInput.KEY_LBRACKET, true);
        densityTool.setMinValue(1).setStepAmount(1).setValue(3);
        minHeight.setValue(0);
        useNormal.setValue(false);
        locationOffset.setValue(new Vector3f());
        scaleMinAdjust.setValue(new Vector3f(1,1,1));
        scaleMaxAdjust.setValue(new Vector3f(1,1,1));
        rotationMinAdjust.setValue(new Vector3f());
        rotationMaxAdjust.setValue(new Vector3f());
        
        paintTool.bindActionEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        eraseTool.bindActionEvent().bindButton(MouseInput.BUTTON_LEFT, true);
        
        Tool[] conflicts = new Tool[]{eraseTool, paintTool};
        addToggleMapping(new ToggleMappingEvent(-1, paintTool).setConflicts(conflicts).setRelations(paintToolParams));
        addToggleMapping(new ToggleMappingEvent(-1, eraseTool).setConflicts(conflicts));
        
        add(sizeTool);
        add(densityTool);
        add(sourceTool);
        
        add(paintTool);
        add(paintToolParams);
        
        add(eraseTool);
        
        setEnabled(sizeTool, true);
        setEnabled(sourceTool, true);
        setEnabled(densityTool, true);
    }

    public NumberValueTool getSizeTool() {
        return sizeTool;
    }

    public IntegerValueTool getDensityTool() {
        return densityTool;
    }

    public SourceTool getSourceTool() {
        return sourceTool;
    }

    public PaintTool getPaintTool() {
        return paintTool;
    }

    public ParamsTool getPaintToolParams() {
        return paintToolParams;
    }

    public NumberValueTool getMinHeight() {
        return minHeight;
    }

    public BooleanValueTool getUseNormal() {
        return useNormal;
    }

    public Vector3fValueTool getLocationOffset() {
        return locationOffset;
    }

    public Vector3fValueTool getScaleMinAdjust() {
        return scaleMinAdjust;
    }

    public Vector3fValueTool getScaleMaxAdjust() {
        return scaleMaxAdjust;
    }

    public Vector3fValueTool getRotationMinAdjust() {
        return rotationMinAdjust;
    }

    public Vector3fValueTool getRotationMaxAdjust() {
        return rotationMaxAdjust;
    }

    public InstancedTool getInstancedTool() {
        return instancedTool;
    }

    public EraseTool getEraseTool() {
        return eraseTool;
    }
    
    @Override
    public void cleanup() {
        removeAll();
        clearToggleMappings();
        super.cleanup(); 
    }
}
