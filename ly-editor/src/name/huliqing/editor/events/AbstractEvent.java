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
package name.huliqing.editor.events;

import java.util.ArrayList;
import java.util.List;
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
    
    /**
     * 判断事件是否已经响应完毕,
     */
    protected boolean eventFired;
    
    // 判断是否销毁后续事件
    protected boolean consumed;
    
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
    public boolean isConsumed() {
        return consumed;
    }

    @Override
    public <E extends Event> E setConsumed(boolean consumed) {
        this.consumed = consumed;
        return (E) this;
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
        eventFired = false;
        // 只要有一个KeyMapping不匹配，则视为不匹配
        boolean tempResult = true;
        for (KeyMapping km : keyMappings) {
            if (!km.isMatch()) {
                tempResult = false;
                break;
            }
        }
        match = tempResult; // 匹配结果
    }
    
    /**
     * 触发事件响应
     */
    void fireEventListeners() {
        if (eventFired) {
            return;
        }
        for (EventListener l : listeners) {
            l.onEvent(this);
        }
        eventFired = true;
    }

    @Override
    public String toString() {
        return "eventName=" + name + ", match=" + match + ", initialized=" + initialized;
    }
    
}
