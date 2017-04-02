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

import java.util.List;
import name.huliqing.editor.Editor;
import name.huliqing.editor.toolbar.Toolbar;

/**
 * 编辑器的3D编辑器接口,这个界面主要作为3D环境来编辑3D物体
 * @author huliqing
 */
public interface JmeEdit {
    
    void initialize(Editor editor);
    
    boolean isInitialized();
    
    void update(float tpf);
    
    void cleanup();
    
    void undo();
    
    void redo();
    
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
     * 保存编辑结果
     */
    void save();
    
    /**
     * 获取主工具栏
     * @param <T>
     * @return 
     */
    <T extends Toolbar> T getToolbar();
    
    /**
     * 获取编辑器的扩展工具栏列表,如果没有则返回null.
     * @return 
     */
    List<Toolbar> getExtToolbars();
    
    /**
     * 获取整个编辑器应用
     * @return 
     */
    Editor getEditor();

    
}
