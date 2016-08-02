/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.ui;

/**
 * 层叠布局
 * @author huliqing
 */
public class FrameLayout extends UILayout {
    
    public FrameLayout() {
        super();
    }
    
    public FrameLayout(float width, float height) {
        super(width, height);
    }

    @Override
    protected void updateViewChildren() {
    }
    
    @Override
    protected void updateViewLayout() {
        if (childViews.isEmpty()) 
            return;
        
        // 1.更新子组件自身布局
        for (int i = 0; i < childViews.size(); i++) {
            childViews.get(i).updateView();
        }
        
        // remove20160611,不应该再去setPosition,由外部设置子组件的位置即可。
        // 2.再更新子组件位置
//        UI child;
//        float x;
//        float y;
//        for (int i = 0; i < childViews.size(); i++) {
//            child = childViews.get(i);
//            if (!child.isVisible()) 
//                continue;
//
//            x = paddingLeft + child.getMarginLeft();
//            y = height - paddingTop - child.getMarginTop() - child.getHeight();
//            child.setPosition(x, y);
//        }

    }

    @Override
    public void resize() {
        if (childViews.isEmpty()) {
            if (needUpdate) {
                updateView();
            }
            return;
        }
        
        for (UI child : childViews) {
            child.resize();
        }
        
        float maxWidth = 0;
        float maxHeight = 0;
        UI child;
        float tempWidth;
        float tempHeight;
        for (int i = 0; i < childViews.size(); i++) {
            child = childViews.get(i);
            if (!child.isVisible()) 
                continue;
            tempWidth = child.getWidth() + child.getMarginLeft() + child.getMarginRight();
            if (tempWidth > maxWidth) {
                maxWidth = tempWidth;
            }
            tempHeight = child.getHeight() + child.getMarginTop() + child.getMarginBottom();
            if (tempHeight > maxHeight) {
                maxHeight = tempHeight;
            }
        }
        
        width = maxWidth + paddingLeft + paddingRight;
        height = maxHeight + paddingTop + paddingBottom;
        
        updateView();
    }
    
    
}
