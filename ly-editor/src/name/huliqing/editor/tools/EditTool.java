/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import name.huliqing.editor.toolbar.EditToolbar;
import java.util.logging.Logger;
import name.huliqing.editor.Editor;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.EventListener;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.edit.SimpleEditForm;

/**
 *
 * @author huliqing
 */
public abstract class EditTool implements Tool<EditToolbar> {

    private static final Logger LOG = Logger.getLogger(EditTool.class.getName());

    protected SimpleEditForm form;
    protected EditToolbar toolbar;
    protected Editor editor;
    protected EventHelper eventHelper;
    protected final EventListener eventListener = new ToolEventListener();
    protected final String name; // 编工具名称
    protected boolean initialized;
    
    public EditTool(String name) {
        this.name = name; 
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Tool already initialized, name=" + name + ", class=" + getClass().getName());
        }
        initialized = true;
        form = toolbar.getEditForm();
        this.editor = form.getEditor();
        if (eventHelper != null) {
            eventHelper.initialize();
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void update(float tpf) {
        // 由子类覆盖
    }

    @Override
    public void cleanup() {
        if (eventHelper != null) {
            eventHelper.cleanup();
        }
        initialized = false;
    }

    @Override
    public void setToolbar(EditToolbar toolbar) {
        this.toolbar = toolbar;
    }
    
    /**
     * 绑定一个事件，如果事件不存在将自动被创建。
     * @param name
     * @return 
     */
    protected final JmeEvent bindEvent(String name) {
        if (eventHelper == null) {
            eventHelper = new EventHelper(eventListener);
        }
        return eventHelper.getEvent(name);
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
            onToolEvent(e);
        }
    }

}
