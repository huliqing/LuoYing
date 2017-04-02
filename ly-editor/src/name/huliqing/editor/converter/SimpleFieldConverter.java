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
package name.huliqing.editor.converter;

import javafx.geometry.Pos;
import javafx.scene.Node;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.xml.ObjectData;

/**
 *  普通ObjectData字段转换器, 
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public abstract class SimpleFieldConverter<E extends JfxAbstractEdit, T extends ObjectData> extends FieldConverter<E, T> {
//    private static final Logger LOG = Logger.getLogger(AbstractFieldConverter.class.getName());
    
    protected boolean ignoreChangedEvent;
    
    @Override
    public void initialize() {
        super.initialize();
        root.setText(field);
        root.setAnimated(false);
        root.setAlignment(Pos.CENTER_LEFT);
        
        // features
        boolean disabled = featureHelper.getAsBoolean(FEATURE_DISABLED);
        boolean collapsed = featureHelper.getAsBoolean(FEATURE_COLLAPSED);
        
        // Layout and features
        Node layout = createLayout();
        layout.setDisable(disabled);
        root.setExpanded(!collapsed);
        root.setContent(layout);
    }

    @Override
    public void cleanup() {
        root.setContent(null);
        super.cleanup();
    }
    
    /**
     * 更新字段属性
     * @param propertyValue 
     */
    public void updateAttribute(Object propertyValue) {
        if (ignoreChangedEvent) {
            return;
        }
        parent.updateAttribute(field, propertyValue);
    }
    
    /**
     * 更新View组件
     */
    @Override
    public void updateView() {
        // ignoreChangedEvent确保在更新UI的时候不会再回调到3D编辑场景中
        ignoreChangedEvent = true;
        updateUI();
        ignoreChangedEvent = false;
    }
    
    /**
     * 直接添加一个历史记录。
     * @param ur 
     */
    protected void addUndoRedo(UndoRedo ur) {
        jfxEdit.addUndoRedo(ur);
    }
    
    /**
     * 添加一个历史记录
     * @param beforeValue 字段改变前的值
     * @param afterValue 字段改变后的值
     */
    protected void addUndoRedo(Object beforeValue, Object afterValue) {
        jfxEdit.addUndoRedo(new JfxEditUndoRedo(field, beforeValue, afterValue));
    }
    
    /**
     * 更新UI，从data中获取属性并更新到JfxUI中
     */
    protected abstract void updateUI();
    
    private class JfxEditUndoRedo<T> implements UndoRedo {

        private final String property;
        private final T before;
        private final T after;
        
        public JfxEditUndoRedo(String property, T before, T after) {
            this.property = property;
            this.before = before;
            this.after = after;
        }
        
        @Override
        public void undo() {
            Jfx.runOnJfx(() -> {
                parent.updateAttribute(property, before);
                updateView();
            });
        }
        
        @Override
        public void redo() {
            Jfx.runOnJfx(() -> {
                parent.updateAttribute(property, after);
                updateView();
            });
        }
    }
    
}
