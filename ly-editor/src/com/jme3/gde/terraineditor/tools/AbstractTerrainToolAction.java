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

import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import name.huliqing.editor.edit.select.EntitySelectObj;

public abstract class AbstractTerrainToolAction {
    
    protected Terrain getTerrain(EntitySelectObj eso) {
        Spatial terrainSpatial = eso.getObject().getSpatial();
        if (!(terrainSpatial instanceof Terrain)) {
            throw new IllegalStateException("terrainSpatial must be a Terrain object! eso=" + eso);
        }
        Terrain terrain = (Terrain) terrainSpatial;
        return terrain;
    }
    
    // remove20170131
//    protected void modifyHeight(Terrain terrain, Vector3f worldLoc, float radius, float heightDir) {
//
//        int radiusStepsX = (int) (radius / ((Node)terrain).getWorldScale().x);
//        int radiusStepsZ = (int) (radius / ((Node)terrain).getWorldScale().z);
//
//        float xStepAmount = ((Node)terrain).getWorldScale().x;
//        float zStepAmount = ((Node)terrain).getWorldScale().z;
//
//        List<Vector2f> locs = new ArrayList<Vector2f>();
//        List<Float> heights = new ArrayList<Float>();
//        
//        for (int z=-radiusStepsZ; z<radiusStepsZ; z++) {
//            for (int x=-radiusStepsX; x<radiusStepsX; x++) {
//
//                float locX = worldLoc.x + (x*xStepAmount);
//                float locZ = worldLoc.z + (z*zStepAmount);
//
//                // see if it is in the radius of the tool
//                if (ToolUtils.isInRadius(locX-worldLoc.x,locZ-worldLoc.z,radius)) {
//                    // adjust height based on radius of the tool
//                    float h = ToolUtils.calculateHeight(radius, heightDir, locX-worldLoc.x, locZ-worldLoc.z);
//                    // increase the height
//                    locs.add(new Vector2f(locX, locZ));
//                    heights.add(h);
//                }
//            }
//        }
//
//        // do the actual height adjustment
//        terrain.adjustHeight(locs, heights);
//
//        ((Node)terrain).updateModelBound(); // or else we won't collide with it where we just edited
//    }
}
