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
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.toolbar.EntityBatchToolbar;
import name.huliqing.editor.tools.AbstractTool;
import name.huliqing.editor.tools.IntegerValueTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.editor.tools.Vector3fValueTool;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * BatchTool,用于将场景中的一些相同类型的实体进行批量Batch到BatchEntity节点中。
 * 该工具会动态的为场景创建BatchEntity.将场景中的指定的实体自动分配到不同的BatchEntity中。
 * 可指定要Batch的区域的大小, 指定要自动创建的BatchEntity的数量
 * @author huliqing
 */
public class AutoBatchTool extends AbstractTool<SimpleJmeEdit, EntityBatchToolbar> 
        implements ValueChangedListener{
    private Spatial debugInfo;
    private IntegerValueTool rowTool;
    private IntegerValueTool columnTool;
    private Vector3fValueTool extentsTool;

    public AutoBatchTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }

    @Override
    public void initialize(SimpleJmeEdit edit, EntityBatchToolbar toolbar) {
        super.initialize(edit, toolbar);
        
        rowTool = this.toolbar.getAutoBatchRowTool();
        columnTool = this.toolbar.getAutoBatchColumnTool();
        extentsTool = this.toolbar.getAutoBatchExtentsTool();
        
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
    
    public void generateBatch() {
        int rows = this.toolbar.getAutoBatchColumnTool().getValue().intValue();
        int columns = this.toolbar.getAutoBatchColumnTool().getValue().intValue();
        // TODO:XXX
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
        debugInfo.setCullHint(CullHint.Never);
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
        if (debugInfo.getCullHint() == CullHint.Always) {
            debugInfo.removeFromParent();
            debugInfo = null;
            return;
        }
        recreateGrid();
    }
}
