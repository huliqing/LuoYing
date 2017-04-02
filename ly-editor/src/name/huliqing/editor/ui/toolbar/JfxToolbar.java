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
package name.huliqing.editor.ui.toolbar;

import javafx.scene.layout.Region;
import name.huliqing.editor.toolbar.Toolbar;

/**
 * JFX工具栏，用于渲染JME工具栏
 * @author huliqing
 */
public interface JfxToolbar {

    void initialize();
    
    boolean isInitialized();
    
    void cleanup();
    
    /**
     * 设置jme toolbar
     * @param toolbar 
     */
    void setToolbar(Toolbar toolbar);
    
    /**
     * 获取工具栏名称
     * @return 
     */
    String getName();
    
    /**
     * 获取工具栏的面板
     * @return 
     */
    Region getView();
    
}
