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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.tools.base.ModeTool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxModeTool extends JfxAbstractTool implements ModeTool.ModeChangedListener {
    
    private final ChoiceBox<Mode> view = new ChoiceBox<Mode>();
    private ModeTool modeTool;
    private boolean ignoreEvent;
    
    public JfxModeTool() {
        view.setPrefWidth(80);
        view.setPrefHeight(25);
        view.setMaxHeight(25);
    }
    
    @Override
    public Region createView() {
        return view;
    }

    @Override
    public void onModeChanged(Mode newMode) {
        if (newMode == view.getValue())
            return;
        Jfx.runOnJfx(() -> {
            ignoreEvent = true;
            view.setValue(newMode);
            ignoreEvent = false;
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        modeTool = (ModeTool) tool;
        modeTool.addModeChangedListener(this);
        view.getItems().clear();
        view.getItems().addAll(Mode.values());
        view.setValue(modeTool.getMode());
        view.valueProperty().addListener((ObservableValue<? extends Mode> observable, Mode oldValue, Mode newValue) -> {
            if (ignoreEvent)
                return;
            Jfx.runOnJme(() -> {
                modeTool.setMode(view.getValue());
            });
        });
        
        // tooltip
        if (tool.getTips() != null) {
            view.setTooltip(new Tooltip(tool.getTips()));
        }
    }
    
}
