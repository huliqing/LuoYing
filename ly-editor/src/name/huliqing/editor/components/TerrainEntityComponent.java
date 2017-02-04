/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.components;

import com.jme3.app.Application;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.edit.terrain.JfxTerrainCreateForm;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.CustomDialog;
import name.huliqing.editor.utils.TerrainUtils;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.constants.AssetConstants;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;

/**
 *
 * @author huliqing
 */
public class TerrainEntityComponent extends EntityComponent {
    public final static String MATERIAL_TERRAIN_LIGHTING = "Common/MatDefs/Terrain/TerrainLighting.j3md";
    
    private final String modelDir = "/Models/terrains";
    private final String alphaTextureDir = "/Textures/terrains";
    
    public TerrainEntityComponent(String id, String name) {
        super(id, name);
    }

    @Override
    public void create(JfxSceneEdit jfxEdit) {
        CustomDialog dialog = new CustomDialog(jfxEdit.getEditRoot().getScene().getWindow());
        JfxTerrainCreateForm form = new JfxTerrainCreateForm();
        HBox.setHgrow(form, Priority.ALWAYS);
        dialog.getChildren().add(form);
        dialog.setTitle(Manager.getRes(ResConstants.COMMON_CREATE_TERRAIN));
        dialog.show();

        form.setOnOk(t -> {
            dialog.hide();
            String terrainName = form.nameField.getText();
            int totalSize = Integer.parseInt(form.totalSizeField.getText());
            int patchSize = Integer.parseInt(form.patchSizeField.getText());
            int alphaTextureSize = Integer.parseInt(form.alphaTextureSizeField.getText());
            String assetFolder = Manager.getConfigManager().getMainAssetDir();
            Jfx.runOnJme(() -> {
                createTerrain(jfxEdit, jfxEdit.getEditor(), terrainName, totalSize, patchSize, alphaTextureSize, assetFolder);
            });
        });
        form.setOnCancel(t -> {
            dialog.hide();
        });
    }
    
    private void createTerrain(JfxSceneEdit jfxEdit, Application application
            , String terrainName, int totalSize, int patchSize, int alphaTextureSize, String assetFolder) {
        try {
            // 创建地形
            Terrain terrain = TerrainUtils.doCreateTerrain(application, assetFolder
                    , alphaTextureDir, terrainName, totalSize, patchSize, alphaTextureSize, null, AssetConstants.TEXTURES_TERRAIN_DIRT);
            Spatial terrainSpatial = (Spatial) terrain;

            // 保存地形文件
            String terrainFullName = modelDir + "/" + terrainName + ".j3o";
            File terrainFile = new File(assetFolder, terrainFullName);
            BinaryExporter.getInstance().save(terrainSpatial, terrainFile);

            // 添加到3D场景编辑
            EntityData ed = Loader.loadData(id);
            ed.setAttribute("file", terrainFullName.substring(1)); // 去掉"/"
            Jfx.runOnJfx(() -> {
                jfxEdit.addEntity(ed);
            });

        } catch (IOException ex) {
            Logger.getLogger(TerrainEntityComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
