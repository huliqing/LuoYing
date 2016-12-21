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
package name.huliqing.luoying.utils.modifier;

import com.jme3.asset.AssetManager;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.utils.GeometryUtils;
import name.huliqing.luoying.utils.ModelFileUtils;

/**
 * 该类主要用于处理简模树的透明问题。通过将J3O载入并设置材质后再重新编译。
 * @author huliqing
 */
public class TreeFixUtils {
    private static float alphaFallOff = 0.5f;
    
    public static void fix(String file, String... childGeometryName) {
        AssetManager am = LuoYing.getAssetManager();
        
        // 1.重设material
        Spatial spatial = am.loadModel(file);
        if (spatial instanceof Geometry) {
            resetMaterial((Geometry) spatial);
        } else {
            Node model = (Node) spatial;
            for (String geoName : childGeometryName) {
                Geometry geo = (Geometry) model.getChild(geoName);
                if (geo == null) {
                    continue;
                }
                resetMaterial(geo);
            }            
        }
        
        // 2.处理为Unshaded
        GeometryUtils.makeUnshaded(spatial);
        
        // 3.保存到系统
        ModelFileUtils.saveTo(spatial, file);
        Logger.getLogger(TreeFixUtils.class.getName()).log(Level.INFO, "Convert ok!");
    }
    
    private static void resetMaterial(Geometry geo) {
        // 准许透明
        geo.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        // 树叶不需要Cull
        geo.getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        
        // 重要：让alpha透明图片不会盖住后面的图片（特别是在手机上）
        geo.getMaterial().getAdditionalRenderState().setDepthWrite(true);
        geo.getMaterial().getAdditionalRenderState().setDepthTest(true);
        geo.getMaterial().getAdditionalRenderState().setAlphaTest(true);
        geo.getMaterial().getAdditionalRenderState().setAlphaFallOff(alphaFallOff);
        geo.getMaterial().getAdditionalRenderState().setColorWrite(true);
        
        geo.getMaterial().setBoolean("UseAlpha", true);
        geo.getMaterial().setBoolean("UseMaterialColors", true);
        geo.getMaterial().setFloat("AlphaDiscardThreshold", alphaFallOff);
        geo.getMaterial().setFloat("Shininess", 16);
        geo.getMaterial().setColor("Ambient", new ColorRGBA(0.7f, 0.7f, 0.7f, 1f));
        geo.getMaterial().setColor("Diffuse", new ColorRGBA(0.7f, 0.7f, 0.7f, 1f));
        geo.getMaterial().setColor("Specular", new ColorRGBA(0, 0, 0, 1));
        
        // 设置Transparent，否则与背景的天空盒混合时会树叶的透明信息变成黑色。
        // 部分树形不能使用Translucent，只能用Transparent
        geo.setQueueBucket(Bucket.Transparent);
    }
    
    public static void fix_trees_dry() {
        alphaFallOff = 0.5f;
        fix("Models/trees/dry/dry389.j3o", "dry389-geom-1");
        fix("Models/trees/dry/dry390.j3o", "dry390-geom-1");
        fix("Models/trees/dry/dry410.j3o", "dry410-geom-1");
    }
    
    public static void fix_trees_fir() {
        alphaFallOff = 0.5f;
        fix("Models/trees/fir/fir473.j3o", "fir473-geom-0");
        fix("Models/trees/fir/fir483.j3o", "fir483-geom-0");
        fix("Models/trees/fir/fir527.j3o", "fir527-geom-0");
    }
    
    public static void fix_trees_palm() {
        alphaFallOff = 0.5f;
        fix("Models/trees/palm/palm111.j3o", "palm111-geom-1");
        fix("Models/trees/palm/palm123.j3o", "palm123-geom-1");
        fix("Models/trees/palm/palm225.j3o", "palm225-geom-1");
    }
    
    public static void fix_trees_tree() {
        alphaFallOff = 0.5f;
        fix("Models/trees/tree/tree381.j3o", "tree381-geom-1");
        fix("Models/trees/tree/tree431.j3o", "tree431-geom-1");
        fix("Models/trees/tree/tree613.j3o", "tree613-geom-0");
        fix("Models/trees/tree/tree978.j3o", "tree978-geom-1");
    }   
    
    public static void fix_plant_flower() {
        alphaFallOff = 0.5f;
        fix("Models/plant/flower/flower1.j3o");
        fix("Models/plant/flower/flower2.j3o");
        fix("Models/plant/flower/flower3.j3o");
        fix("Models/plant/flower/flower4.j3o");
        fix("Models/plant/flower/flower5.j3o");
        fix("Models/plant/flower/flower6.j3o");
        fix("Models/plant/flower/flower7.j3o");
        fix("Models/plant/flower/flower8.j3o");
        fix("Models/plant/flower/flower9.j3o");
        fix("Models/plant/flower/camellia.j3o");
    }
    
    public static void fix_plant_fruit() {
        alphaFallOff = 0.5f;
        fix("Models/plant/fruit/acron.j3o");
        fix("Models/plant/fruit/beet.j3o");
        fix("Models/plant/fruit/carrot.j3o", "carrot-geom-0");
    }
    
    public static void fix_plant_grass() {
        alphaFallOff = 0.5f;
        fix("Models/plant/grass/fern.j3o");
        fix("Models/plant/grass/fern2.j3o");
        fix("Models/plant/grass/grass_clump.j3o");
        fix("Models/plant/grass/leaves.j3o");
        fix("Models/plant/grass/tussockA.j3o");
        fix("Models/plant/grass/tussockB.j3o");
    }
    
    public static void fix_trees_test() {
        alphaFallOff = 0.5f;
        fix("Textures/test/tt3/Tree.j3o", "Tree-geom-1");
    }
    
    public static void main(String[] args) {

//        fix_trees_dry();
//        fix_trees_fir();
//        fix_trees_palm();
//        fix_trees_tree();
//        fix_plant_flower();
//        fix_plant_fruit();
//        fix_plant_grass();
        
    }
}
