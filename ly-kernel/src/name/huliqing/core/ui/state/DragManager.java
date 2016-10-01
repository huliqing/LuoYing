/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.ui.state;

import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.LY;
import name.huliqing.core.state.PlayState;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UIEventListener;

/**
 * 该类主要用于处理UI拖动操作,必须处理更新。
 * @author huliqing
 */
public class DragManager implements ReleaseListener {
    
    private final static DragManager ins = new DragManager();
    
    private UI moving; // 可移动的UI
    private final Vector2f lastMousePos = new Vector2f();
    
    // UI事件侦听
    private List<UIEventListener> eventListeners;
    
    private DragManager() {}
    public static DragManager getInstance() {
        return ins;
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
        lastMousePos.set(LY.getInputManager().getCursorPosition());
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
            Vector2f mousePos = LY.getInputManager().getCursorPosition();
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
