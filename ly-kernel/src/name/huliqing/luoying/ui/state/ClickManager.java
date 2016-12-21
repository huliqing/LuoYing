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

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UIEventListener;
import name.huliqing.luoying.ui.UISound;

/**
 *
 * @author huliqing
 */
public class ClickManager implements ReleaseListener {
//    private final static Logger LOG = Logger.getLogger(ClickManager.class.getName());
    
    private final static ClickManager INSTANCE = new ClickManager();
    
    // 双击时间限制(毫秒）即两次单击的时间间隔在这个时间之内时视为双击
    // 应该处理双击事件
    private final long dbclickLimit = 200;
    // 需要触发事件的UI
    private UI pressUI;      // 按下时的UI
    private UI releaseUI;    // 释放时的UI
    
    // 鼠标按下时的时间.
    private long clickPressTime;
    // 鼠标释放时的时间
    private long clickReleaseTime;
    
    // 限制鼠标按下后允许的移动距离，当鼠标按下后的移动(在x,y的任一方向)距离超过
    // 该值时，则视为无效的点击,这时不允许触发点击事件。
    // 该问题避免如下BUG: 当鼠标在按下后滑动（拖动）后再释放，该操作
    // 会导致按下时的事件和释放时的事件（isPress)所关联的UI不一致。
    // 如按下时为uiA,而经过拖动后释放，这个时候可能触发到uiB的(!isPress)事件。
    private final float clickMoveLimit = 15f;
    
    // 鼠标点击时按下的时间限制,当鼠标按下按钮后超过该时间时,则不视为点击,单位毫秒
    private final float clickPressTimeLimit = 270;
    // 记录最后一次点击时的鼠标位置。
    private final Vector2f lastCursorPosition = new Vector2f();
    
    // UI事件侦听
    private List<UIEventListener> eventListeners;
    
    private ClickManager() {}
    public static ClickManager getInstance() {
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
    
    public void fireEvent(UI ui, boolean isPressed) {
        
        if (!ui.hasClickEvent() && !ui.hasDBClickEvent()) {
            return;
        }
        
        // 按下事件应该直接执行
        if (isPressed) {
            clickPressTime = LuoYing.getGameTime();
            lastCursorPosition.set(LuoYing.getCursorPosition());
            pressUI = ui;
            pressUI.fireClick(true);
            fireUIClickListener(pressUI, true, false);
            return;
        }
        
        // ==== 鼠标释放时 ==== 
        
        // 1.如果鼠标放开时的UI与鼠标按下时的UI不一致，则忽略点击
        if (ui != pressUI) {
            return;
        }
        
        // 2.当鼠标按下太久时,不认为是点击或双击事件.
//        Logger.get(UIClickManager.class).log(Level.INFO, "click time={0}", (Common.getCurrentTime() - clickPressTime));
        if (LuoYing.getGameTime() - clickPressTime > clickPressTimeLimit) {
            return;
        }
        
        // 3.如果鼠标放开时的位置偏移过大，则忽略点击
        Vector2f cursorPosition = LuoYing.getCursorPosition();
        float xMove = FastMath.abs(cursorPosition.x - lastCursorPosition.x);
        float yMove = FastMath.abs(cursorPosition.y - lastCursorPosition.y);
        if ((xMove > clickMoveLimit || yMove > clickMoveLimit)) {
            return;
        }
        
        // 4.如果是双击事件，则直接启动(单击事件需要延迟一定时间,以等待确认是否为双击，因为双击事件优先)
        if (releaseUI == ui && LuoYing.getGameTime() - clickReleaseTime <= dbclickLimit) {
            releaseUI.fireDBClick(isPressed);
            fireUIClickListener(releaseUI, isPressed, true);
            
            releaseUI = null;
            clickReleaseTime = 0;
            return;
        } 
        
        // 如果是单击事件，则视情况:若该UI不存在双击事件，则直接执行单击事件，
        // 否则延迟执行
        if (!ui.hasDBClickEvent()) {
            
            // 播放点击声效
            UISound.playClick(ui);
            ui.fireClick(false);
            fireUIClickListener(ui, false, false);
        } else {
            releaseUI = ui;
            clickReleaseTime = LuoYing.getGameTime();
        }

    }
    
    void update(float tpf) {
        // 延迟执行的单击事件
        if (releaseUI != null 
                && LuoYing.getGameTime()- clickReleaseTime > dbclickLimit) {
            
            releaseUI.fireClick(false);
            fireUIClickListener(releaseUI, false, false);
            
            // 这里的isPress始终为false
            UISound.playClick(releaseUI);
            
            releaseUI = null; // 释放
            clickReleaseTime = 0;
            
        }
    }

    /**
     * 监听鼠标放开的动作
     */
    @Override
    public void release() {
        if (pressUI != null) {
            pressUI.onRelease();
            fireUIReleaseListener(pressUI);
        }
    }
    
    public void cleanup() {
        if (eventListeners != null) {
            eventListeners.clear();
        }
    }
    
    // 触发UI监听事件
    private void fireUIClickListener(UI ui, boolean isPressed, boolean dbclick) {
        if (eventListeners == null)
            return;
        for (int i = 0; i < eventListeners.size(); i++) {
            eventListeners.get(i).UIClick(ui, isPressed, dbclick);
        }
    }
    
    private void fireUIReleaseListener(UI ui) {
        if (eventListeners == null)
            return;
        for (int i = 0; i < eventListeners.size(); i++) {
            eventListeners.get(i).UIRelease(ui);
        }
    }
}
