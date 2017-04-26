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
package name.huliqing.editor.ui.tool;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.tools.batch.BatchEntityGenTool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxBatchEntityGenTool extends JfxAbstractTool<BatchEntityGenTool> {

    private static final Logger LOG = Logger.getLogger(JfxBatchEntityGenTool.class.getName());
    
    private final VBox view = new VBox();
    
    public JfxBatchEntityGenTool() {
        HBox labelBox = new HBox();
        Label label = new Label("ShowDebug");
        CheckBox cb = new CheckBox();
        cb.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
            Jfx.runOnJme(() -> {tool.setGridVisible(newValue);});
        });
        labelBox.getChildren().addAll(label,cb);
        
        // 为BatchEntity指定一个基本名称
        HBox nameGroup = new HBox();
        Label nameLabel = new Label("Enter a base name");
        TextField nameField = new TextField("");
        nameLabel.setPrefWidth(100);
        nameField.setPrefWidth(64);
        nameGroup.getChildren().addAll(nameLabel, nameField);
        
        Button genButton = new Button("Gen BatchEntity");
        genButton.setOnAction((ActionEvent t) -> {
            Jfx.runOnJme(() -> {
                if (nameField.getText() == null || nameField.getText().isEmpty()) {
                    LOG.log(Level.WARNING, "BatchEntity name could not be null!");
                    return;
                }
                tool.generateBatchEntities(nameField.getText());
            });
        });
        
        view.getChildren().addAll(labelBox, nameGroup, genButton);
    }
    
    @Override
    protected Region createView() {
        return view;
    }
    
}
