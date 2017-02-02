/*
 * Copyright (c) 2009-2011 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.gde.terraineditor.tools;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;


public class SmoothTerrainToolAction extends AbstractTerrainToolAction implements UndoRedo {
    
    private final EntityControlTile selectObj;
    private final Vector3f worldLoc;
    private final float radius;
    private final float weight;
    
    List<Vector2f> undoLocs;
    List<Float> undoHeights;

    public SmoothTerrainToolAction(EntityControlTile selectObj, Vector3f markerLocation, float radius, float weight) {
        this.selectObj = selectObj;
        this.worldLoc = markerLocation.clone();
        this.radius = radius;
        this.weight = weight;
    }
    
    public void doAction() {
        redo();
    }

    @Override
    public void undo() {
        if (undoLocs == null || undoHeights == null)
            return;
        Terrain terrain = getTerrain(selectObj);
        resetHeight(terrain, undoLocs, undoHeights);
    }

    @Override
    public void redo() {
        Terrain terrain = getTerrain(selectObj);
        modifyHeight(terrain, worldLoc, radius, weight);
    }
    
    private void modifyHeight(Terrain terrain, Vector3f worldLoc, float radius, float height) {
        // 消除地形的位置和旋转导致的偏移,但是不消除缩放,这们允许地形在缩放、旋转和移动后进行编辑。
        // 否则地形不能旋转和移动
        Spatial terrainSpaitla = (Spatial) terrain;
        Vector3f location = worldLoc.subtract(terrainSpaitla.getWorldTranslation());
        terrainSpaitla.getWorldRotation().inverse().mult(location, location);
        
        int radiusStepsX = (int)(radius / ((Node)terrain).getLocalScale().x);
        int radiusStepsZ = (int)(radius / ((Node)terrain).getLocalScale().z);

        float xStepAmount = ((Node)terrain).getLocalScale().x;
        float zStepAmount = ((Node)terrain).getLocalScale().z;

        List<Vector2f> locs = new ArrayList<Vector2f>();
        List<Float> heights = new ArrayList<Float>();

        for (int z = -radiusStepsZ; z < radiusStepsZ; z++) {
            for (int x = -radiusStepsX; x < radiusStepsX; x++) {

                float locX = location.x + (x*xStepAmount);
                float locZ = location.z + (z*zStepAmount);

                // see if it is in the radius of the tool
                if (ToolUtils.isInRadius(locX - location.x, locZ - location.z, radius)) {

                    Vector2f terrainLoc = new Vector2f(locX, locZ);
                    // adjust height based on radius of the tool
                    float center = terrain.getHeightmapHeight(terrainLoc);
                    float left = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x-1, terrainLoc.y));
                    float right = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x+1, terrainLoc.y));
                    float up = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x, terrainLoc.y+1));
                    float down = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x, terrainLoc.y-1));
                    int count = 1;
                    float amount = center;
                    if ( !isNaN(left) ) {
                        amount += left;
                        count++;
                    }
                    if ( !isNaN(right) ) {
                        amount += right;
                        count++;
                    }
                    if ( !isNaN(up) ) {
                        amount += up;
                        count++;
                    }
                    if ( !isNaN(down) ) {
                        amount += down;
                        count++;
                    }

                    amount /= count; // take average

                    // weigh it
                    float diff = amount-center;
                    diff *= height;
                        
                    locs.add(terrainLoc);
                    heights.add(diff);
                }
            }
        }
        
        undoLocs = locs;
        undoHeights = heights;
        
        // do the actual height adjustment
        terrain.adjustHeight(locs, heights);

        ((Node)terrain).updateModelBound(); // or else we won't collide with it where we just edited
    }
    
    private boolean isNaN(float val) {
        return val != val;
    }
    
    private void resetHeight(Terrain terrain, List<Vector2f> undoLocs, List<Float> undoHeights) {
        List<Float> neg = new ArrayList<Float>();
        for (Float f : undoHeights)
            neg.add( f * -1f );
        
        terrain.adjustHeight(undoLocs, neg);
        ((Node)terrain).updateModelBound();
    }
}
