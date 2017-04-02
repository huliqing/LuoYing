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
 *
 * @author huliqing
 */
public class JmeEvents {
    
    private final JmeEvent[] events;
    private boolean consumed;
    
    /**
     * 设置事件
     * @param events 
     */
    public JmeEvents(JmeEvent[] events) {
        this.events = events;
    }
    
    /**
     * 获取当前所有有响应的事件
     * @return 
     */
    public JmeEvent[] getEvents() {
        return events;
    }
    
    /**
     * 销毁所有事件，调用该方法后，后续事件将不再响应。
     */
    public void consume() {
        consumed = true;
    }
    
    /**
     * 判断所有事件是否销毁
     * @return 
     */
    public boolean isConsumed() {
        return consumed;
    }
}
