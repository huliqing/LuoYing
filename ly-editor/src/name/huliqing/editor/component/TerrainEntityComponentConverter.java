/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.component;

import com.jme3.app.Application;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.edit.terrain.BasePanel;
import name.huliqing.editor.edit.terrain.TerrainCreateForm;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.CustomDialog;
import name.huliqing.editor.utils.TerrainUtils;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.constants.AssetConstants;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;

/**
 * 地形组件的转换器
 * @author huliqing
 */
public class TerrainEntityComponentConverter extends EntityComponentConverter {
//    public final static String MATERIAL_TERRAIN_LIGHTING = "Common/MatDefs/Terrain/TerrainLighting.j3md";
    
    private final String modelDir = "/Models/terrains";
    private final String alphaTextureDir = "/Textures/terrains";
    
    @Override
    public void create(ComponentDefine cd, JfxSceneEdit jfxEdit) {
        CustomDialog dialog = new CustomDialog(jfxEdit.getEditRoot().getScene().getWindow());
        TerrainCreateForm form = new TerrainCreateForm(jfxEdit.getEditor().getAssetManager());
        BasePanel baseForm = form.basePanel;
        dialog.getChildren().add(form);
        dialog.setTitle(Manager.getRes(ResConstants.COMMON_CREATE_TERRAIN));
        dialog.showOnCenter();

        form.setOnOk(t -> {
            dialog.hide();
            String terrainName = baseForm.nameField.getText();
            int totalSize = Integer.parseInt(baseForm.totalSizeField.getText());
            int patchSize = Integer.parseInt(baseForm.patchSizeField.getText());
            int alphaTextureSize = Integer.parseInt(baseForm.alphaTextureSizeField.getText());
            String assetFolder = Manager.getConfigManager().getMainAssetDir();
            Jfx.runOnJme(() -> {
                float[] heightMapData = null;
                if (form.flatPanel.isVisible()) {
                    heightMapData = null;
                    
                } else if (form.hillPanel.isVisible()) {
                    heightMapData = form.hillPanel.getHeightMap();
                    
                } else if (form.imageBasedPanel.isVisible()) {
                    heightMapData = form.imageBasedPanel.getHeightMap();
                }
                createTerrain(cd, jfxEdit, jfxEdit.getEditor(), terrainName, totalSize, patchSize, alphaTextureSize, assetFolder, heightMapData);
                
            });
        });
        form.setOnCancel(t -> {
            dialog.hide();
        });
    }
    
    private void createTerrain(ComponentDefine cd, JfxSceneEdit jfxEdit, Application application
            , String terrainName, int totalSize, int patchSize, int alphaTextureSize, String assetFolder, float[] heightMap) {
        try {
            // 创建地形
            Terrain terrain = TerrainUtils.doCreateTerrain(application, assetFolder
                    , alphaTextureDir, terrainName, totalSize, patchSize, alphaTextureSize, heightMap, AssetConstants.TEXTURES_TERRAIN_DIRT);
            Spatial terrainSpatial = (Spatial) terrain;

            // 保存地形文件
            String terrainFullName = modelDir + "/" + terrainName + ".j3o";
            File terrainFile = new File(assetFolder, terrainFullName);
            BinaryExporter.getInstance().save(terrainSpatial, terrainFile);

            // 添加到3D场景编辑
            EntityData ed = Loader.loadData(cd.getId());
            ed.setAttribute("file", terrainFullName.substring(1)); // 去掉"/"
            Jfx.runOnJfx(() -> {
                jfxEdit.addEntity(ed);
            });

        } catch (IOException ex) {
            Logger.getLogger(TerrainEntityComponentConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
