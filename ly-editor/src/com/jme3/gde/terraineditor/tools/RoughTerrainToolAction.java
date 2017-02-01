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

//import com.jme3.gde.core.sceneexplorer.nodes.AbstractSceneExplorerNode;
//import com.jme3.gde.terraineditor.ExtraToolParams;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.noise.Basis;
import com.jme3.terrain.noise.ShaderUtils;
import com.jme3.terrain.noise.basis.FilteredBasis;
import com.jme3.terrain.noise.filter.IterativeFilter;
import com.jme3.terrain.noise.filter.OptimizedErode;
import com.jme3.terrain.noise.filter.PerturbFilter;
import com.jme3.terrain.noise.filter.SmoothFilter;
import com.jme3.terrain.noise.fractal.FractalSum;
import com.jme3.terrain.noise.modulator.NoiseModulator;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.select.EntityControlTile;


public class RoughTerrainToolAction extends AbstractTerrainToolAction implements UndoRedo {
    
    private final EntityControlTile selectObj;
    private final Vector3f worldLoc;
    private final float radius;
    private final float weight;
    private final RoughExtraToolParams params;
    
    List<Vector2f> undoLocs;
    List<Float> undoHeights;

    public RoughTerrainToolAction(EntityControlTile selectObj, 
            Vector3f markerLocation, float radius, float weight, RoughExtraToolParams params) {
        this.selectObj = selectObj;
        this.worldLoc = markerLocation.clone();
        this.radius = radius;
        this.weight = weight;
        this.params = params;
    }
    
    public void doAction() {
        redo();
    }
    
    @Override
    public void undo() {
        Terrain terrain = getTerrain(selectObj);
        if (undoLocs == null || undoHeights == null)
            return;
        resetHeight(terrain, undoLocs, undoHeights);
    }

    @Override
    public void redo() {
        Terrain terrain = getTerrain(selectObj);
        roughen(terrain, worldLoc, radius, weight, params);
    }
    
    private void roughen(Terrain terrain, Vector3f worldLoc, float radius, float weight, RoughExtraToolParams params) {
        // 消除地形的位置和旋转导致的偏移,但是不消除缩放,这们允许地形在缩放、旋转和移动后进行编辑。
        // 否则地形不能旋转和移动
        Spatial terrainSpaitla = (Spatial) terrain;
        Vector3f location = worldLoc.subtract(terrainSpaitla.getWorldTranslation());
        terrainSpaitla.getWorldRotation().inverse().mult(location, location);
        
        Basis fractalFilter = createFractalGenerator(params, weight);
        List<Vector2f> locs = new ArrayList<Vector2f>();
        List<Float> heights = new ArrayList<Float>();
        
        // offset it by radius because in the loop we iterate through 2 radii
        int radiusStepsX = (int) (radius / ((Node)terrain).getLocalScale().x);
        int radiusStepsZ = (int) (radius / ((Node)terrain).getLocalScale().z);
        float xStepAmount = ((Node)terrain).getLocalScale().x;
        float zStepAmount = ((Node)terrain).getLocalScale().z;
        
        int r2 = (int) (radius*2);
        FloatBuffer fb = fractalFilter.getBuffer(location.x, location.z, 0, r2);
        
        int xfb = 0, yfb = 0;
        for (int z = -radiusStepsZ; z < radiusStepsZ; z++) {
            for (int x = -radiusStepsX; x < radiusStepsX; x++) {
                
                float locX = location.x + (x * xStepAmount);
                float locZ = location.z + (z * zStepAmount);
                
                float height = fb.get(yfb*r2 + xfb);
                
                if (isInRadius(locX - location.x, locZ - location.z, radius)) {
                    // see if it is in the radius of the tool
                    float h = calculateHeight(radius, height, locX - location.x, locZ - location.z);
                    locs.add(new Vector2f(locX, locZ));
                    heights.add(h);
                }
                xfb++;
            }
            yfb++;
            xfb = 0;
        }
        
        undoLocs = locs;
        undoHeights = heights;
        
        // do the actual height adjustment
        terrain.adjustHeight(locs, heights);

        ((Node)terrain).updateModelBound(); // or else we won't collide with it where we just edited
    }
    
    private boolean isInRadius(float x, float y, float radius) {
        Vector2f point = new Vector2f(x, y);
        // return true if the distance is less than equal to the radius
        return point.length() <= radius;
    }

    private float calculateHeight(float radius, float heightFactor, float x, float z) {
        // find percentage for each 'unit' in radius
        Vector2f point = new Vector2f(x, z);
        float val = point.length() / radius;
        val = 1 - val;
        if (val <= 0) {
            val = 0;
        }
        return heightFactor * val * 0.1f; // 0.1 scales it down a bit to lower the impact of the tool
    }
    
    private void resetHeight(Terrain terrain, List<Vector2f> undoLocs, List<Float> undoHeights) {
        List<Float> neg = new ArrayList<Float>();
        for (Float f : undoHeights)
            neg.add( f * -1f );
        
        terrain.adjustHeight(undoLocs, neg);
        ((Node)terrain).updateModelBound();
    }
    
    private Basis createFractalGenerator(RoughExtraToolParams params, float weight) {
        FractalSum base = new FractalSum();
        base.setRoughness(params.roughness);
        base.setFrequency(params.frequency);
        base.setAmplitude(weight);
        base.setLacunarity(params.lacunarity <= 1 ? 1.1f : params.lacunarity); // make it greater than 1.0f
        base.setOctaves(params.octaves);
        float scale = params.scale;
        if (scale > 1.0f)
            scale = 1.0f;
        if (scale < 0)
            scale = 0;
        base.setScale(scale);//0.02125f
        base.addModulator(new NoiseModulator() {
            @Override
            public float value(float... in) {
                return ShaderUtils.clamp(in[0] * 0.5f + 0.5f, 0, 1);
            }
        });

        FilteredBasis ground = new FilteredBasis(base);

        PerturbFilter perturb = new PerturbFilter();
        perturb.setMagnitude(0.2f);//0.119 the higher, the slower it is

        OptimizedErode therm = new OptimizedErode();
        therm.setRadius(5);
        therm.setTalus(0.011f);

        SmoothFilter smooth = new SmoothFilter();
        smooth.setRadius(1);
        smooth.setEffect(0.1f); // 0.7

        IterativeFilter iterate = new IterativeFilter();
        iterate.addPreFilter(perturb);
        iterate.addPostFilter(smooth);
        iterate.setFilter(therm);
        iterate.setIterations(1);

        ground.addPreFilter(iterate);
        
        return ground;
    }
}
