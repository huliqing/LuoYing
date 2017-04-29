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
package name.huliqing.editor.component;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.heightmap.HillHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import name.huliqing.editor.Editor;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.scene.JfxSceneEdit;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.CustomDialog;
import name.huliqing.editor.utils.TerrainUtils;
import static name.huliqing.editor.utils.TerrainUtils.TERRAIN_DIRT;
import name.huliqing.fxswing.Jfx;
import name.huliqing.editor.UncacheAssetEventListener;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;

/**
 * 地形组件的转换器
 * @author huliqing
 */
public class TerrainEntityComponentConverter extends EntityComponentConverter {

    private static final Logger LOG = Logger.getLogger(TerrainEntityComponentConverter.class.getName());
    
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
                    , alphaTextureDir, terrainName, totalSize, patchSize, alphaTextureSize, heightMap, TERRAIN_DIRT);
            Spatial terrainSpatial = (Spatial) terrain;
            
            // 保存地形文件
            String terrainFullName = modelDir.substring(1) + "/" + terrainName + ".j3o";
            File terrainFile = new File(assetFolder, terrainFullName);
            BinaryExporter.getInstance().save(terrainSpatial, terrainFile);
            
            UncacheAssetEventListener.getInstance().addUncache(terrainFullName);
            
            // 添加到3D场景编辑
            EntityData ed = Loader.loadData(cd.getId());
            ed.setAttribute("file", terrainFullName); // 去掉"/"
            Jfx.runOnJfx(() -> {
                jfxEdit.addEntityUseUndoRedo(ed);
            });

        } catch (IOException ex) {
            Logger.getLogger(TerrainEntityComponentConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // ---- 
    
    private class TerrainCreateForm extends VBox {

        private final ToggleGroup tg = new ToggleGroup();
        private final GridPane tgPanel = new GridPane();
        private final RadioButton flat = new RadioButton(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_FLAT));
        private final RadioButton hill = new RadioButton(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL));
        private final RadioButton imageBased = new RadioButton(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_IMAGE));

        public final BasePanel basePanel = new BasePanel();
        public final FlatPanel flatPanel = new FlatPanel();
        public final HillPanel hillPanel = new HillPanel(basePanel);
        public ImageBasedPanel imageBasedPanel;

        private final GridPane btnPane = new GridPane();
        private final Button ok = new Button(Manager.getRes(ResConstants.COMMON_OK));
        private final Button cancel = new Button(Manager.getRes(ResConstants.COMMON_CANCEL));

        public TerrainCreateForm(AssetManager am) {
            imageBasedPanel = new ImageBasedPanel(am);

            tg.getToggles().addAll(flat, hill, imageBased);
            tgPanel.addRow(0, new Label(), flat, hill, imageBased); // new Label用于占位
            btnPane.addRow(0, new Label(), ok, cancel);
            getChildren().addAll(basePanel, tgPanel, flatPanel, hillPanel, imageBasedPanel, btnPane);

            flat.setTooltip(new Tooltip(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_FLAT_TIP)));
            hill.setTooltip(new Tooltip(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL_TIP)));
            imageBased.setTooltip(new Tooltip(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_IMAGE_TIP)));

            tgPanel.getColumnConstraints().add(new ColumnConstraints(100));
            tgPanel.setHgap(10);
            tgPanel.setPadding(new Insets(0, 0, 10, 0));

            flatPanel.managedProperty().bind(flatPanel.visibleProperty());
            hillPanel.managedProperty().bind(hillPanel.visibleProperty());
            imageBasedPanel.managedProperty().bind(imageBasedPanel.visibleProperty());

            ok.setMinWidth(80);
            cancel.setMinWidth(80);
            btnPane.getColumnConstraints().addAll(new ColumnConstraints(110), new ColumnConstraints(100), new ColumnConstraints(100));
            btnPane.setPadding(new Insets(10, 0, 10, 0));

            setMinWidth(550);
            setMinHeight(320);

            tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                    flatPanel.setVisible(false);
                    hillPanel.setVisible(false);
                    imageBasedPanel.setVisible(false);
                    if (newValue == flat) {
                        flatPanel.setVisible(true);
                    } else if (newValue == hill) {
                        hillPanel.setVisible(true);
                    } else if (newValue == imageBased) {
                        imageBasedPanel.setVisible(true);
                    }
                }
            });

            tg.selectToggle(flat);

        }

        public void setOnOk(EventHandler<ActionEvent> e) {
            ok.setOnAction(e);
        }

        public void setOnCancel(EventHandler<ActionEvent> e) {
            cancel.setOnAction(e);
        }
    }
    
    private class ImageBasedPanel extends GridPane {

        public final Label imageLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_IMAGE_FILE));
        public final HBox fileZone = new HBox();
        public final TextField imageField = new TextField();
        public final Button fileButton = new Button("");

        public final Label roughLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_IMAGE_SMOOTH));
        public final Slider roughField = new Slider(0, 1, 0.2f);

        public final Label scaleLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_IMAGE_SCALE));
        public final TextField scaleField = new TextField("1.0");

        private final AssetManager am;

        public ImageBasedPanel(AssetManager am) {
            this.am = am;

            fileZone.getChildren().addAll(imageField, fileButton);
            addRow(0, imageLabel, fileZone);
            addRow(1, roughLabel, roughField);
            addRow(2, scaleLabel, scaleField);

            HBox.setHgrow(imageField, Priority.ALWAYS);
            getColumnConstraints().add(new ColumnConstraints(110));
            GridPane.setHalignment(imageLabel, HPos.RIGHT);
            GridPane.setHalignment(roughLabel, HPos.RIGHT);
            GridPane.setHalignment(scaleLabel, HPos.RIGHT);
            GridPane.setHgrow(fileZone, Priority.ALWAYS);
            GridPane.setHgrow(roughField, Priority.ALWAYS);
            GridPane.setHgrow(scaleField, Priority.ALWAYS);
            setVgap(10);
            setPadding(new Insets(0, 10, 0, 0));

            Image image = new Image(getClass().getResourceAsStream("/" + AssetConstants.INTERFACE_COMMON_IMAGE));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(18);
            imageView.setFitHeight(18);
            fileButton.setGraphic(imageView);

            fileButton.setOnAction(e -> {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png", "*.bmp", "*.jpeg"));
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
                fc.setInitialDirectory(new File(Manager.getConfigManager().getMainAssetDir() + "/Textures"));
                File file = fc.showOpenDialog(this.getScene().getWindow());
                if (file != null) {
                    imageField.setText(file.getAbsolutePath());
                }
            });

            roughField.setMajorTickUnit(0.1);
            roughField.setShowTickMarks(true);
        }

        public float[] getHeightMap() {
            if (imageField.getText().isEmpty()) {
                return null;
            }
            String imagePath = Editor.toAssetFilePath(imageField.getText());
            Texture tex = am.loadTexture(imagePath);
            ImageBasedHeightMap heightMap = new ImageBasedHeightMap(tex.getImage(), new Float(scaleField.getText()));
            heightMap.load();
            heightMap.smooth(new Float(roughField.getValue()), 2);
            return heightMap.getHeightMap();
        }
    }
    
    private class HillPanel extends GridPane {
        private final BasePanel basePanel;
        public final Label iterationsLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL_ITERATIONS));
        public final TextField iterationsField = new TextField("2000");
        public final Label seedLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL_SEED));
        public final TextField flatteningField = new TextField("4");
        public final Label minRadiusLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL_RADIUS_MIN));
        public final TextField minRadiusField = new TextField("20");
        public final Label maxRadiusLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL_RADIUS_MAX));
        public final TextField maxRadiusField = new TextField("50");

        public HillPanel(BasePanel basePanel) {
            this.basePanel = basePanel;
            addRow(0, iterationsLabel, iterationsField, seedLabel, flatteningField);
            addRow(1, minRadiusLabel, minRadiusField, maxRadiusLabel, maxRadiusField);
            getColumnConstraints().add(new ColumnConstraints(110));
            GridPane.setHalignment(iterationsLabel, HPos.RIGHT);
            GridPane.setHalignment(seedLabel, HPos.RIGHT);
            GridPane.setHalignment(minRadiusLabel, HPos.RIGHT);
            GridPane.setHalignment(maxRadiusLabel, HPos.RIGHT);
            GridPane.setHgrow(seedLabel, Priority.ALWAYS);
            GridPane.setHgrow(maxRadiusLabel, Priority.ALWAYS);
            setPadding(new Insets(0, 10, 0, 0));
            setVgap(10);
        }

        public float[] getHeightMap() {
            try {
                int terrainTotalSize = new Integer(basePanel.totalSizeField.getText());
                int iterations = new Integer(iterationsField.getText());
                long seed = new Long(flatteningField.getText());
                float min = new Float(minRadiusField.getText());
                float max = new Float(maxRadiusField.getText());
                HillHeightMap heightMap = new HillHeightMap(terrainTotalSize, iterations, min, max, seed);
                return heightMap.getHeightMap();
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "", ex);
            }
            return null;
        }
    }
  
    private class FlatPanel extends VBox {}
    
    private class BasePanel extends VBox {
        private final GridPane grid = new GridPane();
        private final Label nameLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TERRAIN_NAME));
        private final Label totalSizeLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TOTAL_SIZE));
        private final Label patchSizeLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_PATCH_SIZE));
        // The size of one side of the texture, eg. 512. This will be used to blend several textures together into one texture for
        // the terrain. The larger the size, the more detailed the image. But it will use more memory and possibly slow 
        // down the editor. You cannot change this value after it is set!
        private final Label alphaTextureSizeLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_ALPHA_SIZE));

        public final TextField nameField = new TextField();
        public final TextField totalSizeField = new TextField("512");
        public final TextField patchSizeField = new TextField("64");
        public final TextField alphaTextureSizeField = new TextField("512");

        public BasePanel() {
            getChildren().addAll(grid);

            grid.addRow(0, nameLabel, nameField);
            grid.addRow(1, totalSizeLabel, totalSizeField);
            grid.addRow(2, patchSizeLabel, patchSizeField);
            grid.addRow(3, alphaTextureSizeLabel, alphaTextureSizeField);

            GridPane.setHgrow(totalSizeField, Priority.ALWAYS);
            GridPane.setHgrow(patchSizeField, Priority.ALWAYS);

            nameField.setPromptText(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TERRAIN_NAME_TIP));
            nameField.setTooltip(new Tooltip(nameField.getPromptText()));
            totalSizeField.setPromptText(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TOTAL_SIZE_TIP));
            totalSizeField.setTooltip(new Tooltip(totalSizeField.getPromptText()));
            patchSizeField.setPromptText(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_PATCH_SIZE_TIP));
            patchSizeField.setTooltip(new Tooltip(patchSizeField.getPromptText()));
            alphaTextureSizeField.setPromptText(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_ALPHA_SIZE_TIP));
            alphaTextureSizeField.setTooltip(new Tooltip(alphaTextureSizeField.getPromptText()));

            grid.setVgap(10);
            GridPane.setHalignment(nameLabel, HPos.RIGHT);
            GridPane.setHalignment(totalSizeLabel, HPos.RIGHT);
            GridPane.setHalignment(patchSizeLabel, HPos.RIGHT);
            GridPane.setHalignment(alphaTextureSizeLabel, HPos.RIGHT);
            setPadding(new Insets(10));
            setMinWidth(400);
            ColumnConstraints cc = new ColumnConstraints(100);
            grid.getColumnConstraints().add(cc);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            nameField.setText("terrain" + sdf.format(new Date()));
            // for debug
//        setStyle("-fx-border: solid inside;-fx-border-width:1;-fx-border-color:red;");
        }
    }
}
