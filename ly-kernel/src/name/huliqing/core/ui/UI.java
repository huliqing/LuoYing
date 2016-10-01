/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.ui;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public interface UI {
    
    /** 事件响应接口 */
    public interface Listener {
        
        /**
         * 当view被点击时执行。
         * @param view
         * @param isPressed 是否处于按下状态
         */
        void onClick(UI view, boolean isPressed);
    }
    
    /** 拖动侦听 */
    public interface DragListener {
        
        /**
         * 拖动侦听
         * @param view
         */
        void onMove(UI view);
    }
    
    /**
     * 定位
     */
    public enum Corner {
        /** 特殊定位:左下 */
        LB, 
        /** 特殊定位:左中 */
        LC, 
        /** 特殊定位:左上 */
        LT, 
        /** 特殊定位:中下 */
        CB, 
        /** 特殊定位:中中 */
        CC,
        /** 特殊定位:中上 */
        CT, 
        /** 特殊定位:右下 */
        RB,
        /** 特殊定位:右中 */
        RC, 
        /** 特殊定位:右上 */
        RT;
        
        public static Corner identify(String name) {
            for (Corner c : values()) {
                if (c.name().equals(name)) {
                    return c;
                }
            }
            return null;
        }
    }
    
    Spatial getDisplay();
    
    void setDebug(boolean debug);
    
    float getWidth();
    
    void setWidth(float width);
    
    float getHeight();
    
    void setHeight(float height);
    
    /**
     * 获取localPosition
     * @return 
     */
    Vector3f getPosition();
    
    void setPosition(float x, float y);
    
    // remove20160309
//    void setPosition(float x, float y, float z);
//    
//    void setPosition(Vector3f position);
    
    void setMargin(float left, float top, float right, float bottom);
    
    float getMarginLeft();
    float getMarginTop();
    float getMarginRight();
    float getMarginBottom();
    
    void setBackground(String file, boolean useAlpha);
    
    void setBackgroundColor(ColorRGBA color, boolean useAlpha);
    
    void setBackgroundVisible(boolean visible);
    
    boolean isDragEnabled();
    
    void setDragEnabled(boolean enabled);
    
    boolean isVisible();
    
    void setVisible(boolean visible);

    /**
     * 是否存在事件响应
     * @return 
     */
    boolean hasEvent();
    
    boolean hasClickEvent();
    
    boolean hasDBClickEvent();
    
    /**
     * 是否阻止事件向父组件广播
     * @return 
     */
    boolean isPreventEvent();
    
    /**
     * 设置是否阻止事件向父组件扩散.
     * @param preventEvent 
     */
    void setPreventEvent(boolean preventEvent);
    
    /**
     * 是否禁止事件穿透,即禁止事件在不同层的UI之间穿透.
     * 如为true,则当前UI的事件处理后,下一层的UI事件将不再处理.
     * @return 
     */
    boolean isPreventCross();

    /**
     * 是否防止事件穿透,当UI(同层)互相覆盖时,上层UI的事件处理完成后,下层UI的事件将不再处理.
     * 把该值设置为false,同允许UI事件在同层间穿透.
     * @param preventCross 
     */
    void setPreventCross(boolean preventCross);
    
    /**
     * 更新组件布局
     */
    void updateView();
    
    /**
     * 获取父groupView,如果没有父groupView则返回null.
     * @return 
     */
    UILayout getParentView();
    
    /**
     * 内部使用,不要去手动调用
     * @param parentView 
     */
    void setParentView(UILayout parentView);
    
    void addClickListener(Listener listener);
    
    void addDBClickListener(Listener listener);
    
    void addDragListener(DragListener listener);
    
    boolean fireClick(boolean isPressed);
    
    boolean fireDBClick(boolean isPressed);
    
    void fireDrag(float xMoveAmount, float yMoveAmount);
    
    /**
     * 是否打开UI的默认事件效果
     * @return 
     */
    boolean isEffectEnabled();
    
    /**
     * 打开或关闭默认的事件效果
     * @param enabled 
     */
    void setEffectEnabled(boolean enabled); 
    
    /**
     * 判断声效是否打开
     * @return 
     */
    boolean isSoundEnabled();

    /**
     * 设置是否开启UI声效
     * @param soundEnabled 
     */
    void setSoundEnabled(boolean soundEnabled);

    String getSoundClick();

    /**
     * 如: "Sounds/aaa.ogg"
     * @param soundClick 
     */
    void setSoundClick(String soundClick);
    
    /**
     * 当UI被释放时的处理操作，只要UI有过pressed操作，不管是否有click事件发生，最
     * 后都会进行release.
     */
    void onRelease();
    
    /**
     * 让当前UI在最上层显示
     */
    void setOnTop();
    
    /**
     * 把组件设置到一个角落
     * @param corner 
     */
    void setToCorner(Corner corner);
    
    void resize();
    
}
