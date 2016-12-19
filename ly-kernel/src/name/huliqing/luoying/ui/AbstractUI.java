/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.ui;

import name.huliqing.luoying.ui.state.DragManager;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Quad;
import java.util.LinkedHashSet;
import java.util.Set;
import name.huliqing.luoying.constants.AssetConstants;

/**
 * UI基类
 * @author huliqing
 */
public abstract class AbstractUI extends Node implements UI {
    
    /**
     *  一个空的listener，可用于添加到一些UI上，让这些UI可阻止事件继续穿透
     */
    public final static Listener EMPTY_LISTENER = new Listener() {
        @Override
        public void onClick(UI view, boolean isPressed) {
            // ignore
        }
    };
    
    private boolean debug;
    // 父layout
    protected UILayout parentView;
    protected float width;
    protected float height;
    protected float marginLeft;
    protected float marginTop;
    protected float marginRight;
    protected float marginBottom;
    
    // 背景
    protected Image background;
    // 事件响应Geo
    // 1.始终保持与当前UI的宽高度一致
    // 2.必须放在所有子组件的最下层（包括background),才能保证与子UI的点击检测顺序正确
    // 3.不能被移除
    protected Geometry eventGeo;
    
    // 是否打开拖动
    protected boolean dragEnabled;
    protected Listener dragListener;
    
    // ==== Click事件侦听列表
    protected Set<Listener> clickListeners;     // 单击
    protected Set<Listener> dbclickListeners;   // 双击
    protected Set<DragListener> dragListeners;  // 拖动
    // 阻止事件向父组件广播
    protected boolean preventEvent = true;
    // 阻止事件在不同UI层间穿透,当UI的事件处理后,下一层(非父子关系)的UI事件将不再处理.
    // 举例如:当一个按钮覆盖在另一个按钮之上时,如果preventCross=true,则点击按钮后将只处理
    // 最上面按钮的事件.
    protected boolean preventCross = true;
    
    // 是否开启事件效果.
    protected boolean effectEnabled = true;
    
    // 是否开启UI声效
    protected boolean soundEnabled = true;
    // 点击声效,如：Sounds/xyz.ogg
    protected String soundClick;
    
    // ---
    // 是否需要更新UI布局
    protected boolean needUpdate = true;
    
    // remove20160218,除了消耗性能，其它没有什么卵用。
//    // 使用懒惰模式,在该模式下不会执行组件的updateLogicalState
//    protected boolean lazy = true;
    
    public AbstractUI() {
        setQueueBucket(Bucket.Gui);
        setCullHint(CullHint.Never);
    }
    
    public AbstractUI(float width, float height) {
        this();
        this.width = width;
        this.height = height;
    }
    
    @Override
    public Spatial getDisplay() {
        return this;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
        setNeedUpdate();
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        setNeedUpdate();
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        setNeedUpdate();
    }

    @Override
    public Vector3f getPosition() {
        return getLocalTranslation();
    }

    @Override
    public void setPosition(float x, float y) {
        Vector3f loc = getLocalTranslation();
        loc.setX(x);
        loc.setY(y);
        setLocalTranslation(loc);
    }

    @Override
    public void setMargin(float left, float top, float right, float bottom) {
        marginLeft = left;
        marginTop = top;
        marginRight = right;
        marginBottom = bottom;
    }

    @Override
    public float getMarginLeft() {
        return marginLeft;
    }

    @Override
    public float getMarginTop() {
        return marginTop;
    }

    @Override
    public float getMarginRight() {
        return marginRight;
    }

    @Override
    public float getMarginBottom() {
        return marginBottom;
    }
    
    @Override
    public void setBackground(String file, boolean useAlpha) {
        if (background == null) {
            createBackground();
        }
        background.setUseAlpha(useAlpha);
        background.setFile(file);
    }

    @Override
    public void setBackgroundColor(ColorRGBA color, boolean useAlpha) {
        if (background == null) {
            createBackground();
        }
        background.setUseAlpha(useAlpha);
        background.setColor(color);
    }

    @Override
    public void setBackgroundVisible(boolean visible) {
        if (background != null) {
            background.setCullHint(visible ? CullHint.Never : CullHint.Always);
        }
    }
    
    protected void createBackground() {
        background = new Image();
        background.setWidth(width);
        background.setHeight(height);
        super.attachChildAt(background, 0);
    }

    @Override
    public boolean isDragEnabled() {
        return dragEnabled;
    }

    @Override
    public void setDragEnabled(boolean enabled) {
        this.dragEnabled = enabled;
        
        if (enabled) {
            final UI self = this;
            if (dragListener == null) {
                dragListener = new Listener() {
                    @Override
                    public void onClick(UI ui, boolean isPressed) {
                        if (!dragEnabled) 
                            return;
                        
                        if (isPressed) {
                            DragManager.getInstance().startMoving(self);
                            onDragStart();
                        } else {
                            DragManager.getInstance().releaseMoving(self);
                            onDragEnd();
                        }
                    }
                };
                addClickListener(dragListener);
            }
        }
        
    }

    @Override
    public boolean isVisible() {
        return getCullHint() == CullHint.Never;
    }

    @Override
    public void setVisible(boolean visible) {
        CullHint ch = visible ? CullHint.Never : CullHint.Always;
        if (ch != this.cullHint) {
            setCullHint(ch);
            setNeedUpdate();
        }
    }

    @Override
    public boolean hasEvent() {
        
        if (dragEnabled)
            return true;
        
        if (clickListeners != null && clickListeners.size() > 0)
            return true;
        
        if (dbclickListeners != null && dbclickListeners.size() > 0)
            return true;
        
        return false;
    }
    
    @Override
    public boolean hasClickEvent() {
        return (clickListeners != null && clickListeners.size() > 0);
    }

    @Override
    public boolean hasDBClickEvent() {
        return (dbclickListeners != null && dbclickListeners.size() > 0);
    }
    
    @Override
    public boolean isPreventEvent() {
        return preventEvent;
    }
    
    @Override
    public void setPreventEvent(boolean preventEvent) {
        this.preventEvent = preventEvent;
    }

    @Override
    public boolean isPreventCross() {
        return preventCross;
    }

    @Override
    public void setPreventCross(boolean preventCross) {
        this.preventCross = preventCross;
    }

//    @Override
//    public void updateGeometricState() {
//        super.updateGeometricState(); 
//    }

    @Override
    public void updateLogicalState(float tpf) {
        
        if (needUpdate) {
            updateView();
            needUpdate = false;
        }
        
//        // remove20160218,太过消耗性能
//        if (!lazy) {
//            super.updateLogicalState(tpf);
//        }
    }
    
    @Override
    public void updateView() {
        // 更新背景图
        if (background != null) {
            background.setWidth(width);
            background.setHeight(height);
        }
        
        // 更新事件响应区
        if (hasEvent() || debug) {
            if (eventGeo == null) {
                createEventGeo();
            }
            eventGeo.setLocalScale(width, height, 1);
            if (debug) {
                eventGeo.getMaterial().setColor("Color", ColorRGBA.Red);
                eventGeo.getMaterial().getAdditionalRenderState().setWireframe(true);
                eventGeo.setCullHint(CullHint.Never);
            }
        }
    }
    
    private void createEventGeo() {
        eventGeo = new Geometry("", new Quad(1, 1));
        Material mat = new Material(UIFactory.getUIConfig().getAssetManager(), AssetConstants.MATERIAL_UNSHADED);
        eventGeo.setMaterial(mat);
        eventGeo.setCullHint(CullHint.Always);
        super.attachChildAt(eventGeo, 0);
    }
    
    protected void setNeedUpdate() {
        needUpdate = true;
        if (parentView != null) {
            parentView.setNeedUpdate();
        }
    }

    @Override
    public UILayout getParentView() {
        return parentView;
    }
    
    @Override
    public void setParentView(UILayout parentView) {
        this.parentView = parentView;
    }
    
    @Override
    public void addClickListener(Listener listener) {
        if (clickListeners == null) {
            clickListeners = new LinkedHashSet<Listener>(1);
        }
        clickListeners.add(listener);
        setNeedUpdate();
    }

    @Override
    public void addDBClickListener(Listener listener) {
        if (dbclickListeners == null) {
            dbclickListeners = new LinkedHashSet<Listener>(1);
        }
        dbclickListeners.add(listener);
        setNeedUpdate();
    }

    @Override
    public void addDragListener(DragListener listener) {
        if (dragListeners == null) {
            dragListeners = new LinkedHashSet<DragListener>(1);
        }
        dragListeners.add(listener);
        setNeedUpdate();
    }

    @Override
    public boolean fireClick(boolean isPressed) {
        return fireClickInner(isPressed, false);
    }
    
    final boolean fireClickInner(boolean isPressed, boolean fired) {
        if (clickListeners != null && !clickListeners.isEmpty()) {
            fired = true;
            for (Listener click : clickListeners) {
                click.onClick(this, isPressed);
            }
            if (effectEnabled) {
                clickEffect(isPressed);
            }
            // 如果当前已经处理了事件，并且该UI设置了阻止事件传播，则停止事件传播
            // 注：只有当前UI有事件才会根据preventEvent确定是否进行事件传播，如果
            // 当前没有事件，则应该始终传播,交由其它父UI去处理事件
            if (preventEvent) {
                return fired;
            }
        }
        
        // ----remove20160612,
        // 这是一个比较严重的BUG，花了很长时间才发现，在以下情形会造成一些问题:
        // 假设：
        // 1.a->b->c 三个UI层次顺序是 a,b,c;  
        // 2.a是b和c的父UI，b和c的关系是兄弟UI节点。
        // 3.a在最下面,b在中间，c在最上面.三个UI刚好完全重叠.
        // 4.a和b存在事件，c没有事件。
        // 结果，这个时候想要点击b，以触发b事件的时候就会存在问题，因为c在b上面，所以c会受到点击，由于c没有事件，
        // 这时c会把事件传递到父UI"a", 即造成b的事件被跳过的BUG。所以这里不能直接传递事件到父UI中去,即不再传递事件向
        // 父UI。
        // ----
//        // do parent click
//        if (parentView != null) {
//            return parentView.fireClickInner(isPressed, fired);
//        }
        
        return fired;
    }
    
    /**
     * 处理点击效果,由子组件处理
     * @param isPressed
     */
    protected void clickEffect(boolean isPressed) {
        if (background != null) {
            background.setColor(isPressed ? UIFactory.getUIConfig().getActiveColor() : ColorRGBA.White);
        }
    }

    @Override
    public boolean fireDBClick(boolean isPressed) {
        return fireDBClickInner(isPressed, false);
    }
    
    final boolean fireDBClickInner(boolean isPressed, boolean fired) {
        // 1.优先处理双击事件, 双击事件不向父UI扩展事件，减少复杂度
        if (dbclickListeners != null && !dbclickListeners.isEmpty()) {
            fired = true;
            for (Listener dbclick : dbclickListeners) {
                dbclick.onClick(this, isPressed);
            }
            if (preventEvent) {
                return fired;
            }
        }
        
        // ----remove20160612,
        // 这是一个比较严重的BUG，花了很长时间才发现，在以下情形会造成一些问题:
        // 假设：
        // 1.a->b->c 三个UI层次顺序是 a,b,c;  
        // 2.a是b和c的父UI，b和c的关系是兄弟UI节点。
        // 3.a在最下面,b在中间，c在最上面.三个UI刚好完全重叠.
        // 4.a和b存在事件，c没有事件。
        // 结果，这个时候想要点击b，以触发b事件的时候就会存在问题，因为c在b上面，所以c会受到点击，由于c没有事件，
        // 这时c会把事件传递到父UI"a", 即造成b的事件被跳过的BUG。所以这里不能直接传递事件到父UI中去,即不再传递事件向
        // 父UI。
        // ----
        // do parent 
//        if (parentView != null) {
//            return parentView.fireDBClickInner(isPressed, fired);
//        }
        return fired;
    }
    
    @Override
    public void fireDrag(float xMoveAmount, float yMoveAmount) {
        // 1. 如果当前组件不允许拖动，则交由父UI决定，可能父UI允许拖动。
        if (dragEnabled) {
            onDragMove(xMoveAmount, yMoveAmount);
            if (dragListeners != null && !dragListeners.isEmpty()) {
                for (DragListener listener : dragListeners) {
                    listener.onMove(this);
                }
                if (preventEvent) {
                    return;
                }
            }
        }
        
        if (parentView != null) {
            parentView.fireDrag(xMoveAmount, yMoveAmount);
        }
    }
    
    protected void onDragStart() {
        // do something
    }
    
    protected void onDragEnd() {
        // do something
    }
    
    /**
     * 当组件被拖动时触发
     * @param xMoveAmount 在X轴上的移动量
     * @param yMoveAmount 在Y轴上的移动量
     */
    protected void onDragMove(float xMoveAmount, float yMoveAmount) {
        move(xMoveAmount, yMoveAmount, 0);
    }
    
    @Override
    public void onRelease() {
        if (effectEnabled) {
            clickEffect(false);
        }
    }

    @Override
    public boolean isEffectEnabled() {
        return this.effectEnabled;
    }

    @Override
    public void setEffectEnabled(boolean enabled) {
        this.effectEnabled = enabled;
    }
    
    @Override
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * 设置是否开启UI声效（需要同时开启UIConfig中的配置）
     * @param soundEnabled 
     */    
    @Override
    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }
    
    @Override
    public String getSoundClick() {
        return soundClick;
    }

    @Override
    public void setSoundClick(String soundClick) {
        this.soundClick = soundClick;
    }
    
//    @Deprecated
//    @Override
//    public void setOnTop() {
////        if (Config.debug) {
////            Logger.getLogger(AbstractUI.class.getName()).log(Level.WARNING, "Do not use this method!");
////        }
//        
//        // remove20160309以后不再使用这个方法，即让UI根据attach的顺序去排序。
//        // 后attach的UI自动放在上面。
//        // 如果开放这个功能可能会导致一些刚打开的UI反而放在旧UI的下面，被遮住。
////        Vector3f pos = this.getLocalTranslation();
////        pos.z = globalZIndex++;
////        super.setLocalTranslation(pos);
//    }

    @Override
    public void setToCorner(Corner cornerPosition) {
        float parentWidth;
        float parentHeight;
        float paddingLeft = 0;
        float paddingTop = 0;
        float paddingRight = 0;
        float paddingBottom = 0;
        if (parentView != null) {
            parentWidth = parentView.getWidth();
            parentHeight = parentView.getHeight();
            paddingLeft = parentView.paddingLeft;
            paddingTop = parentView.paddingTop;
            paddingRight = parentView.paddingRight;
            paddingBottom = parentView.paddingBottom;
        } else {
            parentWidth = UIFactory.getUIConfig().getScreenWidth();
            parentHeight = UIFactory.getUIConfig().getScreenHeight();
        }
        
        float x = 0;
        float y = 0;
        // center
        if (cornerPosition == Corner.CB) {
            x = (parentWidth - width) * 0.5f;
            x += getMarginLeft();
            x -= getMarginRight();
            y += paddingBottom;
            y -= getMarginTop();
            y += getMarginBottom();
        } else if (cornerPosition == Corner.CC) {
            x = (parentWidth - width) * 0.5f;
            x += getMarginLeft();
            x -= getMarginRight();
            y = (parentHeight - height) * 0.5f;
            y -= getMarginTop();
            y += getMarginBottom();
        } else if (cornerPosition == Corner.CT) {
            x = (parentWidth - width) * 0.5f;
            x += getMarginLeft();
            x -= getMarginRight();
            y = parentHeight - height;
            y -= paddingTop;
            y -= getMarginTop();
            y += getMarginBottom();
        }
        // left 
        else if (cornerPosition  == Corner.LB) {
            x = y = 0;
            x += paddingLeft;
            x += getMarginLeft();
            x -= getMarginRight();
            y += paddingBottom;
            y -= getMarginTop();
            y += getMarginBottom();
        } else if (cornerPosition == Corner.LC) {
            x = 0;
            x += paddingLeft;
            x += getMarginLeft();
            x -= getMarginRight();
            y = (parentHeight - height) * 0.5f;
            y -= getMarginTop();
            y += getMarginBottom();
        } else if (cornerPosition == Corner.LT) {
            x = 0;
            x += paddingLeft;
            x += getMarginLeft();
            x -= getMarginRight();
            y = parentHeight - height;
            y -= paddingTop;
            y -= getMarginTop();
            y += getMarginBottom();
        }
        // right
        else if (cornerPosition == Corner.RB) {
            x = parentWidth - width;
            x -= paddingRight;
            x += getMarginLeft();
            x -= getMarginRight();
            y = 0;
            y += paddingBottom;
            y -= getMarginTop();
            y += getMarginBottom();
        } else if (cornerPosition == Corner.RC) {
            x = parentWidth - width;
            x -= paddingRight;
            x += getMarginLeft();
            x -= getMarginRight();
            y = (parentHeight - height) * 0.5f;
            y -= getMarginTop();
            y += getMarginBottom();
        } else if (cornerPosition == Corner.RT) {
            x = parentWidth - width;
            x -= paddingRight;
            x += getMarginLeft();
            x -= getMarginRight();
            y = parentHeight - height;
            y -= paddingTop;
            y -= getMarginTop();
            y += getMarginBottom();
        }
        
        // 注：不要设置Z值，这会改变它的层叠顺序
        Vector3f location = getLocalTranslation();
        location.setX(x).setY(y);
        setLocalTranslation(location);
    }
    
    /**
     * 对于不可见的UI将不进行collide检测，也就是如果当前UI visible=false,
     * 则该方法始终返回0.主要为优化UI事件检测性能。
     * @param other
     * @param results
     * @return 
     */
    @Override
    public int collideWith(Collidable other, CollisionResults results) {
        if (!this.isVisible()) {
            return 0;
        } else {
            return super.collideWith(other, results);
        }
    }
    
    /**
     * 添加子节点.
     * @param child
     * @param index
     * @return 
     */
    @Override
    public int attachChildAt(Spatial child, int index) {
        if (child == background || child == eventGeo) {
            throw new IllegalArgumentException("Cound not attach background or eventGeo! View=" + getName() 
                    + ",child=" + child);
        }
        
        // 确保被添加的子节点不在background和eventGeo前面
        int idx = 0;
        if (background != null) {
            idx++;
        }
        if (eventGeo != null) {
            idx++;
        }
        if (index < idx) {
            index = idx;
        }
        return super.attachChildAt(child, index);
    }

    @Override
    public boolean removeFromParent() {
        if (parentView != null) {
            parentView.removeView(this);
        }
        return super.removeFromParent();
    }
    
    /**
     * 重置组件的layout,使组件的大小适合内容的宽度和高度
     */
    @Override
    public void resize() {
        // do resize
    }
    
}
