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

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.toolbar.EntityBatchToolbar;
import name.huliqing.editor.tools.AbstractTool;
import name.huliqing.editor.tools.ButtonTool;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.impl.BatchEntity;

/**
 * BatchTool,用于将Entity批量Batch到各自的BatchEntity节点中.
 * @author huliqing
 */
public class BatchTool extends AbstractTool<SimpleJmeEdit, EntityBatchToolbar> implements ButtonTool<SimpleJmeEdit, EntityBatchToolbar>{

    private static final Logger LOG = Logger.getLogger(BatchTool.class.getName());

    public BatchTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }

    /**
     *  将所选择的Entity列表，根据这些Entity各自所在的区域，分别batch各自的BatchEntity中去。
     */
    @Override
    public void doAction() {
        List<Entity> es = toolbar.getBatchSourceTool().getEntities();
        List<BatchEntity> bes = toolbar.getBatchTargetTool().getBatchEntities();
        if (es == null || es.isEmpty())
            return;
        if (bes == null || bes.isEmpty())
            return;
        
        List<BatchZoneWrap> bws = new ArrayList<>(bes.size());
        bes.forEach(be -> {
            bws.add(new BatchZoneWrap(be));
        });
        
        // 把Entity分配到各自的Batch区中
        for (Entity e : es) {
            for (BatchZoneWrap bw : bws) {
                if (bw.contain(e)) {
                    bw.entities.add(e);
                    break;
                } else if (bw.batchEntity.isBatch(e)) {
                    // 如果一个Entity已经被Batch到当前BatchEntity，但是却不在当前BatchEntity中，则要从当前的BatchEntity中
                    // 移除，避免Entity被同时Batch到多个BatchEntity(这种情况会发生在当Batch处理后，Entity进行了移动
                    //，之后又进行了多次Batch的情况。)
                    bw.batchEntity.removeBatchEntity(e);
                }
            }
        }
        
        // 将Entity添加到BatchEntity并进行Batch
        BatchUndoRedo bur = new BatchUndoRedo(bws);
        bur.redo();
        
        // 添加到历史记录
        edit.addUndoRedo(bur);
    }
    
    private final class BatchZoneWrap {
        private final BatchEntity batchEntity;
        private final BoundingBox bb;
        private final List<Entity> entities = new ArrayList<>();
        
        private BatchZoneWrap(BatchEntity be) {
            this.batchEntity = be;
            Vector3f center = be.getData().getAsVector3f(BatchEntityGenTool.ATTR_BATCH_CENTER);
            float xExtent = be.getData().getAsFloat(BatchEntityGenTool.ATTR_BATCH_XEXTENT, 0);
            float yExtent = be.getData().getAsFloat(BatchEntityGenTool.ATTR_BATCH_YEXTENT, 0);
            float zExtent = be.getData().getAsFloat(BatchEntityGenTool.ATTR_BATCH_ZEXTENT, 0);
            bb = new BoundingBox(center, xExtent, yExtent, zExtent);
            LOG.log(Level.INFO, "Create BatchZoneWrap,  BatchEntityName={0}, BoundingBox={1}"
                    , new Object[] {be.getData().getName(), bb});
        }
        
        /**
         * 判断一个Entity是否是在Batch范围内.
         * @param entity
         * @return 
         */
        public boolean contain(Entity entity) {
            if (entity.getSpatial() == null)
                return false;
            return bb.contains(entity.getSpatial().getWorldTranslation());
        }
    }
    
    private class BatchUndoRedo implements UndoRedo {
        private final List<BatchZoneWrap> bwsAdded = new ArrayList();

        public BatchUndoRedo(List<BatchZoneWrap> bw) {
            this.bwsAdded.addAll(bw);
        }
        
        @Override
        public void undo() {
            for (int i = bwsAdded.size() - 1; i >=0; i--) {
                BatchZoneWrap bw = bwsAdded.get(i);
                if (bw.entities != null && !bw.entities.isEmpty()) {
                    for (int j = bw.entities.size() - 1; j >= 0; j--) {
                        bw.batchEntity.removeBatchEntity(bw.entities.get(j));
                    }
                    bw.batchEntity.batch();
                }
            }
        }

        @Override
        public void redo() {
            for (int i = 0; i < bwsAdded.size(); i++) {
                BatchZoneWrap bw = bwsAdded.get(i);
                if (bw.entities != null && !bw.entities.isEmpty()) {
                    for (Entity e : bw.entities) {
                        bw.batchEntity.addBatchEntity(e);
                    }
                    bw.batchEntity.batch();
                }
            }
        }
        
    }
}
