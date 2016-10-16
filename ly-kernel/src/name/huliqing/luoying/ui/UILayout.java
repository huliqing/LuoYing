/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.ui;

import com.jme3.util.SafeArrayList;
import java.util.List;

/**
 *
 * @author huliqing
 */
public abstract class UILayout extends AbstractUI {
    
    // 子view列表
    // 使用SafeArrayList,确保在clearView时不会出现 -> java.util.ConcurrentModificationException
    protected final SafeArrayList<UI> childViews = new SafeArrayList<UI>(UI.class);
    
    protected float paddingLeft;
    protected float paddingTop;
    protected float paddingRight;
    protected float paddingBottom;
    
    public UILayout() {
        super();
    }
    
    public UILayout(float width, float height) {
        super(width, height);
    }
    
    public void setPadding(float left, float top, float right, float bottom) {
        paddingLeft = left;
        paddingTop = top;
        paddingRight = right;
        paddingBottom = bottom;
    }
    
    /**
     * 获取内容区域的宽度,即减去paddingLeft,paddingRight
     * @return 
     */
    public float getContentWidth() {
        return width - paddingLeft - paddingRight;
    }
    
    /**
     * 获取内容区域的高度,即减去paddingTop,paddingBottom
     * @return 
     */
    public float getContentHeight() {
        return height - paddingTop - paddingBottom;
    }
    
    /**
     * 添加一个子view
     * @param view 
     */
    public void addView(UI view) {
        // 这里代码有些重复
        // 不要直接去调用重载的方法: addView(UI, int), 如: addView(UI, childViews.size());
        // 这会造成BUG,除非把addView(UI, int)设置为final.
        // 否则如果子类覆盖了addView(UI,int)的情况, 并可能在调用super.addView(UI)的时候
        // 会出现问题,即实际调用的是子类自己的addView(UI, int)方法.
        
        // 在重载方法的时候要注意在重载方法中的相互调用问题, 在调用重载方法的时候尽量确保被调用
        // 的重载方法为final.即不被子类覆盖.否则子类在调用super.xxx的时候可能造成错乱.
        // 这是比较容易造成隐蔽的BUG的问题.
        if (!childViews.contains(view) && view != this) {
            // 从旧的移除
            UILayout oldGroup = view.getParentView();
            if (oldGroup != null) {
                oldGroup.removeView(view);
            }
            
            // 添加到新的
            view.setParentView(this);
            childViews.add(view);
            
            // 添加节点
            attachChild(view.getDisplay());
            
            // 标记update
            setNeedUpdate();
        }
    }
    
    /**
     * 添加一个子view
     * @param view 
     * @param index
     */
    public void addView(UI view, int index) {
        if (!childViews.contains(view) && view != this) {
            // 从旧的移除
            UILayout oldGroup = view.getParentView();
            if (oldGroup != null) {
                oldGroup.removeView(view);
            }
            
            // 添加到新的
            view.setParentView(this);
            childViews.add(index, view);
            
            // 添加节点
            attachChildAt(view.getDisplay(), index);
            
            // 标记update
            setNeedUpdate();
        }
    }
    
    /**
     * 移除一个指定的VIEW
     * @param view
     * @return 
     */
    public boolean removeView(UI view) {
        boolean result = childViews.remove(view);
        if (result) {
            
            // remove20160306
//            view.getDisplay().removeFromParent();
//            view.setParentView(null);
            
            detachChild(view.getDisplay());
            view.setParentView(null);
            
            setNeedUpdate();
        }
        return result;
    }
    
    /**
     * 清理所有子组件
     */
    public void clearViews() {
        if (childViews.isEmpty())
            return;
        for (UI v : childViews) {
            v.getDisplay().removeFromParent();
        }
        childViews.clear();
    }
    
    /**
     * 获取子组件列表
     * @return 
     */
    public List<UI> getViews() {
        return childViews;
    }

    @Override
    public final void updateView() {
        super.updateView();
        
        // 更新子组件
        updateViewChildren();
        
        // 更新布局
        updateViewLayout();
    }
    
    // remove20160606不再使用
//    /**
//     * 开始更新自身UI
//     */
//    protected void updateViewStart() {
//        super.updateView();
//    }
    
    /**
     * 更新子组件,主要更新子组件的宽度，高度等
     * @param children 
     */
    protected abstract void updateViewChildren();
    
    /**
     * 更新组件布局
     */
    protected abstract void updateViewLayout();
    

}
