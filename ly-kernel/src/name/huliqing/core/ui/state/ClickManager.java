/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.ui.state;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.LY;
import name.huliqing.ly.game.state.PlayState;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UIEventListener;
import name.huliqing.core.ui.UISound;

/**
 *
 * @author huliqing
 */
public class ClickManager implements ReleaseListener {
//    private final static Logger logger = Logger.getLogger(ClickManager.class.getName());
    
    private final static ClickManager ins = new ClickManager();
    
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
    
    public void fireEvent(UI ui, boolean isPressed) {
        
        if (!ui.hasClickEvent() && !ui.hasDBClickEvent()) {
            return;
        }
        
        // remove20160217
//        // TODO:移出View包,不要依赖 PlayState
//        // 防止镜头转动
//        PlayState ps = Common.getPlayState();
//        if (ps != null) {
//            ps.setChaseEnabled(false);
//        }
        
        // 按下事件应该直接执行
        if (isPressed) {
            clickPressTime = LY.getGameTime();
            lastCursorPosition.set(LY.getCursorPosition());
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
        if (LY.getGameTime() - clickPressTime > clickPressTimeLimit) {
            return;
        }
        
        // 3.如果鼠标放开时的位置偏移过大，则忽略点击
        Vector2f cursorPosition = LY.getCursorPosition();
        float xMove = FastMath.abs(cursorPosition.x - lastCursorPosition.x);
        float yMove = FastMath.abs(cursorPosition.y - lastCursorPosition.y);
        if ((xMove > clickMoveLimit || yMove > clickMoveLimit)) {
            return;
        }
        
        // 4.如果是双击事件，则直接启动(单击事件需要延迟一定时间,以等待确认是否为双击，因为双击事件优先)
        if (releaseUI == ui && LY.getGameTime() - clickReleaseTime <= dbclickLimit) {
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
            clickReleaseTime = LY.getGameTime();
        }

    }
    
    void update(float tpf) {
        // 延迟执行的单击事件
        if (releaseUI != null 
                && LY.getGameTime()- clickReleaseTime > dbclickLimit) {
            
//            Log.get(ClickManager.class).log(Level.INFO, "Fire single click!ui={0}", releaseUI);
            
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
        
        // remove20160217,代码已经移出
//        // 恢复镜头转动
//        PlayState ps = Common.getPlayState();
//        if (ps != null) {
//            ps.setChaseEnabled(true);
//        }
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
