/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.utils;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import name.huliqing.ly.Ly;

/**
 *
 * @author huliqing
 */
public class MatUtils {
    
    public static Material createWireFrame() {
        return createWireFrame(ColorRGBA.White);
    }
    
    public static Material createWireFrame(ColorRGBA color) {
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
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
        Texture texture = Ly.getAssetManager().loadTexture(tex);
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
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
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
        AssetManager am = Ly.getAssetManager();
        Material mat = new Material(am, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", am.loadTexture(texture));
        return mat;
    }
    
    public static Material createSkillCooldown(ColorRGBA maskColor) {
        final Material mat = new Material(Ly.getAssetManager(),"MatDefs/Skill/Cooldown.j3md");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setColor("Color", maskColor);
        return mat;
    }
    
    public static Material createUnshaded() {
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        return mat;
    }
}
