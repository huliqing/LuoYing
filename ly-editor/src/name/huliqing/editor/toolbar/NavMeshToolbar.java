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
    
    private NavMeshGenTool navMeshGenTool;

    public NavMeshToolbar(SimpleJmeEdit edit) {
        super(edit);
    }

    @Override
    public String getName() {
        return Manager.getRes("<NavMeshToolbar>");
    }

    @Override
    public void initialize() {
        super.initialize();
        navMeshSourceTool = new EntityValueTool("NavMeshSourceTool", "NavMeshSourceTool", null);
        cellSize = new FloatValueTool("<cellSize>", "<cellSize>", null);
        cellHeight = new FloatValueTool("<cellHeight>", "<cellHeight>", null);
        minTraversableHeight = new FloatValueTool("<minTraversableHeight>", "<minTraversableHeight>", null);
        maxTraversableStep = new FloatValueTool("<maxTraversableStep>", "<maxTraversableStep>", null);
        maxTraversableSlope = new FloatValueTool("<maxTraversableSlope>", "<maxTraversableSlope>", null);
        clipLedges = new BooleanValueTool("<clipLedges>", "<clipLedges>", null);
        traversableAreaBorderSize = new FloatValueTool("<traversableAreaBorderSize>", "<traversableAreaBorderSize>", null);
        smoothingThreshold = new IntegerValueTool("<smoothingThreshold>", "<smoothingThreshold>", null);
        useConservativeExpansion = new BooleanValueTool("<useConservativeExpansion>", "<useConservativeExpansion>", null);
        minUnconnectedRegionSize = new IntegerValueTool("<minUnconnectedRegionSize>", "<minUnconnectedRegionSize>", null);
        mergeRegionSize = new IntegerValueTool("<mergeRegionSize>", "<mergeRegionSize>", null);
        maxEdgeLength = new FloatValueTool("<maxEdgeLength>", "<maxEdgeLength>", null);
        edgeMaxDeviation = new FloatValueTool("<edgeMaxDeviation>", "<edgeMaxDeviation>", null);
        maxVertsPerPoly = new IntegerValueTool("<maxVertsPerPoly>", "<maxVertsPerPoly>", null);
        contourSampleDistance = new FloatValueTool("<contourSampleDistance>", "<contourSampleDistance>", null);
        contourMaxDeviation = new FloatValueTool("<contourMaxDeviation>", "<contourMaxDeviation>", null);
        timeout = new FloatValueTool("<timeout>", "<timeout>", null);
        navMeshGenTool = new NavMeshGenTool("<navMeshGenTool>", "<navMeshGenTool>", null);
        
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

    public NavMeshGenTool getNavMeshGenTool() {
        return navMeshGenTool;
    }
    
    
}
