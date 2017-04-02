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
//package test;
//
//import name.huliqing.editor.components.*;
//import com.jme3.app.Application;
//import com.jme3.asset.AssetManager;
//import com.jme3.asset.TextureKey;
//import com.jme3.bounding.BoundingBox;
//import com.jme3.export.binary.BinaryExporter;
//import com.jme3.material.Material;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import com.jme3.terrain.Terrain;
//import com.jme3.terrain.geomipmap.TerrainLodControl;
//import com.jme3.terrain.geomipmap.TerrainQuad;
//import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
//import com.jme3.texture.Texture;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
//import javax.imageio.ImageIO;
////import name.huliqing.editor.constants.AssetConstants;
//import name.huliqing.editor.constants.ResConstants;
//import name.huliqing.editor.edit.scene.JfxSceneEdit;
//import name.huliqing.editor.edit.terrain.JfxTerrainCreateForm;
//import name.huliqing.editor.manager.Manager;
//import name.huliqing.editor.ui.CustomDialog;
//import name.huliqing.fxswing.Jfx;
//import name.huliqing.luoying.constants.AssetConstants;
//import name.huliqing.luoying.data.EntityData;
//import name.huliqing.luoying.object.Loader;
//
///**
// *
// * @author huliqing
// */
//public class TerrainUtils extends EntityComponent {
//    public final static String MATERIAL_TERRAIN_LIGHTING = "Common/MatDefs/Terrain/TerrainLighting.j3md";
//    // texture settings
//    private static final float DEFAULT_TEXTURE_SCALE = 16.0625f;
//    private static final int NUM_ALPHA_TEXTURES = 3;
////    private final int MAX_DIFFUSE = 12;
////    private final int MAX_TEXTURES = 16 - NUM_ALPHA_TEXTURES; // 16 max (diffuse and normal), minus the ones we are reserving
//    
//    private final static String terrainModelDir = "/Models/terrains";
//    private final static String terrainTextureDir = "/Textures/terrains";
//    
//    public TerrainUtils(String id, String name) {
//        super(id, name);
//    }
//
////    @Override
////    public void create(JfxSceneEdit jfxEdit) {
////        CustomDialog dialog = new CustomDialog(jfxEdit.getEditRoot().getScene().getWindow());
////        JfxTerrainCreateForm form = new JfxTerrainCreateForm();
////        HBox.setHgrow(form, Priority.ALWAYS);
////        dialog.getChildren().add(form);
////        dialog.setTitle(Manager.getRes(ResConstants.COMMON_CREATE_TERRAIN));
////        dialog.show();
////
////        form.setOnOk(t -> {
////            try {
////                String terrainName = form.nameField.getText();
////                int totalSize = Integer.parseInt(form.totalSizeField.getText()) + 1;
////                int patchSize = Integer.parseInt(form.patchSizeField.getText()) + 1;
////                int alphaTextureSize = Integer.parseInt(form.alphaTextureSizeField.getText());
////                String assetFolder = Manager.getConfigManager().getMainAssetDir();
////                
////                // 创建地形
////                Spatial terrain = doCreateTerrain(jfxEdit.getEditor(), assetFolder, terrainName, totalSize, patchSize, alphaTextureSize, null);
////                
////                // 保存地形文件
////                String terrainFullName = terrainModelDir + "/" + terrainName + ".j3o";
////                File terrainFile = new File(assetFolder, terrainFullName);
////                BinaryExporter.getInstance().save(terrain, terrainFile);
////                
////                // 添加到3D场景编辑
////                Jfx.runOnJme(() -> {
////                    EntityData ed = Loader.loadData(id);
////                    ed.setAttribute("file", terrainFullName);
////                    Jfx.runOnJfx(() -> {
////                        jfxEdit.addEntity(ed);
////                    });
////                });
////                
////            } catch (IOException ex) {
////                Logger.getLogger(TerrainUtils.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            
////            dialog.hide();
////        });
////        form.setOnCancel(t -> {
////            dialog.hide();
////        });
////    }
//    
//    public static Spatial doCreateTerrain(Application app, String terrainName) {
//        try {
//            return doCreateTerrain(app, "C:\\home\\workspace\\jme3.1\\luoying\\ly-editor\\assets", terrainName
//                    , 128 + 1, 64 + 1, 128, null);
//        } catch (IOException ex) {
//            Logger.getLogger(TerrainUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//    
//    private static Spatial doCreateTerrain(Application app, String assetFolder, String terrainName
//            , int totalSize, int patchSize, int alphaTextureSize, float[] heightmapData) 
//            throws IOException {
//        AssetManager manager = app.getAssetManager();
//
//        File terrainModelFolder = new File(assetFolder + terrainModelDir);
//        if (!terrainModelFolder.exists()) {
//            terrainModelFolder.mkdirs();
//        }
//        File terrainTextureFolder = new File(assetFolder + terrainTextureDir);
//        if (!terrainTextureFolder.exists()) {
//            terrainTextureFolder.mkdirs();
//        }
//        
//        Terrain terrain = new TerrainQuad(terrainName, patchSize, totalSize, heightmapData); 
//        Material mat = new Material(manager, MATERIAL_TERRAIN_LIGHTING);
//        // write out 3 alpha blend images
//        for (int i = 0; i < NUM_ALPHA_TEXTURES; i++) {
//            BufferedImage alphaBlend = new BufferedImage(alphaTextureSize, alphaTextureSize, BufferedImage.TYPE_INT_ARGB);
//            if (i == 0) {
//                // the first alpha level should be opaque so we see the first texture over the whole terrain
//                for (int h = 0; h < alphaTextureSize; h++)
//                    for (int w = 0; w < alphaTextureSize; w++)
//                        alphaBlend.setRGB(w, h, 0x00FF0000);//argb
//            }
//            
//            String alphaBlendFileName = terrainTextureDir + "/" + terrainName + "-alphablend" + i + ".png";
//            ImageIO.write(alphaBlend, "png", new File(assetFolder + alphaBlendFileName));
//            Texture tex = manager.loadAsset(new TextureKey(alphaBlendFileName, false));
//            switch (i) {
//                case 0:
//                    mat.setTexture("AlphaMap", tex);
//                    break;
//                case 1:
//                    mat.setTexture("AlphaMap_1", tex);
//                    break;
//                case 2:
//                    mat.setTexture("AlphaMap_2", tex);
//                    break;
//                default:
//                    break;
//            }
//        }
//        
//        // remove20170202
////        // copy the default texture to the assets folder if it doesn't exist there yet
////        String dirtTextureName = "/Textures/dirt.jpg";
////        File dirtTextureFile = new File(assetFolder + dirtTextureName);
////        if (!dirtTextureFile.exists()) {
////            BufferedImage bi = ImageToAwt.convert(manager.loadTexture(AssetConstants.TEXTURES_TERRAIN_DIRT).getImage(), false, true, 0);
////            ImageIO.write(bi, "jpg", dirtTextureFile);
////        }
//
//        String dirtTextureName = AssetConstants.TEXTURES_TERRAIN_DIRT;
//        
//        
//        // give the first layer default texture
//        Texture dirtTexture = manager.loadTexture(dirtTextureName);
//        dirtTexture.setWrap(Texture.WrapMode.Repeat);
//        mat.setTexture("DiffuseMap", dirtTexture);
//        mat.setFloat("DiffuseMap_0_scale", DEFAULT_TEXTURE_SCALE);
//        mat.setBoolean("WardIso", true);
//        mat.setFloat("Shininess", 0.01f);
//
//        ((Node)terrain).setMaterial(mat);
//        ((Node)terrain).setModelBound(new BoundingBox());
//        ((Node)terrain).updateModelBound();
//        ((Node)terrain).setLocalTranslation(0, 0, 0);
//        ((Node)terrain).setLocalScale(1f, 1f, 1f);
//
//        // add the lod control
//        TerrainLodControl control = new TerrainLodControl(terrain, app.getCamera());
//        control.setLodCalculator(new DistanceLodCalculator(patchSize, 2.7f));
//	((Node)terrain).addControl(control);
//
//        //setNeedsSave(true);
//        //addSpatialUndo(parent, (Node)terrain, jmeNodeParent);
//        
//        return (Spatial) terrain;
//    }
//}
