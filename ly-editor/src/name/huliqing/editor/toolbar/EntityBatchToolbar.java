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
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.IntegerValueTool;
import name.huliqing.editor.tools.ParamsTool;
import name.huliqing.editor.tools.SimpleToggleTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.tools.Vector3fValueTool;
import name.huliqing.editor.tools.batch.AutoBatchTool;
import name.huliqing.editor.tools.batch.BatchTool;

/**
 * 场景实体Batch工具栏
 * @author huliqing
 */
public class EntityBatchToolbar extends EditToolbar<SceneEdit> {

    // 自动Batch工具栏
    private SimpleToggleTool autoBatchToggleTool;
    private ParamsTool autoBatchGroupTool;
    private IntegerValueTool autoBatchRowTool;
    private IntegerValueTool autoBatchColumnTool;
    // Batch范围大小
    private Vector3fValueTool autoBatchExtentsTool;
    private AutoBatchTool autoBatchTool;
    
    // 单独Batch工具栏
    private SimpleToggleTool batchToggleTool;
    private ParamsTool batchGroupTool;
    private BatchTool batchTool;
    
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
        autoBatchToggleTool = new SimpleToggleTool("AutoBatchTool", "xxx", null);
        autoBatchGroupTool = new ParamsTool("", "", null);
        autoBatchRowTool = new IntegerValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_ROW), Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_ROW_TIP), null);
        autoBatchColumnTool = new IntegerValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_COLUMN), Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_COLUMN_TIP), null);
        autoBatchExtentsTool = new Vector3fValueTool(Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_EXTENTS), Manager.getRes(ResConstants.TOOL_ENTITY_BATCH_EXTENTS_TIP), null);
        autoBatchTool = new AutoBatchTool("BatchTool", "xxx", null);
        autoBatchGroupTool.addChild(autoBatchRowTool);
        autoBatchGroupTool.addChild(autoBatchColumnTool);
        autoBatchGroupTool.addChild(autoBatchExtentsTool);
        autoBatchGroupTool.addChild(autoBatchTool);
        
        batchToggleTool = new SimpleToggleTool("BatchTool", "xxx", null);
        batchGroupTool = new ParamsTool("BatchGroupTool", "xxx", null);
        batchTool = new BatchTool("BatchTool", "xxx", null);
        batchGroupTool.addChild(batchTool);
        
        add(autoBatchToggleTool);
        add(autoBatchGroupTool);
        add(batchToggleTool);
        add(batchGroupTool);
        
        Tool[] autoBatchTools = new Tool[]{autoBatchToggleTool, autoBatchGroupTool};
        Tool[] batchTools = new Tool[]{batchToggleTool, batchGroupTool};
  
        addToggleMapping(new ToggleMappingEvent(-1, autoBatchToggleTool).setConflicts(batchTools).setRelations(autoBatchGroupTool));
        addToggleMapping(new ToggleMappingEvent(-1, batchToggleTool).setConflicts(autoBatchTools).setRelations(batchGroupTool));
        
        setEnabled(autoBatchToggleTool, true);
        setEnabled(batchToggleTool, false);
    }
    
    @Override
    public void cleanup() {
        removeAll();
        clearToggleMappings();
        super.cleanup(); 
    }

    public IntegerValueTool getAutoBatchRowTool() {
        return autoBatchRowTool;
    }

    public IntegerValueTool getAutoBatchColumnTool() {
        return autoBatchColumnTool;
    }

    public Vector3fValueTool getAutoBatchExtentsTool() {
        return autoBatchExtentsTool;
    }
    
    
}
