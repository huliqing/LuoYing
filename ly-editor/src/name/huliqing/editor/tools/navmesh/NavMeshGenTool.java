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

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3tools.optimize.GeometryBatchFactory;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.toolbar.NavMeshToolbar;
import name.huliqing.editor.tools.AbstractTool;
import name.huliqing.editor.tools.ButtonTool;
import name.huliqing.editor.tools.EntityValueTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.GeometryUtils;
import name.huliqing.luoying.utils.modifier.NavMeshGenerator;

/**
 * NavMesh创建工具
 * @author huliqing
 */
public class NavMeshGenTool extends AbstractTool<SimpleJmeEdit, NavMeshToolbar> 
        implements ButtonTool<SimpleJmeEdit, NavMeshToolbar>, ValueChangedListener<Entity> {
    
    private EntityValueTool navMeshSourceTool;

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
        navMeshSourceTool.addValueChangeListener(this);
    }

    @Override
    public void cleanup() {
        navMeshSourceTool.removeValueChangeListener(this);
        super.cleanup();
    }

    @Override
    public void onValueChanged(ValueTool<Entity> vt, Entity oldValue, Entity newValue) {
        if (newValue != null) {
//            newValue.getData().getAsFloat("");
            //
        }
    }
    
    @Override
    public void doAction() {
        
        if (!(edit instanceof SceneEdit)) {
            Logger.getLogger(NavMeshGenTool.class.getName()).log(Level.WARNING
                    , "NavMeshGenTool only support SceneEdit, edit={0}", edit.getClass());
            return;
        }
        List<Geometry> geometries = GeometryUtils.findAllGeometry(((SceneEdit) edit).getScene().getRoot());
        
        Mesh navMesh = createNavMesh(geometries);
        Geometry navGeo = new Geometry("NavMeshGeo", navMesh);
        navGeo.setCullHint(Spatial.CullHint.Always);
        navGeo.setModelBound(new BoundingBox());
        navGeo.setMaterial(new Material(LuoYing.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));
        
    }
    
    private Mesh createNavMesh(List<Geometry> geometries) {
        Mesh mesh = new Mesh();
        GeometryBatchFactory.mergeGeometries(geometries, mesh);
        return createNavMeshGenerator().optimize(mesh);
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
        return generator;
    }

        
}
