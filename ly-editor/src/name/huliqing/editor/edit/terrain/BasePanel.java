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

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;

/**
 *
 * @author huliqing
 */
public class BasePanel extends VBox {
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
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        nameField.setText("terrain" + sdf.format(new Date()));
        // for debug
//        setStyle("-fx-border: solid inside;-fx-border-width:1;-fx-border-color:red;");
    }
    

}
