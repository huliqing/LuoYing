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

/**
 * KeyMapping用于匹配按键事件，每一个KeyMapping只匹配一个按键。
 * @author huliqing
 */
public interface KeyMapping {
    
     /**
     * 初始化按键匹配
     */
    void initialize();
    
    /**
     * 判断是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理按键绑定，并释放资源
     */
    void cleanup();
    
    /**
     * 获取匹配名称
     * @return 
     */
    String getMappingName();
    
    /**
     * 判断按键是否匹配
     * @return 
     */
    boolean isMatch();
    
    /**
     * 添加一个按键匹配侦听器
     * @param listener 
     */
    void addListener(KeyMappingListener listener);
    
    /**
     * 移除一个按键匹配侦听器
     * @param listener
     * @return true如果侦听器存在
     */
    boolean removeListener(KeyMappingListener listener);
 
}
