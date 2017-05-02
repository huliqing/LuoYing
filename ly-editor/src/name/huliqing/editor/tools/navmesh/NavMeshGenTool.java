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
package name.huliqing.editor.tools.navmesh;

import com.jme3.export.binary.BinaryExporter;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.UncacheAssetEventListener;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.toolbar.NavMeshToolbar;
import name.huliqing.editor.tools.AbstractTool;
import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.ButtonTool;
import name.huliqing.editor.tools.EntityValueTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.impl.NavMeshEntity;
import name.huliqing.luoying.utils.MaterialUtils;
import name.huliqing.luoying.utils.modifier.NavMeshGenerator;
import name.huliqing.luoying.utils.modifier.NavMeshUtils;

/**
 * NavMesh创建工具
 * @author huliqing
 */
public class NavMeshGenTool extends AbstractTool<SimpleJmeEdit, NavMeshToolbar> 
        implements ButtonTool<SimpleJmeEdit, NavMeshToolbar> {
    
    // 寻路网格生成后的保存位置
    private final String modelDir = "/Models/navmesh";
    
    private EntityValueTool navMeshSourceTool;
    private final NavMeshSourceChangedListener navMeshSourceChangedListener = new NavMeshSourceChangedListener();
    
    private BooleanValueTool displayNavMeshTool;
    private final NavMeshDisplayChangedListener navMeshDisplayChangedListener = new NavMeshDisplayChangedListener();
    
    // 最近一次创建的NavMesh,用于在编辑器中debug显示
    private Spatial lastNavMesh;

    public NavMeshGenTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }

    @Override
    public void initialize(SimpleJmeEdit edit, NavMeshToolbar toolbar) {
        super.initialize(edit, toolbar);
        navMeshSourceTool = toolbar.getNavMeshSourceTool();
        navMeshSourceTool.addValueChangeListener(navMeshSourceChangedListener);
        
        displayNavMeshTool = toolbar.getDisplayNavMeshTool();
        displayNavMeshTool.addValueChangeListener(navMeshDisplayChangedListener);
    }
    
    @Override
    public void cleanup() {
        navMeshSourceTool.removeValueChangeListener(navMeshSourceChangedListener);
        displayNavMeshTool.removeValueChangeListener(navMeshDisplayChangedListener);
        if (lastNavMesh != null) {
            lastNavMesh.removeFromParent();
            lastNavMesh = null;
        }
        super.cleanup();
    }
    
    @Override
    public void doAction() {
        // 只能支持SceneEdit
        if (!(edit instanceof SceneEdit)) {
            Logger.getLogger(NavMeshGenTool.class.getName()).log(Level.WARNING
                    , "NavMeshGenTool only support SceneEdit, edit={0}", edit.getClass());
            return;
        }
        
        // 先清除上一次创建的NavMesh
        if (lastNavMesh != null) {
            lastNavMesh.removeFromParent();
            lastNavMesh = null;
        }
        
        //  确定一个用于存储NavMesh的 NavMeshEntity
        // 如果没有指定则重新创建一个
        Entity navMeshEntity = toolbar.getNavMeshSourceTool().getValue();
        if (!(navMeshEntity instanceof NavMeshEntity)) {
            Logger.getLogger(NavMeshGenTool.class.getName()).log(Level.SEVERE
                    , "NavMeshSource need a entity of type of NavMeshEntity, but found={0}", navMeshEntity);
            return;
        }

        // 生成NavMesh
        SceneEdit se = (SceneEdit) edit;
        Spatial tempNavMesh;
        try {
            tempNavMesh = NavMeshUtils.doCreateSpatial(se.getScene().getRoot(), createNavMeshGenerator(), LuoYing.getAssetManager());
            if (tempNavMesh == null) {
                Logger.getLogger(NavMeshGenTool.class.getName()).log(Level.SEVERE, "Could not create NavMesh!");
                return;
            } 
        } catch (Exception e) {
            Logger.getLogger(NavMeshGenTool.class.getName()).log(Level.SEVERE, "failure!", e);
            return;
        }
        
        // 保存NavMesh
        try {
            // 确定保存NavMesh的资源路径名称
            String filenameInAssets = navMeshEntity.getData().getAsString("file");
            if (filenameInAssets == null) {
                filenameInAssets = modelDir.substring(1) + "/navmesh-" 
                        + new SimpleDateFormat("yyyy-MM-dd-hhmmss").format(new Date()) + ".j3o";
                navMeshEntity.getData().setAttribute("file", filenameInAssets);
            }
            
            // 保存NavMesh到指定的路径
            File navmeshFile = new File(Manager.getConfigManager().getMainAssetDir(), filenameInAssets);
            BinaryExporter.getInstance().save(tempNavMesh, navmeshFile);
            UncacheAssetEventListener.getInstance().addUncache(filenameInAssets); // 不缓存NavMesh
            se.reloadEntity(navMeshEntity.getData());
            
            // 注：这个lastNavMesh只能放在EditRoot下，不能放在Scene.getRoot()下面, 避免lastNavMesh被用于计算寻路网格
            lastNavMesh = tempNavMesh;
            lastNavMesh.setMaterial(MaterialUtils.createWireFrame(ColorRGBA.Green));
            lastNavMesh.setCullHint(Spatial.CullHint.Never);
            if (toolbar.getDisplayNavMeshTool().getValue()) {
                edit.getEditRoot().attachChild(lastNavMesh);
            }
            
            //  储存参数到实体，下次载入时可以直接读取这个数据
            storePropertiesToEntity(navMeshEntity);
            
        } catch (IOException e) {
            Logger.getLogger(NavMeshGenTool.class.getName()).log(Level.SEVERE, "failure!", e);
        }
    }
    
    // 把参考储存到指定的实体中
    private void storePropertiesToEntity(Entity nmeStore) {
            nmeStore.getData().setAttribute("_navMesh_cellHeight", toolbar.getCellHeight().getValue().floatValue());
            nmeStore.getData().setAttribute("_navMesh_cellSize", toolbar.getCellSize().getValue().floatValue());
            nmeStore.getData().setAttribute("_navMesh_clipLedges", toolbar.getClipLedges().getValue());
            nmeStore.getData().setAttribute("_navMesh_contourMaxDeviation", toolbar.getContourMaxDeviation().getValue().floatValue());
            nmeStore.getData().setAttribute("_navMesh_contourSampleDistance", toolbar.getContourSampleDistance().getValue().floatValue());
            nmeStore.getData().setAttribute("_navMesh_edgeMaxDeviation", toolbar.getEdgeMaxDeviation().getValue().floatValue());
            nmeStore.getData().setAttribute("_navMesh_maxEdgeLength", toolbar.getMaxEdgeLength().getValue().floatValue());
            nmeStore.getData().setAttribute("_navMesh_maxTraversableSlope", toolbar.getMaxTraversableSlope().getValue().floatValue());
            nmeStore.getData().setAttribute("_navMesh_maxTraversableStep", toolbar.getMaxTraversableStep().getValue().floatValue());
            nmeStore.getData().setAttribute("_navMesh_maxVertsPerPoly", toolbar.getMaxVertsPerPoly().getValue().intValue());
            nmeStore.getData().setAttribute("_navMesh_mergeRegionSize", toolbar.getMergeRegionSize().getValue().intValue());
            nmeStore.getData().setAttribute("_navMesh_minTraversableHeight", toolbar.getMinTraversableHeight().getValue().floatValue());
            nmeStore.getData().setAttribute("_navMesh_minUnconnectedRegionSize", toolbar.getMinUnconnectedRegionSize().getValue().intValue());
            nmeStore.getData().setAttribute("_navMesh_smoothingThreshold", toolbar.getSmoothingThreshold().getValue().intValue());
            nmeStore.getData().setAttribute("_navMesh_timeout", toolbar.getTimeout().getValue().intValue());
            nmeStore.getData().setAttribute("_navMesh_traversableAreaBorderSize", toolbar.getTraversableAreaBorderSize().getValue().floatValue());
            nmeStore.getData().setAttribute("_navMesh_useConservativeExpansion", toolbar.getUseConservativeExpansion().getValue());
    }
    
    private NavMeshGenerator createNavMeshGenerator() {
        NavMeshGenerator generator = new NavMeshGenerator();
        generator.setCellHeight(toolbar.getCellHeight().getValue().floatValue());
        generator.setCellSize(toolbar.getCellSize().getValue().floatValue());
        generator.setClipLedges(toolbar.getClipLedges().getValue());
        generator.setContourMaxDeviation(toolbar.getContourMaxDeviation().getValue().floatValue());
        generator.setContourSampleDistance(toolbar.getContourSampleDistance().getValue().floatValue());
        generator.setEdgeMaxDeviation(toolbar.getEdgeMaxDeviation().getValue().floatValue());
//        generator.setIntermediateData(11111111);
        generator.setMaxEdgeLength(toolbar.getMaxEdgeLength().getValue().floatValue());
        generator.setMaxTraversableSlope(toolbar.getMaxTraversableSlope().getValue().floatValue());
        generator.setMaxTraversableStep(toolbar.getMaxTraversableStep().getValue().floatValue());
        generator.setMaxVertsPerPoly(toolbar.getMaxVertsPerPoly().getValue().intValue());
        generator.setMergeRegionSize(toolbar.getMergeRegionSize().getValue().intValue());
        generator.setMinTraversableHeight(toolbar.getMinTraversableHeight().getValue().floatValue());
        generator.setMinUnconnectedRegionSize(toolbar.getMinUnconnectedRegionSize().getValue().intValue());
//        generator.setNmgen(11111111);
        generator.setSmoothingThreshold(toolbar.getSmoothingThreshold().getValue().intValue());
        generator.setTimeout(toolbar.getTimeout().getValue().intValue());
        generator.setTraversableAreaBorderSize(toolbar.getTraversableAreaBorderSize().getValue().floatValue());
        generator.setUseConservativeExpansion(toolbar.getUseConservativeExpansion().getValue());
        generator.printParams();
        return generator;
    }

    private class NavMeshDisplayChangedListener implements ValueChangedListener<Boolean> {

        @Override
        public void onValueChanged(ValueTool<Boolean> vt, Boolean oldValue, Boolean newValue) {
            if (lastNavMesh == null) return;
            if (newValue) {
                edit.getEditRoot().attachChild(lastNavMesh);
            } else {
                lastNavMesh.removeFromParent();
            }
        }
    }
    
    private class NavMeshSourceChangedListener implements ValueChangedListener<Entity> {

        @Override
        public void onValueChanged(ValueTool<Entity> vt, Entity oldValue, Entity newValue) {
            // 当切换实体的时候把实体中储存NavMesh设置参数读取到toolbar中的参数工具中
            if (newValue instanceof NavMeshEntity) {
                NavMeshEntity nme = (NavMeshEntity) newValue;
                toolbar.getCellHeight().setValue(nme.getData().getAsFloat("_navMesh_cellHeight", toolbar.getCellHeight().getValue().floatValue()));
                toolbar.getCellSize().setValue(nme.getData().getAsFloat("_navMesh_cellSize", toolbar.getCellSize().getValue().floatValue()));
                toolbar.getClipLedges().setValue(nme.getData().getAsBoolean("_navMesh_clipLedges", toolbar.getClipLedges().getValue()));
                toolbar.getContourMaxDeviation().setValue(nme.getData().getAsFloat("_navMesh_contourMaxDeviation", toolbar.getContourMaxDeviation().getValue().floatValue()));
                toolbar.getContourSampleDistance().setValue(nme.getData().getAsFloat("_navMesh_contourSampleDistance", toolbar.getContourSampleDistance().getValue().floatValue()));
                toolbar.getEdgeMaxDeviation().setValue(nme.getData().getAsFloat("_navMesh_edgeMaxDeviation", toolbar.getEdgeMaxDeviation().getValue().floatValue()));
                toolbar.getMaxEdgeLength().setValue(nme.getData().getAsFloat("_navMesh_maxEdgeLength", toolbar.getMaxEdgeLength().getValue().floatValue()));
                toolbar.getMaxTraversableSlope().setValue(nme.getData().getAsFloat("_navMesh_maxTraversableSlope", toolbar.getMaxTraversableSlope().getValue().floatValue()));
                toolbar.getMaxTraversableStep().setValue(nme.getData().getAsFloat("_navMesh_maxTraversableStep", toolbar.getMaxTraversableStep().getValue().floatValue()));
                toolbar.getMaxVertsPerPoly().setValue(nme.getData().getAsInteger("_navMesh_maxVertsPerPoly", toolbar.getMaxVertsPerPoly().getValue().intValue()));
                toolbar.getMergeRegionSize().setValue(nme.getData().getAsInteger("_navMesh_mergeRegionSize", toolbar.getMergeRegionSize().getValue().intValue()));
                toolbar.getMinTraversableHeight().setValue(nme.getData().getAsFloat("_navMesh_minTraversableHeight", toolbar.getMinTraversableHeight().getValue().floatValue()));
                toolbar.getMinUnconnectedRegionSize().setValue(nme.getData().getAsInteger("_navMesh_minUnconnectedRegionSize", toolbar.getMinUnconnectedRegionSize().getValue().intValue()));
                toolbar.getSmoothingThreshold().setValue(nme.getData().getAsInteger("_navMesh_smoothingThreshold", toolbar.getSmoothingThreshold().getValue().intValue()));
                toolbar.getTimeout().setValue(nme.getData().getAsInteger("_navMesh_timeout", toolbar.getTimeout().getValue().intValue()));
                toolbar.getTraversableAreaBorderSize().setValue(nme.getData().getAsFloat("_navMesh_traversableAreaBorderSize", toolbar.getTraversableAreaBorderSize().getValue().floatValue()));
                toolbar.getUseConservativeExpansion().setValue(nme.getData().getAsBoolean("_navMesh_useConservativeExpansion", toolbar.getUseConservativeExpansion().getValue()));

                if (lastNavMesh != null) {
                    lastNavMesh.removeFromParent();
                    lastNavMesh = null;
                }
                Mesh mesh = nme.getNavMesh();
                if (mesh != null) {
                    lastNavMesh = new Geometry("navMesh-" + nme.getEntityId(), mesh);
                    lastNavMesh.setCullHint(Spatial.CullHint.Never);
                    lastNavMesh.setMaterial(MaterialUtils.createWireFrame(ColorRGBA.Green));
                    if (toolbar.getDisplayNavMeshTool().getValue()) {
                        edit.getEditRoot().attachChild(lastNavMesh);
                    }
                }
            }
        }
    }

}
