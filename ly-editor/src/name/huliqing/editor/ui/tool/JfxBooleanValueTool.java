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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxBooleanValueTool extends JfxAbstractTool<BooleanValueTool> implements ValueChangedListener<Boolean>{

    private final HBox layout = new HBox();
    private final Label label = new Label();
    private final CheckBox checkBox = new CheckBox();
    
    private boolean ignoreViewUpdate;
    
    public JfxBooleanValueTool() {
        layout.getChildren().addAll(label, checkBox);
        checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            Jfx.runOnJme(() -> {
                ignoreViewUpdate = true;
                tool.setValue(newValue);
                ignoreViewUpdate = false;
            });
        });
        
        label.setPrefWidth(100);
        checkBox.setPrefWidth(64);
    }
    
    @Override
    public Region createView() {
        return layout;
    }

    @Override
    public void onValueChanged(ValueTool<Boolean> vt, Boolean oldValue, Boolean newValue) {
        if (ignoreViewUpdate) 
            return;
        Jfx.runOnJfx(() -> {
            checkBox.setSelected(newValue);
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        label.setText(tool.getName());
        checkBox.setSelected(tool.getValue());
    }
    
}
