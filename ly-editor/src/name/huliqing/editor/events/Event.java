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

import java.util.List;

/**
 * 事件接口,一个事件可以绑定一个到多个键盘、鼠标按键来触发事件，当绑定的按键发生状态变化时会触发事件。
 * 通过调用{@link #isMatch() }方法来判断是否触发事件。
 * @author huliqing
 */
public interface Event {
    
    /**
     * 获取事件名称
     * @return 
     */
    String getName();
    
    /**
     * 初始化
     */
    void initialize();
    
    /**
     * 判断事件是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理事件
     */
    void cleanup();
    
    /**
     * 判断当前事件是否匹配。
     * @return 
     */
    boolean isMatch();
    
    /**
     * 添加一个按键匹配
     * @param keyMapping 
     */
    void addKeyMapping(KeyMapping keyMapping);
    
    /**
     * 移除一个按键匹配
     * @param keyMapping 
     * @return  
     */
    boolean removeKeyMapping(KeyMapping keyMapping);
    
    /**
     * 获取所有的按键绑定
     * @param <T>
     * @return 
     */
    <T extends KeyMapping> List<T> getKeyMappings();
    
    /**
     * 添加一个事件侦听器
     * @param listener 
     */
    void addListener(EventListener listener);
    
    /**
     * 移除指定的事件侦听器
     * @param listener
     * @return 
     */
    boolean removeListener(EventListener listener);
    
    /**
     * 调用该方法来销毁后续事件,调用该方法之后，后续匹配的事件将不会执行。当存在多个相同按键的匹配事件同时响应时
     * 可以使用该方法来阻止事件的继续响应。
     * @param <E>
     * @param consume 
     * @return  返回当前实例
     */
    <E extends Event> E setConsumed(boolean consume);
    
    /**
     * 判断是否事件销毁。
     * @return 
     */
    boolean isConsumed();
}
