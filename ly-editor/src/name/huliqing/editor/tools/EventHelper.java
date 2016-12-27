/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.editor.events.EventListener;
import name.huliqing.editor.events.JmeEvent;

/**
 *
 * @author huliqing
 */
public class EventHelper {
    
    private final Map<String, JmeEvent> events = new HashMap<String, JmeEvent>();
    private final EventListener listener;
    private boolean initialized;
    
    public EventHelper(EventListener listener) {
        this.listener = listener;
    }
    
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
        events.forEach((k, v) -> { v.initialize();});
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public void cleanup() {
        events.forEach((k, v) -> {
            if (v.isInitialized()) {
                v.cleanup();
            }
        });
        initialized = false;
    }
    
    public void clearAllEvents() {
        events.forEach((k, v) -> {
            if (v.isInitialized()) {
                v.cleanup();
            }
        });
        events.clear();
    }
    
    /**
     * 获取指定名称的事件，如果事件不存在，则自动创建一个事件。
     * @param name
     * @return 
     */
    public JmeEvent getEvent(String name) {
        JmeEvent event = events.get(name);
        if (event == null) {
            event = new JmeEvent(name);
            event.addListener(listener);
            events.put(name, event);
        }
        return event;
    }
    
    /**
     * 移除指定名称的事件，移除的事件将自动被清理
     * @param name
     * @return 
     */
    public boolean removeEvent(String name) {
        JmeEvent event = events.remove(name);
        if (event != null && event.isInitialized()) {
            event.cleanup();
        }
        return event != null;
    }
}
