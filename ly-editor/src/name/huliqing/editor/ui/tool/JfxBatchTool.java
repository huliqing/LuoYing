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

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.tools.batch.BatchTool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxBatchTool extends JfxAbstractTool<BatchTool> {

    private final VBox view = new VBox();
    
    private Button button = new Button("Batch");
    
    public JfxBatchTool() {
//        CheckBox cb = new CheckBox();
//        cb.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
//            Jfx.runOnJme(() -> {tool.setGridVisible(newValue);});
//        });
        
//        labelBox.getChildren().addAll(label,cb);
        view.getChildren().add(button);
    }
    
    @Override
    protected Region createView() {
        return view;
    }
    
}
