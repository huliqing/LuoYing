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

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.manager.ControlTileManager;
import name.huliqing.editor.toolbar.EntityBatchToolbar;
import name.huliqing.editor.tools.AbstractTool;
import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.ButtonTool;
import name.huliqing.editor.tools.IntegerValueTool;
import name.huliqing.editor.tools.StringValueTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.editor.tools.Vector3fValueTool;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.impl.BatchEntity;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * 用于创建BatchEntity的工具, 这个工具会对场景进行区域划分，并为各个区域创建BatchEntity,然后把这些BatchEntity添加到
 * 场景中.
 * @author huliqing
 */
public class BatchEntityGenTool extends AbstractTool<SimpleJmeEdit, EntityBatchToolbar> 
        implements ButtonTool<SimpleJmeEdit, EntityBatchToolbar>, ValueChangedListener{
    private static final Logger LOG = Logger.getLogger(BatchEntityGenTool.class.getName());
    
    public final static String ATTR_BATCH_XEXTENT = "_batch_xextent";
    public final static String ATTR_BATCH_YEXTENT = "_batch_yextent";
    public final static String ATTR_BATCH_ZEXTENT = "_batch_zextent";
    public final static String ATTR_BATCH_CENTER = "_batch_center";
    
    private Node debugInfo;
    private StringValueTool genNameTool;
    private IntegerValueTool genRowTool;
    private IntegerValueTool genColumnTool;
    private Vector3fValueTool genExtentsTool;
    private BooleanValueTool genInfoTool;
    
    public BatchEntityGenTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }
    
    @Override
    public void initialize(SimpleJmeEdit edit, EntityBatchToolbar toolbar) {
        super.initialize(edit, toolbar);
        genNameTool = this.toolbar.getBatchEntityGenNameTool();
        genRowTool = this.toolbar.getBatchEntityGenRowTool();
        genColumnTool = this.toolbar.getBatchEntityGenColumnTool();
        genExtentsTool = this.toolbar.getBatchEntityGenExtentsTool();
        genInfoTool = this.toolbar.getBatchEntityGenInfoTool();
        
        genRowTool.addValueChangeListener(this);
        genColumnTool.addValueChangeListener(this);
        genExtentsTool.addValueChangeListener(this);
        genInfoTool.addValueChangeListener(this);
        if (genInfoTool.getValue() != null && genInfoTool.getValue()) {
            recreateGrid();
        }
    }
    
    @Override
    public void cleanup() {
        if (debugInfo != null) {
            debugInfo.removeFromParent();
        }
        if (genRowTool != null) {
            genRowTool.removeValueChangeListener(this);
        }
        if (genColumnTool != null) {
            genColumnTool.removeValueChangeListener(this);
        }
        if (genExtentsTool != null) {
            genExtentsTool.removeValueChangeListener(this);
        }
        if (genInfoTool != null) {
            genInfoTool.removeValueChangeListener(this);
        }
        super.cleanup();
    }

    /**
     * 根据设置生成BatchEntity, 并放到场景中。
     */
    @Override
    public void doAction() {
        String baseName = genNameTool.getValue();
        int rows = genRowTool.getValue().intValue();
        int columns = genColumnTool.getValue().intValue();
        Vector3f extents = genExtentsTool.getValue();
        
        // 根据划定的区域 ，为每个区域生成一个BatchEntity.
        List<BatchZone> bzs = createBatchZone(rows, columns, extents.x, extents.y, extents.z);
        if (bzs == null || bzs.isEmpty()) {
            return;
        }
        List<BatchEntity> bes = new ArrayList<>(bzs.size());
        bzs.forEach(bz -> {
            BatchEntity be = createBatchEntity(baseName + bz.name, bz.xExtent, bz.yExtent, bz.zExtent, bz.center);
            bes.add(be);
        });
        
        // 创建ControlTile(在放到场景中时必须使用ControlTile)
        List<EntityControlTile> ects = new ArrayList<>(bes.size());
        bes.forEach((be) -> {
            EntityControlTile ect = ControlTileManager.createEntityControlTile(be.getData());
            ect.setTarget(be);
            ects.add(ect);
        });
        
        //  放到场景中并创建历史记录
        BatchEntityGenUndoRedo ur = new BatchEntityGenUndoRedo(ects);
        ur.redo();
        edit.addUndoRedo(ur);
    }
    
    private BatchEntity createBatchEntity(String name, float xExtent, float yExtent, float zExtent, Vector3f center) {
        BatchEntity be = Loader.load(IdConstants.SYS_ENTITY_BATCH);
        be.getData().setName(name);
        be.getData().setAttribute(ATTR_BATCH_CENTER, center);
        be.getData().setAttribute(ATTR_BATCH_XEXTENT, xExtent);
        be.getData().setAttribute(ATTR_BATCH_YEXTENT, yExtent);
        be.getData().setAttribute(ATTR_BATCH_ZEXTENT, zExtent);
        LOG.log(Level.INFO, "CreateBatchEntity, name={0}, xExtent={1}, yExtent={2}, zExtent={3}, center={4}"
                , new Object[]{name, xExtent, yExtent, zExtent, center});
        return be;
    }
    
    private Spatial recreateGrid() {
        if (debugInfo != null) {
            debugInfo.removeFromParent();
        }
        boolean display = genInfoTool.getValue();
        int rows = genRowTool.getValue().intValue();
        int columns = genColumnTool.getValue().intValue();
        Vector3f extents = genExtentsTool.getValue();
        if (!display) {
            return null;
        }
        
        debugInfo = new Node("batchRootDisplay");
        toolbar.getEdit().getEditRoot().attachChild(debugInfo);
        
        List<BatchZone> zones = createBatchZone(rows, columns, extents.x, extents.y, extents.z);
        if (zones != null && !zones.isEmpty()) {
            zones.forEach(e -> {
                Spatial zoneBox = createBox(e.xExtent, e.yExtent, e.zExtent, e.center);
                debugInfo.attachChild(zoneBox);
            });
        }
        return debugInfo;
    }
    
    /**
     * 划分Batch区域
     * @param rows
     * @param columns
     * @param fullZoneXExtent
     * @param fullZoneYExtent
     * @param fullZoneZExtent
     * @return 
     */
    private List<BatchZone> createBatchZone(int rows, int columns, float fullZoneXExtent, float fullZoneYExtent, float fullZoneZExtent) {
        if (rows < 1 || columns < 1) {
            LOG.log(Level.WARNING, "Rows and Columns could not less than 1. rows={0}, columns={1}", new Object[] {rows, columns});
            return null;
        }
        if (fullZoneXExtent <= 0 || fullZoneYExtent <= 0 || fullZoneZExtent <= 0) {
            LOG.log(Level.WARNING, "Extents could not less than 0, fullZoneXExtent={0}, fullZoneYExtent={1}, fullZoneZExtent={2}"
                    , new Object[]{fullZoneXExtent, fullZoneYExtent, fullZoneZExtent});
            return null;
        }
        
        float batchZoneXExtent = fullZoneXExtent / columns;
        float batchZoneZExtent = fullZoneZExtent / rows;
        float batchZoneYExtent = fullZoneYExtent;
        int batchZones = rows * columns;
        List<BatchZone> zones = new ArrayList<>(batchZones);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                BatchZone bz = new BatchZone();
                bz.name = i + "_" + j;
                bz.xExtent = batchZoneXExtent;
                bz.yExtent = batchZoneYExtent;
                bz.zExtent = batchZoneZExtent;
                bz.center = new Vector3f(
                         -fullZoneXExtent + batchZoneXExtent + batchZoneXExtent * 2 * j
                        , 0
                        , -fullZoneZExtent + batchZoneZExtent + batchZoneZExtent * 2 * i
                );
                zones.add(bz);
            }
        }
        return zones;
    }
    
    private Spatial createBox(float xExtent, float yExtent, float zExtent, Vector3f center) {
        Box box = new Box(xExtent, yExtent, zExtent);
        Geometry geo = new Geometry("Box", box);
        Material mat = MaterialUtils.createUnshaded();
        mat.setColor("Color", new ColorRGBA(0f, 0f, 1.0f, 0.3f));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
        geo.setMaterial(mat);
        geo.setLocalTranslation(center);
        return geo;
    }

    @Override
    public void onValueChanged(ValueTool vt, Object oldValue, Object newValue) {
        recreateGrid();
    }
    
    private class BatchZone {
        String name;
        Vector3f center;
        float xExtent;
        float yExtent;
        float zExtent;
    }
    
    private class BatchEntityGenUndoRedo implements UndoRedo {
        
        private final List<EntityControlTile> ectAddeds = new ArrayList();

        public BatchEntityGenUndoRedo(List<EntityControlTile> ectAddeds) {
            this.ectAddeds.addAll(ectAddeds);
        }
        
        @Override
        public void undo() {
            SceneEdit sEdit = (SceneEdit) edit;
            for (int i = ectAddeds.size() - 1; i >= 0; i--) {
                sEdit.removeControlTile(ectAddeds.get(i));
            }
        }

        @Override
        public void redo() {
            SceneEdit sEdit = (SceneEdit) edit;
            for (int i = 0; i < ectAddeds.size(); i++) {
                sEdit.addControlTile(ectAddeds.get(i));
            }
        }
    }
}
