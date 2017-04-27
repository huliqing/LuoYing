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

import com.jme3.math.Vector3f;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.IntegerValueTool;
import name.huliqing.editor.tools.ParamsTool;
import name.huliqing.editor.tools.SimpleToggleTool;
import name.huliqing.editor.tools.StringValueTool;
import name.huliqing.editor.tools.Vector3fValueTool;
import name.huliqing.editor.tools.batch.BatchEntityGenTool;
import name.huliqing.editor.tools.batch.BatchSourceTool;
import name.huliqing.editor.tools.batch.BatchTargetTool;
import name.huliqing.editor.tools.batch.BatchTool;

/**
 * 场景实体Batch工具栏
 * @author huliqing
 */
public class EntityBatchToolbar extends EditToolbar<SceneEdit> {

    // ----将实体Batch到指定BatchEntity的工具
    private SimpleToggleTool batchToggleTool;
    private ParamsTool batchGroupTool;
    private BatchSourceTool batchSourceTool;
    private BatchTargetTool batchTargetTool;
    private BatchTool batchTool;
    
    // ---- 创建BatchEntity的工具
    private SimpleToggleTool batchEntityGenToggleTool;
    private ParamsTool batchEntityGenGroupTool;
    private StringValueTool batchEntityGenNameTool;
    private IntegerValueTool batchEntityGenRowTool;
    private IntegerValueTool batchEntityGenColumnTool;
    private Vector3fValueTool batchEntityGenExtentsTool; // Batch范围大小
    private BooleanValueTool batchEntityGenInfoTool;
    private BatchEntityGenTool batchEntityGenTool;
    
    public EntityBatchToolbar(SceneEdit edit) {
        super(edit);
    }
    
    @Override
    public String getName() {
        return Manager.getRes(ResConstants.EDIT_TOOLBAR_ENTITY_BATCH);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        batchToggleTool = new SimpleToggleTool(Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_TOGGLE)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_TOGGLE_TIP), null);
        batchGroupTool = new ParamsTool("", "", null);
        batchSourceTool = new BatchSourceTool(Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_SOURCE)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_SOURCE_TIP), null);
        batchTargetTool = new BatchTargetTool(Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_TARGET)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_TARGET_TIP), null);
        batchTool = new BatchTool(Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_SUBMIT)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_SUBMIT_TIP), null);
        batchGroupTool.addChild(batchSourceTool);
        batchGroupTool.addChild(batchTargetTool);
        batchGroupTool.addChild(batchTool);
        
        batchEntityGenToggleTool = new SimpleToggleTool(
                Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_TOGGLE)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_TOGGLE_TIP), null);
        batchEntityGenGroupTool = new ParamsTool("", "", null);
        batchEntityGenNameTool = new StringValueTool(
                Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_NAME)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_NAME_TIP), null);
        batchEntityGenRowTool = new IntegerValueTool(
                Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_ROW)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_ROW_TIP), null);
        batchEntityGenColumnTool = new IntegerValueTool(
                Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_COLUMN)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_COLUMN_TIP), null);
        batchEntityGenExtentsTool = new Vector3fValueTool(
                Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_EXTENTS)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_EXTENTS_TIP), null);
        batchEntityGenInfoTool = new BooleanValueTool(
                Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_INFO)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_INFO_TIP), null);
        batchEntityGenTool = new BatchEntityGenTool(
                Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_SUBMIT)
                , Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_GEN_SUBMIT_TIP), null);
        
        batchEntityGenGroupTool.addChild(batchEntityGenNameTool);
        batchEntityGenGroupTool.addChild(batchEntityGenRowTool);
        batchEntityGenGroupTool.addChild(batchEntityGenColumnTool);
        batchEntityGenGroupTool.addChild(batchEntityGenExtentsTool);
        batchEntityGenGroupTool.addChild(batchEntityGenInfoTool);
        batchEntityGenGroupTool.addChild(batchEntityGenTool);
        batchEntityGenNameTool.setValue("batch");
        batchEntityGenRowTool.setMinValue(1).setValue(8);
        batchEntityGenColumnTool.setMinValue(1).setValue(8);
        batchEntityGenExtentsTool.setValue(new Vector3f(256, 128, 256));
        batchEntityGenInfoTool.setValue(true);
        add(batchToggleTool);
        add(batchGroupTool);
        
        add(batchEntityGenToggleTool);
        add(batchEntityGenGroupTool);
        
        addToggleMapping(new ToggleMappingEvent(-1, batchToggleTool)
                .setConflicts(batchEntityGenToggleTool, batchEntityGenGroupTool).setRelations(batchGroupTool));
        addToggleMapping(new ToggleMappingEvent(-1, batchEntityGenToggleTool)
                .setConflicts(batchToggleTool, batchGroupTool).setRelations(batchEntityGenGroupTool));
    }
    
    @Override
    public void cleanup() {
        removeAll();
        clearToggleMappings();
        super.cleanup(); 
    }
    
    public SimpleToggleTool getBatchToggleTool() {
        return batchToggleTool;
    }

    public ParamsTool getBatchGroupTool() {
        return batchGroupTool;
    }

    public BatchSourceTool getBatchSourceTool() {
        return batchSourceTool;
    }

    public BatchTargetTool getBatchTargetTool() {
        return batchTargetTool;
    }

    public BatchTool getBatchTool() {
        return batchTool;
    }

    public SimpleToggleTool getBatchEntityGenToggleTool() {
        return batchEntityGenToggleTool;
    }

    public ParamsTool getBatchEntityGenGroupTool() {
        return batchEntityGenGroupTool;
    }

    public StringValueTool getBatchEntityGenNameTool() {
        return batchEntityGenNameTool;
    }
    
    public IntegerValueTool getBatchEntityGenRowTool() {
        return batchEntityGenRowTool;
    }

    public IntegerValueTool getBatchEntityGenColumnTool() {
        return batchEntityGenColumnTool;
    }

    public Vector3fValueTool getBatchEntityGenExtentsTool() {
        return batchEntityGenExtentsTool;
    }
    
    public BooleanValueTool getBatchEntityGenInfoTool() {
        return batchEntityGenInfoTool;
    }
    
    public BatchEntityGenTool getBatchEntityGenTool() {
        return batchEntityGenTool;
    }

    
}
