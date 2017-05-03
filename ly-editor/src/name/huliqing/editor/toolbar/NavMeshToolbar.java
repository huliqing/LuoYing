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
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.FloatValueTool;
import name.huliqing.editor.tools.IntegerValueTool;
import name.huliqing.editor.tools.navmesh.NavMeshGenTool;
import name.huliqing.editor.tools.EntityValueTool;
import name.huliqing.luoying.object.entity.impl.NavMeshEntity;

/**
 * 用于为场景创建寻路网格的工具栏
 * @author huliqing
 */
public class NavMeshToolbar extends EditToolbar<SimpleJmeEdit> {
    
    private EntityValueTool navMeshSourceTool;
    private FloatValueTool cellSize;
    private FloatValueTool cellHeight;
    private FloatValueTool minTraversableHeight;
    private FloatValueTool maxTraversableStep;
    private FloatValueTool maxTraversableSlope;
    private BooleanValueTool clipLedges;
    private FloatValueTool traversableAreaBorderSize;
    private IntegerValueTool smoothingThreshold;
    private BooleanValueTool useConservativeExpansion;
    private IntegerValueTool minUnconnectedRegionSize;
    private IntegerValueTool mergeRegionSize;
    private FloatValueTool maxEdgeLength;
    private FloatValueTool edgeMaxDeviation;
    private IntegerValueTool maxVertsPerPoly;
    private FloatValueTool contourSampleDistance;
    private FloatValueTool contourMaxDeviation;
//    private FloatValueTool intermediateData;
    private FloatValueTool timeout;
    
    private BooleanValueTool displayNavMeshTool;
    private NavMeshGenTool navMeshGenTool;

    public NavMeshToolbar(SimpleJmeEdit edit) {
        super(edit);
    }

    @Override
    public String getName() {
        return Manager.getRes(ResConstants.EDIT_TOOLBAR_NAVMESH);
    }

    @Override
    public void initialize() {
        super.initialize();
        navMeshSourceTool = new EntityValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_SOURCE), Manager.getRes(ResConstants.TOOL_NAVMESH_SOURCE_TIP), null);
        cellSize = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_CELLSIZE), Manager.getRes(ResConstants.TOOL_NAVMESH_CELLSIZE_TIP), null);
        cellHeight = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_CELLHEIGHT), Manager.getRes(ResConstants.TOOL_NAVMESH_CELLHEIGHT_TIP), null);
        minTraversableHeight = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_MINTRAVERSABLEHEIGHT), Manager.getRes(ResConstants.TOOL_NAVMESH_MINTRAVERSABLEHEIGHT_TIP), null);
        maxTraversableStep = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_MAXTRAVERSABLESTEP), Manager.getRes(ResConstants.TOOL_NAVMESH_MAXTRAVERSABLESTEP_TIP), null);
        maxTraversableSlope = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_MAXTRAVERSABLESLOPE), Manager.getRes(ResConstants.TOOL_NAVMESH_MAXTRAVERSABLESLOPE_TIP), null);
        clipLedges = new BooleanValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_CLIPLEDGES), Manager.getRes(ResConstants.TOOL_NAVMESH_CLIPLEDGES_TIP), null);
        traversableAreaBorderSize = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_TRAVERSABLEAREABORDERSIZE), Manager.getRes(ResConstants.TOOL_NAVMESH_TRAVERSABLEAREABORDERSIZE_TIP), null);
        smoothingThreshold = new IntegerValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_SMOOTHINGTHRESHOLD), Manager.getRes(ResConstants.TOOL_NAVMESH_SMOOTHINGTHRESHOLD_TIP), null);
        useConservativeExpansion = new BooleanValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_USECONSERVATIVEEXPANSION), Manager.getRes(ResConstants.TOOL_NAVMESH_USECONSERVATIVEEXPANSION_TIP), null);
        minUnconnectedRegionSize = new IntegerValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_MINUNCONNECTEDREGIONSIZE), Manager.getRes(ResConstants.TOOL_NAVMESH_MINUNCONNECTEDREGIONSIZE_TIP), null);
        mergeRegionSize = new IntegerValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_MERGEREGIONSIZE), Manager.getRes(ResConstants.TOOL_NAVMESH_MERGEREGIONSIZE_TIP), null);
        maxEdgeLength = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_MAXEDGELENGTH), Manager.getRes(ResConstants.TOOL_NAVMESH_MAXEDGELENGTH_TIP), null);
        edgeMaxDeviation = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_EDGEMAXDEVIATION), Manager.getRes(ResConstants.TOOL_NAVMESH_EDGEMAXDEVIATION_TIP), null);
        maxVertsPerPoly = new IntegerValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_MAXVERTSPERPOLY), Manager.getRes(ResConstants.TOOL_NAVMESH_MAXVERTSPERPOLY_TIP), null);
        contourSampleDistance = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_CONTOURSAMPLEDISTANCE), Manager.getRes(ResConstants.TOOL_NAVMESH_CONTOURSAMPLEDISTANCE_TIP), null);
        contourMaxDeviation = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_CONTOURMAXDEVIATION), Manager.getRes(ResConstants.TOOL_NAVMESH_CONTOURMAXDEVIATION_TIP), null);
        timeout = new FloatValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_TIMEOUT), Manager.getRes(ResConstants.TOOL_NAVMESH_TIMEOUT_TIP), null);
        displayNavMeshTool = new BooleanValueTool(Manager.getRes(ResConstants.TOOL_NAVMESH_DISPLAY), Manager.getRes(ResConstants.TOOL_NAVMESH_DISPLAY_TIP), null);
        navMeshGenTool = new NavMeshGenTool(Manager.getRes(ResConstants.TOOL_NAVMESH_GEN), Manager.getRes(ResConstants.TOOL_NAVMESH_GEN_TIP), null);
        
        navMeshSourceTool.setEntityType(NavMeshEntity.class);
        cellSize.setValue(1f);
        cellHeight.setValue(1.5f);
        minTraversableHeight.setValue(7.5f);
        maxTraversableStep.setValue(1f);
        maxTraversableSlope.setValue(48.0f);
        clipLedges.setValue(false);
        traversableAreaBorderSize.setValue(1.2f);
        smoothingThreshold.setValue(2);
        useConservativeExpansion.setValue(true);
        minUnconnectedRegionSize.setValue(3);
        mergeRegionSize.setValue(10);
        maxEdgeLength.setValue(0);
        edgeMaxDeviation.setValue(2.4f);
        maxVertsPerPoly.setValue(6);
        contourSampleDistance.setValue(25);
        contourMaxDeviation.setValue(25);
        timeout.setValue(10000);
        displayNavMeshTool.setValue(true);
        
        add(navMeshSourceTool);
        add(cellSize);
        add(cellHeight);
        add(minTraversableHeight);
        add(maxTraversableStep);
        add(maxTraversableSlope);
        add(clipLedges);
        add(traversableAreaBorderSize);
        add(smoothingThreshold);
        add(useConservativeExpansion);
        add(minUnconnectedRegionSize);
        add(mergeRegionSize);
        add(maxEdgeLength);
        add(edgeMaxDeviation);
        add(maxVertsPerPoly);
        add(contourSampleDistance);
        add(contourMaxDeviation);
        add(timeout);
        add(displayNavMeshTool);
        add(navMeshGenTool);
        
        setEnabled(navMeshSourceTool, true);
        setEnabled(cellSize, true);
        setEnabled(cellHeight, true);
        setEnabled(minTraversableHeight, true);
        setEnabled(maxTraversableStep, true);
        setEnabled(maxTraversableSlope, true);
        setEnabled(clipLedges, true);
        setEnabled(traversableAreaBorderSize, true);
        setEnabled(smoothingThreshold, true);
        setEnabled(useConservativeExpansion, true);
        setEnabled(minUnconnectedRegionSize, true);
        setEnabled(mergeRegionSize, true);
        setEnabled(maxEdgeLength, true);
        setEnabled(edgeMaxDeviation, true);
        setEnabled(maxVertsPerPoly, true);
        setEnabled(contourSampleDistance, true);
        setEnabled(contourMaxDeviation, true);
        setEnabled(timeout, true);
        setEnabled(displayNavMeshTool, true);
        setEnabled(navMeshGenTool, true);
    }

    @Override
    public void cleanup() {
        removeAll();
        clearToggleMappings();
        super.cleanup();
    }

    public EntityValueTool getNavMeshSourceTool() {
        return navMeshSourceTool;
    }

    public FloatValueTool getCellSize() {
        return cellSize;
    }

    public FloatValueTool getCellHeight() {
        return cellHeight;
    }

    public FloatValueTool getMinTraversableHeight() {
        return minTraversableHeight;
    }

    public FloatValueTool getMaxTraversableStep() {
        return maxTraversableStep;
    }

    public FloatValueTool getMaxTraversableSlope() {
        return maxTraversableSlope;
    }

    public BooleanValueTool getClipLedges() {
        return clipLedges;
    }

    public FloatValueTool getTraversableAreaBorderSize() {
        return traversableAreaBorderSize;
    }

    public IntegerValueTool getSmoothingThreshold() {
        return smoothingThreshold;
    }

    public BooleanValueTool getUseConservativeExpansion() {
        return useConservativeExpansion;
    }

    public IntegerValueTool getMinUnconnectedRegionSize() {
        return minUnconnectedRegionSize;
    }

    public IntegerValueTool getMergeRegionSize() {
        return mergeRegionSize;
    }

    public FloatValueTool getMaxEdgeLength() {
        return maxEdgeLength;
    }

    public FloatValueTool getEdgeMaxDeviation() {
        return edgeMaxDeviation;
    }

    public IntegerValueTool getMaxVertsPerPoly() {
        return maxVertsPerPoly;
    }

    public FloatValueTool getContourSampleDistance() {
        return contourSampleDistance;
    }

    public FloatValueTool getContourMaxDeviation() {
        return contourMaxDeviation;
    }

    public FloatValueTool getTimeout() {
        return timeout;
    }

    public BooleanValueTool getDisplayNavMeshTool() {
        return displayNavMeshTool;
    }

    public NavMeshGenTool getNavMeshGenTool() {
        return navMeshGenTool;
    }
    
    
}
