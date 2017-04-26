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

import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.IntegerValueTool;
import name.huliqing.editor.tools.ParamsTool;
import name.huliqing.editor.tools.SimpleToggleTool;
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
    private IntegerValueTool batchEntityGenRowTool;
    private IntegerValueTool batchEntityGenColumnTool;
    private Vector3fValueTool batchEntityGenExtentsTool; // Batch范围大小
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
        batchToggleTool = new SimpleToggleTool("AutoBatchTool", "xxx", null);
        batchGroupTool = new ParamsTool("", "", null);
        batchSourceTool = new BatchSourceTool("AutoBatchSourceTool", "xxx", null);
        batchTargetTool = new BatchTargetTool("AutoBatchTargetTool", "xxx", null);
        batchTool = new BatchTool("BatchTool", "xxx", null);
        batchGroupTool.addChild(batchSourceTool);
        batchGroupTool.addChild(batchTargetTool);
        batchGroupTool.addChild(batchTool);
        
        batchEntityGenToggleTool = new SimpleToggleTool("BatchEntityGenTool", "xxx", null);
        batchEntityGenGroupTool = new ParamsTool("", "", null);
        batchEntityGenRowTool = new IntegerValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_ROW), Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_ROW_TIP), null);
        batchEntityGenColumnTool = new IntegerValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_COLUMN), Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_COLUMN_TIP), null);
        batchEntityGenExtentsTool = new Vector3fValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_EXTENTS), Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_EXTENTS_TIP), null);
        batchEntityGenTool = new BatchEntityGenTool("BatchEntityGenTool", "", null);
        batchEntityGenGroupTool.addChild(batchEntityGenRowTool);
        batchEntityGenGroupTool.addChild(batchEntityGenColumnTool);
        batchEntityGenGroupTool.addChild(batchEntityGenExtentsTool);
        batchEntityGenGroupTool.addChild(batchEntityGenTool);
        
        
        add(batchToggleTool);
        add(batchGroupTool);
        
        add(batchEntityGenToggleTool);
        add(batchEntityGenGroupTool);
        
        addToggleMapping(new ToggleMappingEvent(-1, batchToggleTool).setConflicts(batchEntityGenToggleTool, batchEntityGenGroupTool).setRelations(batchGroupTool));
        addToggleMapping(new ToggleMappingEvent(-1, batchEntityGenToggleTool).setConflicts(batchToggleTool, batchGroupTool).setRelations(batchEntityGenGroupTool));

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

    public IntegerValueTool getBatchEntityGenRowTool() {
        return batchEntityGenRowTool;
    }

    public IntegerValueTool getBatchEntityGenColumnTool() {
        return batchEntityGenColumnTool;
    }

    public Vector3fValueTool getBatchEntityGenExtentsTool() {
        return batchEntityGenExtentsTool;
    }

    public BatchEntityGenTool getBatchEntityGenTool() {
        return batchEntityGenTool;
    }

    
}
