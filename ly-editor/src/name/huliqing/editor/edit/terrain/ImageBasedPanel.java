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
package name.huliqing.editor.edit.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import java.io.File;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import name.huliqing.editor.Editor;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;

/**
 *
 * @author huliqing
 */
public class ImageBasedPanel extends GridPane {
    
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
            fc.getExtensionFilters().addAll(new ExtensionFilter("Image", "*.jpg", "*.png", "*.bmp", "*.jpeg"));
            fc.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
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
        if (imageField.getText().isEmpty())
            return null;
        String imagePath = Editor.toAssetFilePath(imageField.getText());
        Texture tex = am.loadTexture(imagePath);
        ImageBasedHeightMap heightMap = new ImageBasedHeightMap(tex.getImage(), new Float(scaleField.getText()));
        heightMap.load();
        heightMap.smooth(new Float(roughField.getValue()), 2);
        return heightMap.getHeightMap();
    }
}
