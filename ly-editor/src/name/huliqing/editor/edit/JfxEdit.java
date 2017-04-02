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
package name.huliqing.editor.edit;

import javafx.scene.layout.Pane;
import name.huliqing.editor.Editor;

/**
 * 编辑器的UI界面接口, 主要作为编辑器的UI编辑界面，包含各种编辑工具等。
 * @author huliqing
 * @param <T>
 */
public interface JfxEdit<T extends JmeEdit> {
    
    /**
     * 初始化
     * @param editor 
     */
    void initialize(Editor editor);
    
    /**
     * 更新逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理编辑器
     */
    void cleanup();
    
    /**
     * 回退历史记录
     */
    void undo();
    
    /**
     * 重做历史记录
     */
    void redo();
    
    /**
     * 添加一个编辑操作的历史记录
     * @param ur 
     */
    void addUndoRedo(UndoRedo ur);
    
    /**
     * 判断当前编辑器是否已经经过修改，一般这表示编辑器在退出的时候需要进行保存操作。
     * 可调用{@link #save() }来保存操作
     * @return 
     */
    boolean isModified();
    
    /**
     * 设置、标记编辑器是否经过编辑操作。
     * @param modified 
     */
    void setModified(boolean modified);
    
    /**
     * 保存编辑操作
     */
    void save();
    
    /**
     * 获取编辑器
     * @return 
     */
    Editor getEditor();
    
    /**
     * 获取JmeEdit
     * @return 
     */
    T getJmeEdit();
    
    /**
     * 获取当前编辑器的UI根节点
     * @return 
     */
    Pane getEditRoot();
    
    /**
     * 获取属性面板
     * @return 
     */
    Pane getPropertyPanel();
    
    Pane getEditPanel();
    
}
