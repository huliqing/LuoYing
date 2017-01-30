/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public abstract class EditTool<E extends SimpleJmeEdit, T extends EditToolbar> implements Tool<E, T> {

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
    
    public EditTool(String name, String tips, String icon) {
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
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void update(float tpf) {
        // 由子类覆盖
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
            if (!toolbar.isEnabled()) 
                return;
            
            onToolEvent(e);
        }
    }

}
