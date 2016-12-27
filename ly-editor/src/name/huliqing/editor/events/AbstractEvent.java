/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.events;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public abstract class AbstractEvent implements Event, KeyMappingListener{

    private static final Logger LOG = Logger.getLogger(AbstractEvent.class.getName());

    protected final String name;
    
    protected final List<EventListener> listeners = new ArrayList<EventListener>();
    protected final List<KeyMapping> keyMappings = new ArrayList<KeyMapping>();
    
    protected boolean initialized;
    
    /**
     * 事件的匹配结果,只有在所有keyMapping都匹配时该值才会为true.
     */
    protected boolean match;
    
    public AbstractEvent(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("EventMapping already initialized, eventMapping=" + this);
        }
        initialized = true;
        for (KeyMapping km : keyMappings) {
            km.initialize();
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        for (KeyMapping km : keyMappings) {
            km.cleanup();
        }
        initialized = false;
    }

    @Override
    public boolean isMatch() {
        return match;
    }

    @Override
    public void addListener(EventListener listener) {
        if (listener == null)
            throw new NullPointerException();
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(EventListener listener) {
        return listeners.remove(listener);
    }

    @Override
    public void addKeyMapping(KeyMapping keyMapping) {
        if (keyMapping == null) 
            throw new NullPointerException();
        if (!keyMappings.contains(keyMapping)) {
            keyMapping.addListener(this);
            keyMappings.add(keyMapping);
            if (isInitialized()) {
                keyMapping.initialize();
            }
        }
    }
    
    @Override
    public boolean removeKeyMapping(KeyMapping keyMapping) {
        keyMapping.removeListener(this);
        if (keyMapping.isInitialized()) {
            keyMapping.cleanup();
        }
        return keyMappings.remove(keyMapping);
    }

    @Override
    public <T extends KeyMapping> List<T> getKeyMappings() {
        return (List<T>) keyMappings;
    }
    
    @Override
    public void onKeyMapping(KeyMapping em) {
        // 只要有一个KeyMapping不匹配，则视为不匹配
        boolean tempResult = true;
        for (KeyMapping km : keyMappings) {
            if (!km.isMatch()) {
                tempResult = false;
                break;
            }
        }
        match = tempResult; // 匹配结果
        LOG.log(Level.INFO, "Event trigger, name={0}, result={1}", new Object[]{name, match});
        for (EventListener l : listeners) {
            l.onEvent(this);
        }
    }

    @Override
    public String toString() {
        return "eventName=" + name + ", match=" + match + ", initialized=" + initialized;
    }
    
}
