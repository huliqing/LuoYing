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
package name.huliqing.editor.tools.batch;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.toolbar.EntityBatchToolbar;
import name.huliqing.editor.tools.AbstractTool;
import name.huliqing.luoying.object.entity.impl.BatchEntity;

/**
 * Batch的目标,,AutoBatchTargetTool会保存一个BatchEntity列表, 场景中的Entity将自动Batch到这个列表中的BatchEntity中。
 * @author huliqing
 */
public class BatchTargetTool extends AbstractTool<SimpleJmeEdit, EntityBatchToolbar>{
    
    private List<BatchEntity> batchEntities;

    public BatchTargetTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }
    
    public List<BatchEntity> getBatchEntities() {
        return batchEntities;
    }
    
    public void setBatchEntities(List<BatchEntity> batchEntities) {
        this.batchEntities = batchEntities;
    }
    
    public void addBatchEntity(BatchEntity entity) {
        if (batchEntities == null) {
            batchEntities = new ArrayList<>();
        }
        if (!batchEntities.contains(entity)) {
            batchEntities.add(entity);
        }
    }
    
    public boolean removeBatchEntity(BatchEntity batchEntity) {
        return batchEntities != null && batchEntities.remove(batchEntity);
    }
    
    public void clear() {
        if (batchEntities != null) {
            batchEntities.clear();
        }
    }
    
    /**
     * 生成BatchEntity
     */
    public void generateBatchEntity() {
        throw new UnsupportedOperationException();
    }
}
