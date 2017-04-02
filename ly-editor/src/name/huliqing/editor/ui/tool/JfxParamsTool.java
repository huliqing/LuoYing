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

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.tools.ParamsTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxParamsTool extends JfxAbstractTool<ParamsTool> {

    private final VBox layout = new VBox();
    
    public JfxParamsTool() {
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        layout.setSpacing(5);
    }
    
    @Override
    public Region createView() {
        return layout;
    }

    @Override
    protected void setViewEnabled(boolean enabled) {
        root.setVisible(enabled);
    }

    @Override
    public void initialize() {
        super.initialize();
        Jfx.runOnJme(() -> {
            List<Tool> children = new ArrayList(tool.getChildren());
            Jfx.runOnJfx(() -> {
                for (int i = 0; i < children.size(); i++) {
                    Tool child = children.get(i);
                    JfxTool jt = JfxToolFactory.createJfxTool(child, toolbar);
                    if (jt != null) {
                        jt.initialize();
                        Region toolView = jt.getView();
                        toolView.prefWidthProperty().bind(layout.widthProperty());
                        layout.getChildren().add(toolView);
                    }
                }
            });
        });
    }
    
}
