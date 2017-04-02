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

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.Tool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class JfxAbstractTool<T extends Tool> implements JfxTool<T> {

    protected Toolbar toolbar;
    protected T tool;
    protected boolean enabled = true;
    protected boolean initialized;
    
    protected final VBox root = new VBox();
    
    public JfxAbstractTool() {
        root.managedProperty().bind(root.visibleProperty());
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public Tool getTool() {
        return tool;
    }

    @Override
    public void setTool(T tool) {
        this.tool = tool;
    }

    @Override
    public final Region getView() {
        return root;
    }

    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException();
        }
        Region region = createView();
        region.prefWidthProperty().bind(root.widthProperty());
        region.prefHeightProperty().bind(root.heightProperty());
        root.getChildren().add(region);
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        root.getChildren().clear();
        initialized = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        boolean changed = this.enabled != enabled;
        this.enabled = enabled;
        if (changed) {
            setViewEnabled(enabled);
            Jfx.runOnJme(() -> toolbar.setEnabled(tool, enabled));
        }
    }

    /**
     * 打开UI、关闭UI,子类根据实际情况可以使用view隐藏或者关闭。
     * @param enabled 
     */
    protected void setViewEnabled(boolean enabled) {
        root.setDisable(!enabled);
    }
    
    /**
     * 创建工具的组件
     * @return 
     */
    protected abstract Region createView();
}
