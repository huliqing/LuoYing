/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.shortcut;

import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.InterfaceConstants;
import name.huliqing.luoying.data.define.CountObject;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.object.shortcut.AbstractShortcut;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class BaseUIShortcut<T extends ObjectData> extends AbstractShortcut<T> {
    private final static float NUM_COUNT_ALPHA = 0.75f;
    
    protected ShortcutView view;
    protected UI icon;
    protected Text count;
    
    // 开始拖动的时间点
    private long startDragTime;
    private boolean bucketVisible;
    
    protected final class ShortcutView extends FrameLayout {

        @Override
        protected void updateViewChildren() {
            super.updateViewChildren();
            updateShortcutViewChildren(width, height);
        }

        @Override
        protected void updateViewLayout() {
            super.updateViewLayout();
            updateShortcutViewLayout(width, height);
        }
        
        @Override
        protected void onDragStart() {
            onShortcutDragStart();
        }

        @Override
        protected void onDragMove(float xAmount, float yAmount) {
            super.onDragMove(xAmount, yAmount);
            onShortcutDragMove(xAmount, yAmount);
        }
        
        @Override
        public void onRelease() {
            super.onRelease();
            onShortcutRelease();
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        view = new ShortcutView();
        view.setWidth(width);
        view.setHeight(height);
        view.setLocalTranslation(location);
        view.setDragEnabled(dragEnabled);
        view.setBackground(InterfaceConstants.UI_SHORTCUT_BACKGROUND2, true);
        view.addClickListener(new UI.Listener() {
            @Override
            public void onClick(UI view, boolean isPressed) {
                onShortcutClick(isPressed);
            }
        });
        
        icon = createIconView();
        count = createCountView();
        
        view.addView(icon);
        view.addView(count);
    }

    @Override
    public void cleanup() {
        if (view != null) {
            view.removeFromParent();
        }
        super.cleanup(); 
    }
    
    @Override
    public Spatial getView() {
        return view;
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        if (view != null) {
            view.setWidth(width);
        }
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        if (view != null) {
            view.setHeight(height);
        }
    }

    @Override
    public void setLocation(Vector3f location) {
        super.setLocation(location);
        if (view != null) {
            view.setLocalTranslation(location);
        }
    }

    @Override
    public Vector3f getLocation() {
        if (view != null) {
            return view.getWorldTranslation();
        }
        return super.getLocation();
    }

    @Override
    public void setDragEnagled(boolean dragEnabled) {
        super.setDragEnagled(dragEnabled);
        if (view != null) {
            view.setDragEnabled(dragEnabled);
        }
    }

    @Override
    public boolean isDragEnabled() {
        if (view != null) {
            return view.isDragEnabled();
        }
        return dragEnabled;
    }

    /**
     * 当快捷方式被点击时该方法被调用。
     * @param pressed 鼠标或按键是否处于被按下状态。
     */
    public abstract void onShortcutClick(boolean pressed);
    
    /**
     * 更新快捷方式子组件的布局
     * @param width
     * @param height 
     */
    protected void updateShortcutViewChildren(float width, float height) {
        icon.setWidth(width * 0.85f);
        icon.setHeight(height * 0.85f);
        count.setWidth(width);
        count.setHeight(height);
        updateObjectData(objectData);
    }
    
    /**
     * 更新快捷方式的布局
     * @param width
     * @param height 
     */
    protected void updateShortcutViewLayout(float width, float height) {
        icon.setToCorner(UI.Corner.CC);
        count.setToCorner(UI.Corner.CC);
    }
    
    /**
     * 当快捷方式开始被拖动时该方法被调用。
     */
    protected void onShortcutDragStart() {
        startDragTime = LuoYing.getGameTime();
    }
    
    /**
     * 当快捷方式在被拖动时该方法会被持续调用
     * @param xAmount
     * @param yAmount 
     */
    protected void onShortcutDragMove(float xAmount, float yAmount) {
        // 拖动时间超过一定时间时才显示recycle,以避免在正常点击的时候也显示recycle图标。
        if (!bucketVisible) {
            if (LuoYing.getGameTime() - startDragTime > 100) {
                ShortcutManager.setBucketVisible(true);
                bucketVisible = true;
            }
        }
    }
    
    /**
     * 当快捷方式被点击后或拖放后释放时该方法会被调用
     */
    protected void onShortcutRelease() {
         // 检测是否销毁shortcut
        ShortcutManager.checkProcess(this);
        ShortcutManager.setBucketVisible(false);
        bucketVisible = false;
        startDragTime = 0;
    }
    
    /**
     * 创建快捷图标,如果没有或不需要则返回null.
     * @return 
     */
    protected UI createIconView() {
        return new Icon(objectData.getAsString("icon"));
    }
    
    /**
     * 创建一个物品的数量UI，如果没有或不需要则返回null.
     * @return 
     */
    protected Text createCountView() {
        Text objectCount = new Text("");
        objectCount.setWidth(width);
        objectCount.setHeight(height);
        objectCount.setVerticalAlignment(BitmapFont.VAlign.Center);
        objectCount.setAlignment(BitmapFont.Align.Center);
        objectCount.setAlpha(NUM_COUNT_ALPHA);
        return objectCount;
    }
    
    protected void updateObjectData(T newObjectData) {
        // remove20161109
//        if (objectData != newObjectData) {
//            objectData = newObjectData;
//        }
        
        if (!(objectData instanceof CountObject)) {
            count.setVisible(false);
            return;
        }
        CountObject co = (CountObject) objectData;
        if (co.getTotal() > 999) {
            count.setText("999+");
        } else {
            count.setText(String.valueOf(co.getTotal()));
        }
        // 只有刚好只有一件物品时不显示数量，提高UI美观度。
        // 剩0件时要显示，以告诉player没有该物品了
        count.setVisible(co.getTotal() != 1);
    }
}
