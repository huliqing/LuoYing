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
package name.huliqing.editor.tools;

import name.huliqing.editor.Editor;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.EventListener;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.toolbar.EditToolbar;

/**
 *
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public abstract class AbstractTool<E extends SimpleJmeEdit, T extends EditToolbar> implements Tool<E, T> {

//    private static final Logger LOG = Logger.getLogger(EditTool.class.getName());

    protected Editor editor;
    protected E edit;
    protected T toolbar;
    
    protected String name; // 编工具名称
    protected String tips;
    protected String icon;
    protected boolean initialized;
    
    protected final EventListener eventListener = new ToolEventListener();
    protected final EventHelper eventHelper = new EventHelper(eventListener);
    
    public AbstractTool(String name, String tips, String icon) {
        this.name = name; 
        this.tips = tips;
        this.icon = icon;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTips() {
        return tips;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void initialize(E edit, T toolbar) {
        if (initialized) {
            throw new IllegalStateException("Tool already initialized, name=" + name + ", class=" + getClass().getName());
        }
        initialized = true;
        this.edit = edit;
        this.toolbar = toolbar;
        this.editor = edit.getEditor();
        this.eventHelper.initialize();
    }

    @Override
    public void update(float tpf) {
        // 由子类覆盖
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        eventHelper.cleanup();
        initialized = false;
    }
    
    /**
     * 绑定一个事件，如果事件不存在将自动被创建。
     * @param name
     * @return 
     */
    protected final JmeEvent bindEvent(String name) {
        return eventHelper.getOrCreateEvent(name);
    }
    
    /**
     * 删除指定的事件绑定
     * @param name 
     */
    protected void removeEvent(String name) {
        if (eventHelper != null) {
            eventHelper.removeEvent(name);
        }
    }
    
    /**
     * 当事件响应时该方法被调用, 只有在绑定了事件时该方法才会被调用。{@link #bindEvent(java.lang.String) }
     * @param e 
     * @see #bindEvent(java.lang.String) 
     */
    protected abstract void onToolEvent(Event e);
    
    private class ToolEventListener implements EventListener {
        @Override
        public void onEvent(Event e) {
            // 只有工具栏激活时才响应事件，这可以尽量避免工具栏事件冲突
            if (!toolbar.isEnabled() || !isInitialized()) 
                return;
            
            onToolEvent(e);
        }
    }

}
