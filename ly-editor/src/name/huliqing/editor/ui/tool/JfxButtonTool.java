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

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import name.huliqing.editor.tools.ButtonTool;
import name.huliqing.fxswing.Jfx;

/**
 * 按钮类型的工具
 * @author huliqing
 */
public class JfxButtonTool extends JfxAbstractTool<ButtonTool> {

    private final Button view = new Button();
    
    public JfxButtonTool() {
        view.setPrefWidth(80);
        view.setPrefHeight(25);
        view.setMaxHeight(25);
        view.setOnAction(e -> {
            Jfx.runOnJme(() -> {tool.doAction();});
        });
    }
    
    @Override
    public Region createView() {
        return view;
    }

    @Override
    protected void setViewEnabled(boolean enabled) {
        view.setDisable(!enabled);
    }

    @Override
    public void initialize() {
        super.initialize();
        view.setText(tool.getName());
        // tooltip
        if (tool.getTips() != null) {
            view.setTooltip(new Tooltip(tool.getTips()));
        }
        
        // icon
        if (tool.getIcon() != null) {
            Image image = new Image(getClass().getResourceAsStream("/" + tool.getIcon()));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(14);
            imageView.setFitHeight(14);
            view.setGraphic(imageView);
        }
        
        if (tool.isInitialized()) {
            setViewEnabled(true);
        }
    }
    
}
