/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.jme3.app.SimpleApplication;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.gde.terraineditor.tools.PaintTerrainToolAction;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import com.jme3.texture.Texture;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.utils.TerrainUtils;
import name.huliqing.luoying.constants.AssetConstants;

/**
 *
 * @author huliqing
 */
public class PaintTest extends SimpleApplication {
    private final float DEFAULT_TEXTURE_SCALE = 16.0625f;
    
    private String assetDir = "C:\\home\\workspace\\jme3.1\\luoying\\ly-editor\\assets";
    private final static String terrainModelDir = "/Models/terrains";
    private final static String terrainTextureDir = "/Textures/terrains";
    private String terrainName = "terrainTest";
    
    private Spatial terrainSpatial;
    private Terrain terrain;
    private Material mat;
    
    public static void main(String[] args) {
        PaintTest app = new PaintTest();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(100);
        this.cam.setLocation(new Vector3f(0, 50, 30));
        this.cam.lookAt(new Vector3f(), Vector3f.UNIT_Y);
        
        try {
//            Terrain temp = TerrainUtils.doCreateTerrain(this, assetDir, terrainTextureDir, terrainName, 256, 64, 256, null, AssetConstants.TEXTURES_TERRAIN_DIRT);
//            TerrainUtils.doSaveAlphaImages(temp, assetDir);
//            doSaveTerrain(temp);
//            terrain = (Terrain) assetManager.loadModel(terrainModelDir + "/" + terrainName + ".j3o");
            
//            terrain = TerrainUtils.doCreateTerrain(this, assetDir, terrainTextureDir, terrainName, 256, 64, 256, null, AssetConstants.TEXTURES_TERRAIN_DIRT);
            terrain = (Terrain) assetManager.loadModel(terrainModelDir + "/" + terrainName + ".j3o");
        } catch (Exception ex) {
            Logger.getLogger(PaintTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        terrainSpatial = (Spatial) terrain;
        rootNode.attachChild(terrainSpatial);
        
        rootNode.addLight(new AmbientLight());
        mat = terrain.getMaterial();
        
//        addTextureLayer(1);
        
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) return;
                PaintTerrainToolAction action = new PaintTerrainToolAction();
                action.paintTexture(terrain, new Vector3f(FastMath.nextRandomFloat() * 100.0f, 0, FastMath.nextRandomFloat() * 100.0f)
                        , 3, 3, 1);
            }
        }, "test");
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) return;
                TerrainUtils.doSaveAlphaImages(terrain, assetDir);
                doSaveTerrain(terrain);
            }
        }, "save");
        
        inputManager.addMapping("test", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("save", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        
    }
    
    private void doSaveTerrain(Terrain terrain) {
        // 保存地形文件
        String terrainFullName = assetDir + terrainModelDir + "/" + terrainName + ".j3o";
        File terrainFile = new File(terrainFullName);
        try {
            BinaryExporter.getInstance().save((Spatial)terrain, terrainFile);
        } catch (IOException ex) {
            Logger.getLogger(PaintTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addTextureLayer(int layer) {
        float scale = DEFAULT_TEXTURE_SCALE;
        setDiffuseTextureScale(layer, scale);
        setDiffuseTexture(layer, "Textures/grass.jpg");
    }
    
    public void setDiffuseTextureScale(int layer, float scale) {
        terrain.getMaterial().setFloat("DiffuseMap_" + layer + "_scale", scale);
    }
    
    public void setDiffuseTexture(int layer, String texturePath) {
        Texture texture = getAssetManager().loadTexture(texturePath);
        texture.setWrap(Texture.WrapMode.Repeat);
        terrain.getMaterial().setTexture("DiffuseMap_" + layer, texture);
    }
    
}
