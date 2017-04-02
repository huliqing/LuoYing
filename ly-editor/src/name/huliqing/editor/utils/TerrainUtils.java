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
package name.huliqing.editor.utils;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bounding.BoundingBox;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.gde.terraineditor.tools.PaintTerrainToolAction;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import jme3tools.converters.ImageToAwt;

/**
 *
 * @author huliqing
 */
public class TerrainUtils {
    private static final Logger LOG = Logger.getLogger(TerrainUtils.class.getName());
    public final static String MATERIAL_TERRAIN_LIGHTING = "Common/MatDefs/Terrain/TerrainLighting.j3md";
    public final static float DEFAULT_TEXTURE_SCALE = 16.0625f;
    public final static int NUM_ALPHA_TEXTURES = 3;
    public final static int MAX_TEXTURES = 16 - NUM_ALPHA_TEXTURES; // 16 max (diffuse and normal), minus the ones we are reserving
    /** 默认的地形贴图路径在asset的LuoYing/assets/Textures/目录下 */
    public final static String TERRAIN_DIRT = "LuoYing/assets/Textures/dirt.jpg";
    
    /**
     * @param app
     * @param assetFolder 资源文件夹的绝对路径,示例：C:\\home\\workspace\\jme3.1\\luoying\\ly-editor\\assets"
     * @param alphaTextureAssetDir, 资源文件夹中的目录，示例："/Textures/terrain-alpha"
     * @param terrainName 地形名称，如： test-terrain
     * @param totalSize 地形大小，必须是2的平方
     * @param patchSize 地形分块大小，必须是2的平方，并且必须小于totalSize
     * @param alphaTextureSize alpha贴图的大小,一般保持与totalSize大小即可。
     * @param heightmapData 默认的高度图数据，如果没有由可以为null.
     * @param defaultDiffuseTexture 默认的贴图位置，如：LuoYing/Assets/Textures/dirt.jpg
     * @return
     * @throws IOException 
     */
    public final static Terrain doCreateTerrain(Application app, String assetFolder, String alphaTextureAssetDir
            , String terrainName, int totalSize, int patchSize, int alphaTextureSize
            , float[] heightmapData, String defaultDiffuseTexture)
            throws IOException {
        AssetManager manager = app.getAssetManager();
        File alphaFolder = new File(assetFolder + alphaTextureAssetDir);
        if (!alphaFolder.exists()) {
            alphaFolder.mkdirs();
        }
        Terrain terrain = new TerrainQuad(terrainName, patchSize + 1, totalSize + 1, heightmapData);
        Material mat = new Material(manager, MATERIAL_TERRAIN_LIGHTING);
        // write out 3 alpha blend images
        for (int i = 0; i < NUM_ALPHA_TEXTURES; i++) {
            BufferedImage alphaBlend = new BufferedImage(alphaTextureSize, alphaTextureSize, BufferedImage.TYPE_INT_ARGB);
            if (i == 0) {
                // the first alpha level should be opaque so we see the first texture over the whole terrain
                for (int h = 0; h < alphaTextureSize; h++)
                    for (int w = 0; w < alphaTextureSize; w++)
                        alphaBlend.setRGB(w, h, 0x00FF0000);//argb
            }
            
            String alphaTextureName = alphaTextureAssetDir + "/" + terrainName + "-alphablend" + i + ".png";
            ImageIO.write(alphaBlend, "png", new File(assetFolder + alphaTextureName));
            Texture tex = null;
            if (alphaTextureName.trim().startsWith("/")) {
                tex = manager.loadAsset(new TextureKey(alphaTextureName.trim().substring(1), false));
            } else {
                tex = manager.loadAsset(new TextureKey(alphaTextureName.trim(), false));
            }
            switch (i) {
                case 0:
                    mat.setTexture("AlphaMap", tex);
                    break;
                case 1:
                    mat.setTexture("AlphaMap_1", tex);
                    break;
                case 2:
                    mat.setTexture("AlphaMap_2", tex);
                    break;
                default:
                    break;
            }
        }
        
//        // copy the default texture to the assets folder if it doesn't exist there yet
//        String dirtTextureName = "/Textures/dirt.jpg";
//        File dirtTextureFile = new File(assetFolder + dirtTextureName);
//        if (!dirtTextureFile.exists()) {
//            BufferedImage bi = ImageToAwt.convert(manager.loadTexture(AssetConstants.TEXTURES_TERRAIN_DIRT).getImage(), false, true, 0);
//            ImageIO.write(bi, "jpg", dirtTextureFile);
//        }
        
        // give the first layer default texture
        if (defaultDiffuseTexture.trim().startsWith("/")) {
            defaultDiffuseTexture = defaultDiffuseTexture.trim().substring(1);
        }
        Texture dirtTexture = manager.loadTexture(defaultDiffuseTexture);
        dirtTexture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", dirtTexture);
        mat.setFloat("DiffuseMap_0_scale", DEFAULT_TEXTURE_SCALE);
        mat.setBoolean("WardIso", true);
        mat.setFloat("Shininess", 0.01f);

        ((Node)terrain).setMaterial(mat);
        ((Node)terrain).setModelBound(new BoundingBox());
        ((Node)terrain).updateModelBound();
        ((Node)terrain).setLocalTranslation(0, 0, 0);
        ((Node)terrain).setLocalScale(1f, 1f, 1f);

        // add the lod control
        TerrainLodControl control = new TerrainLodControl(terrain, app.getCamera());
        control.setLodCalculator(new DistanceLodCalculator(patchSize, 2.7f));
	((Node)terrain).addControl(control);
        
        return terrain;
    }
    
    public final static Texture doGetAlphaTexture(Terrain terrain, int alphaLayer) {
        if (terrain == null)
            return null;
        MatParam matParam = null;
        if (alphaLayer == 0)
            matParam = terrain.getMaterial().getParam("AlphaMap");
        else if(alphaLayer == 1)
            matParam = terrain.getMaterial().getParam("AlphaMap_1");
        else if(alphaLayer == 2)
            matParam = terrain.getMaterial().getParam("AlphaMap_2");
        
        if (matParam == null || matParam.getValue() == null) {
            return null;
        }
        Texture tex = (Texture) matParam.getValue();
        return tex;
    }
    
    public final static void doClearAlphaMap(Terrain terrain, int selectedTextureIndex) {
        int alphaIdx = selectedTextureIndex / 4; // 4 = rgba = 4 textures
        int texIndex = selectedTextureIndex - ((selectedTextureIndex/4)*4); // selectedTextureIndex/4 is an int floor
        //selectedTextureIndex - (alphaIdx * 4)
        Texture tex = doGetAlphaTexture(terrain, alphaIdx);
        Image image = tex.getImage();
        
        PaintTerrainToolAction paint = new PaintTerrainToolAction();
        
        ColorRGBA color = ColorRGBA.Black;
        for (int y=0; y<image.getHeight(); y++) {
            for (int x=0; x<image.getWidth(); x++) {
        
                paint.manipulatePixel(image, x, y, color, false); // gets the color at that location (false means don't write to the buffer)
                switch (texIndex) {
                    case 0:
                        color.r = 0; break;
                    case 1:
                        color.g = 0; break;
                    case 2:
                        color.b = 0; break;
                    case 3:
                        color.a = 0; break;
                }
                color.clamp();
                paint.manipulatePixel(image, x, y, color, true); // set the new color
            }
        }
        image.getData(0).rewind();
        tex.getImage().setUpdateNeeded();
    }
    
    /**
     * We are only using RGBA8 images for alpha textures right now.
     * @param image to get/set the color on
     * @param x location
     * @param y location
     * @param color color to get/set
     * @param write to write the color or not
     */
    public final static void manipulatePixel(Image image, int x, int y, ColorRGBA color, boolean write){
        ByteBuffer buf = image.getData(0);
        int width = image.getWidth();

        int position = (y * width + x) * 4;
        if ( position> buf.capacity()-1 || position<0 )
            return;
        
        if (write) {
            switch (image.getFormat()){
                case RGBA8:
                    buf.position( position );
                    buf.put(float2byte(color.r))
                       .put(float2byte(color.g))
                       .put(float2byte(color.b))
                       .put(float2byte(color.a));
                    return;
                 case ABGR8:
                    buf.position( position );
                    buf.put(float2byte(color.a))
                       .put(float2byte(color.b))
                       .put(float2byte(color.g))
                       .put(float2byte(color.r));
                    return;
                default:
                    throw new UnsupportedOperationException("Image format: "+image.getFormat());
            }
        } else {
            switch (image.getFormat()){
                case RGBA8:
                    buf.position( position );
                    color.set(byte2float(buf.get()), byte2float(buf.get()), byte2float(buf.get()), byte2float(buf.get()));
                    return;
                case ABGR8:
                    buf.position( position );
                    float a = byte2float(buf.get());
                    float b = byte2float(buf.get());
                    float g = byte2float(buf.get());
                    float r = byte2float(buf.get());
                    color.set(r,g,b,a);
                    return;
                default:
                    throw new UnsupportedOperationException("Image format: "+image.getFormat());
            }
        }
        
    }
    
    private static float byte2float(byte b){
        return ((float)(b & 0xFF)) / 255f;
    }

    private static byte float2byte(float f){
        return (byte) (f * 255f);
    }
    
    /**
     * 保存Alpha贴图到资源文件夹下。
     * @param terrain
     * @param assetFolder 资源文件夹的绝对路径,示例：C:\\home\\workspace\\jme3.1\\luoying\\ly-editor\\assets"
     */
    public final static synchronized void doSaveAlphaImages(Terrain terrain, String assetFolder) {
        Texture alpha1 = doGetAlphaTexture(terrain, 0);
        BufferedImage bi1 = ImageToAwt.convert(alpha1.getImage(), false, true, 0);
        File imageFile1 = new File(assetFolder+"/"+alpha1.getKey().getName());
        
        Texture alpha2 = doGetAlphaTexture(terrain, 1);
        BufferedImage bi2 = ImageToAwt.convert(alpha2.getImage(), false, true, 0);
        File imageFile2 = new File(assetFolder+"/"+alpha2.getKey().getName());
        
        Texture alpha3 = doGetAlphaTexture(terrain, 2);
        BufferedImage bi3 = ImageToAwt.convert(alpha3.getImage(), false, true, 0);
        File imageFile3 = new File(assetFolder+"/"+alpha3.getKey().getName());
        
        ImageOutputStream ios1 = null;
        ImageOutputStream ios2 = null;
        ImageOutputStream ios3 = null;
        try {
            ios1 = new FileImageOutputStream(imageFile1);
            ios2 = new FileImageOutputStream(imageFile2);
            ios3 = new FileImageOutputStream(imageFile3);
            ImageIO.write(bi1, "png", ios1);
            ImageIO.write(bi2, "png", imageFile2);
            ImageIO.write(bi3, "png", imageFile3);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Failed saving alphamaps, imageFile1={0}, imageFile2={1}, imageFile3={2}"
                    , new Object[] {imageFile1, imageFile2, imageFile3});
        } finally {
            try {
                if (ios1 != null)
                    ios1.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "", ex);
            }
            try {
                if (ios2 != null)
                    ios2.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "", ex);
            }
            try {
                if (ios3 != null)
                    ios3.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "", ex);
            }
        }
        
    }
    
    public final static Texture getDiffuseTexture(Terrain terrain, int layer) {
        if (terrain == null)
            return null;
        
        MatParam matParam;
        
        if (layer == 0)
            matParam = terrain.getMaterial().getParam("DiffuseMap");
        else
            matParam = terrain.getMaterial().getParam("DiffuseMap_"+ layer);
        
        if (matParam == null || matParam.getValue() == null) {
            return null;
        }
        Texture tex = (Texture) matParam.getValue();
        return tex;
    }
    
    public final static void setDiffuseTexture(Terrain terrain, int layer, Texture texture) {
        texture.setWrap(Texture.WrapMode.Repeat);
        if (layer == 0)
            terrain.getMaterial().setTexture("DiffuseMap", texture);
        else
            terrain.getMaterial().setTexture("DiffuseMap_" + layer, texture);
    }
    
    public final static void removeDiffuseTexture(Terrain terrain, int layer) {
        if (layer == 0)
            terrain.getMaterial().clearParam("DiffuseMap");
        else
            terrain.getMaterial().clearParam("DiffuseMap_" + layer);
    }
    
    /**
     * Get the scale of the texture at the specified layer.
     * @param terrain
     * @param layer
     * @return 
     */
    public final static Float getDiffuseTextureScale(Terrain terrain, int layer) {
        if (terrain == null)
            return 1f;
        
        MatParam matParam = terrain.getMaterial().getParam("DiffuseMap_" + layer +"_scale");
        if (matParam == null)
            return -1f;
        
        return (Float)matParam.getValue();
    }
    
    public final static void setDiffuseTextureScale(Terrain terrain, int layer, float scale) {
        terrain.getMaterial().setFloat("DiffuseMap_" + layer + "_scale", scale);
    }
    
    /**
     * 获取法线贴图，如果不存在则返回null.
     * @param terrain
     * @param layer
     * @return 
     */
    public final static Texture getNormalTexture(Terrain terrain, int layer) {
        MatParam matParam;
        if (layer == 0)
            matParam = terrain.getMaterial().getParam("NormalMap");
        else
            matParam = terrain.getMaterial().getParam("NormalMap_"+ layer);
        
        if (matParam == null || matParam.getValue() == null) {
            return null;
        }
        Texture tex = (Texture) matParam.getValue();
        return tex;
    }
    
    public final static void removeNormalTexture(Terrain terrain, int layer) {
        if (layer == 0)
            terrain.getMaterial().clearParam("NormalMap");
        else
            terrain.getMaterial().clearParam("NormalMap_"+layer);
    }
    
    /**
     * 设置法线贴图
     * @param terrain
     * @param layer
     * @param texture
     */
    public final static void setNormalTexture(Terrain terrain, int layer, Texture texture) {
        texture.setWrap(Texture.WrapMode.Repeat);
        if (layer == 0) {
            terrain.getMaterial().setTexture("NormalMap", texture);
        } else {
            terrain.getMaterial().setTexture("NormalMap_" + layer, texture);
        }
    }
    
    /**
     * 保存地形
     * @param terrain
     * @param saveFile 
     */
    public final static void saveTerrain(Spatial terrain, File saveFile) {
        try {
            BinaryExporter.getInstance().save(terrain, saveFile);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}
