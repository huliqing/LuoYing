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
package name.huliqing.luoying.utils;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import name.huliqing.luoying.LuoYing;

/**
 *
 * @author huliqing
 */
public class MaterialUtils {
    
    public static Material createWireFrame() {
        return createWireFrame(ColorRGBA.White);
    }
    
    public static Material createWireFrame(ColorRGBA color) {
        Material mat = new Material(LuoYing.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.setColor("Color", color);
        return mat;
    }
    
    /**
     * @param tex 贴图文件路径
     * @return 
     * @see #createTransparent(com.jme3.texture.Texture) 
     */
    public static Material createTransparent(String tex) {
        Texture texture = LuoYing.getAssetManager().loadTexture(tex);
        return createTransparent(texture);
    }
    
    /**
     * 载入材质，该材质会使用滤色的方式去除图片中的黑色背景，
     * 一般用于“效果”贴图.注意：目标Spatial必须设置Bucket为：
     * geo.setQueueBucket(RenderQueue.Bucket.Transparent);否则可能会看不
     * 到物体。
     * 该材质使用<b>Common/MatDefs/Misc/Unshaded.j3md</b>
     * @param tex 贴图
     * @return 
     */
    public static Material createTransparent(Texture tex) {
        Material mat = new Material(LuoYing.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setAlphaTest(true);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
        mat.getAdditionalRenderState().setColorWrite(true);
        mat.getAdditionalRenderState().setDepthTest(true);
        mat.getAdditionalRenderState().setDepthWrite(false);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setPointSprite(false);
        mat.setTexture("ColorMap", tex);
        return mat;
    }
    
    /**
     * 创建特效粒子所使用的材质。
     * use "Common/MatDefs/Misc/Particle.j3md"
     * @param texture 贴图文件
     * @return 
     */
    public static Material createParticle(String texture) {
        AssetManager am = LuoYing.getAssetManager();
        Material mat = new Material(am, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", am.loadTexture(texture));
        return mat;
    }
    
    public static Material createSkillCooldown(ColorRGBA maskColor) {
        final Material mat = new Material(LuoYing.getAssetManager(),"MatDefs/Skill/Cooldown.j3md");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setColor("Color", maskColor);
        return mat;
    }
    
    public static Material createUnshaded() {
        Material mat = new Material(LuoYing.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        return mat;
    }
}
