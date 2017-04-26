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
import name.huliqing.editor.tools.IntegerValueTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.editor.tools.Vector3fValueTool;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.impl.BatchEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * 用于创建BatchEntity的工具, 这个工具会对场景进行区域划分，并为各个区域创建一个BatchEntity.
 * @author huliqing
 */
public class BatchEntityGenTool extends AbstractTool<SimpleJmeEdit, EntityBatchToolbar> 
        implements ValueChangedListener{
    private static final Logger LOG = Logger.getLogger(BatchEntityGenTool.class.getName());
    
    public final static String ATTR_BATCH_XEXTENT = "_batch_xextent";
    public final static String ATTR_BATCH_YEXTENT = "_batch_yextent";
    public final static String ATTR_BATCH_ZEXTENT = "_batch_zextent";
    public final static String ATTR_BATCH_CENTER = "_batch_center";
    
    private Spatial debugInfo;
    private IntegerValueTool rowTool;
    private IntegerValueTool columnTool;
    private Vector3fValueTool extentsTool;
    
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
        rowTool = this.toolbar.getBatchEntityGenRowTool();
        columnTool = this.toolbar.getBatchEntityGenColumnTool();
        extentsTool = this.toolbar.getBatchEntityGenExtentsTool();
        
        if (rowTool.getValue().intValue() <= 0) {
            rowTool.setValue(8);
        } 
        if (columnTool.getValue().intValue() <= 0) {
            columnTool.setValue(8);
        }
        Vector3f extentValue = extentsTool.getValue();
        if (extentValue == null || (extentValue.x <= 0 && extentValue.y <= 0 && extentValue.z <= 0)) {
            Scene scene = toolbar.getEdit().getScene();
            if (scene != null) {
                BoundingBox bv = (BoundingBox) scene.getRoot().getWorldBound();
                if (bv != null) {
                    Vector3f extents = new Vector3f();
                    extents.set(bv.getXExtent(), bv.getYExtent(), bv.getZExtent());
                    extentsTool.setValue(extents);
                }                
            }
        }
        rowTool.addValueChangeListener(this);
        columnTool.addValueChangeListener(this);
        extentsTool.addValueChangeListener(this);
    }
    
    @Override
    public void cleanup() {
        if (debugInfo != null) {
            debugInfo.removeFromParent();
        }
        super.cleanup();
    }
    
    /**
     * 根据设置生成BatchEntity, 并放到场景中。
     * @param baseName 指定一个基本名称
     */
    public void generateBatchEntities(String baseName) {
        int rows = rowTool.getValue().intValue();
        int columns = columnTool.getValue().intValue();
        Vector3f extents = extentsTool.getValue();
        if (rows < 1 || columns < 1) {
            LOG.log(Level.WARNING, "Rows and Columns could not less than 1. rows={0}, columns={1}", new Object[] {rows, columns});
            return;
        }
        if (extents.x <= 0 || extents.y <= 0 || extents.z <= 0) {
            LOG.log(Level.WARNING, "Extents could not less than 0, extents={0}", extents);
            return;
        }
        
        float xLen = extents.x * 2;
        float yLen = extents.y * 2;
        float zLen = extents.z * 2;
        float colXExtent =  extents.x / columns;
        float rowZExtent =  extents.z / rows;
        int batchNodes = rows * columns;
        
        // 根据划定的区域 ，为每个区域生成一个BatchEntity.
        List<BatchEntity> bes = new ArrayList<>(rows * columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                float xExtent = colXExtent;
                float yExtent =  extents.y;
                float zExtent = rowZExtent;
                Vector3f center = new Vector3f(-xExtent + colXExtent + colXExtent * 2 * j, 0, -zExtent + rowZExtent + rowZExtent * 2 * i);
                BatchEntity be = createBatchEntity(baseName + i + "_" + j, xExtent, yExtent, zExtent, center);
                bes.add(be);
            }
        }
        
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
        be.getData().setAttribute(ATTR_BATCH_XEXTENT, xExtent);
        be.getData().setAttribute(ATTR_BATCH_YEXTENT, yExtent);
        be.getData().setAttribute(ATTR_BATCH_ZEXTENT, zExtent);
        be.getData().setAttribute(ATTR_BATCH_CENTER, center);
        return be;
    }
    
    public void setGridVisible(boolean visible) {
        // hide
        if (!visible) {
            if (debugInfo != null) {
                debugInfo.setCullHint(Spatial.CullHint.Always);
            }
            return;
        }
        
        // show
        if (debugInfo == null) {
            recreateGrid();
        }
        debugInfo.setCullHint(Spatial.CullHint.Never);
    }
    
    private Spatial recreateGrid() {
        if (debugInfo != null) {
            debugInfo.removeFromParent();
        }
        Vector3f extents = extentsTool.getValue();
        debugInfo = createBatchGrid(rowTool.getValue().intValue(), columnTool.getValue().intValue()
                , extents.x, extents.y, extents.z);
        debugInfo.setCullHint(Spatial.CullHint.Never);
        toolbar.getEdit().getEditRoot().attachChild(debugInfo);
        return debugInfo;
    }
    
    private Spatial createBatchGrid(int rows, int columns, float xExtent, float yExtent, float zExtent) {
        float xLen = xExtent * 2;
        float yLen = yExtent * 2;
        float zLen = zExtent * 2;
        float colXExtent = xExtent / columns;
        float rowZExtent = zExtent / rows;
        int batchNodes = rows * columns;
        
        Node root = new Node("batchRootDisplay");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Spatial box = createBox(colXExtent, yExtent * 2, rowZExtent);
                box.setLocalTranslation(-xExtent + colXExtent + colXExtent * 2 * j, 0, -zExtent + rowZExtent + rowZExtent * 2 * i);
                root.attachChild(box);
            }
        }
        
        return root;
    }
    
    private Spatial createBox(float xExtent, float yExtent, float zExtent) {
        Box box = new Box(xExtent, yExtent, zExtent);
        Geometry geo = new Geometry("Box", box);
        Material mat = MaterialUtils.createUnshaded();
        mat.setColor("Color", new ColorRGBA(0f, 0f, 1.0f, 0.3f));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setDepthWrite(false);
        geo.setMaterial(mat);
        return geo;
    }

    @Override
    public void onValueChanged(ValueTool vt, Object oldValue, Object newValue) {
        if (debugInfo == null)
            return;
        if (debugInfo.getCullHint() == Spatial.CullHint.Always) {
            debugInfo.removeFromParent();
            debugInfo = null;
            return;
        }
        recreateGrid();
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
