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
package name.huliqing.luoying.ui.state;

import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UIEventListener;

/**
 * 该类主要用于处理UI拖动操作,必须处理更新。
 * @author huliqing
 */
public class DragManager implements ReleaseListener {
    
    private final static DragManager INSTANCE = new DragManager();
    
    private UI moving; // 可移动的UI
    private final Vector2f lastMousePos = new Vector2f();
    
    // UI事件侦听
    private List<UIEventListener> eventListeners;
    
    private DragManager() {}
    public static DragManager getInstance() {
        return INSTANCE;
    }
    
    public void addEventListener(UIEventListener listener) {
        if (eventListeners == null) {
            eventListeners = new ArrayList<UIEventListener>();
        }
        if (!eventListeners.contains(listener)) {
            eventListeners.add(listener);
        }
    }
    
    public boolean removeEventListener(UIEventListener listener) {
        if (eventListeners == null)
            return false;
        return eventListeners.remove(listener);
    }
    
    public void startMoving(UI ui) {
        lastMousePos.set(LuoYing.getInputManager().getCursorPosition());
        moving = ui;
        fireUIDragListener(ui, true);
    }
    
    public void releaseMoving(UI ui) {
        if (moving == ui) {
            moving = null;
            fireUIDragListener(ui, false);
        }
    }
    
    /**
     * UI 拖动逻辑更新
     * @param tpf 
     */
    void update(float tpf) {
        if (moving != null) {
            Vector2f mousePos = LuoYing.getInputManager().getCursorPosition();
            if (mousePos.x != lastMousePos.x || mousePos.y != lastMousePos.y) {
                
                moving.fireDrag(mousePos.x - lastMousePos.x, mousePos.y - lastMousePos.y);
                
                // 记住最后鼠标位置
                lastMousePos.set(mousePos);
                
            }
        }
    }
    
    /**
     * 该方法只用于监听鼠标的释放动作,当鼠标释放时就停止拖动
     */
    @Override
    public void release() {
        if (moving != null) {
            releaseMoving(moving);
        }
    }
    
    public void cleanup() {
        release();
        if (eventListeners != null) {
            eventListeners.clear();
        }
    }

    // 触发UI监听事件
    private void fireUIDragListener(UI ui, boolean start) {
        if (eventListeners == null)
            return;
        for (int i = 0; i < eventListeners.size(); i++) {
            if (start) {
                eventListeners.get(i).UIDragStart(ui);
            } else {
                eventListeners.get(i).UIDragEnd(ui);
            }
        }
    }
    
}
