/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view;

import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.UI;

/**
 * 顶点按钮栏
 * @author huliqing
 */
public class ToolsView extends LinearLayout {

    // 按钮之间的间隔距离
    private float btnSpace;
    private float btnWidth;
    private float btnHeight;
    
    /**
     * 设置工具按钮的间隔
     * @param space 
     */
    public void setToolSpace(float space) {
        this.btnSpace = space;
        setNeedUpdate();
    }
    
    /**
     * 设置工具按钮的大小
     * @param width
     * @param height 
     */
    public void setToolSize(float width, float height) {
        this.btnWidth = width;
        this.btnHeight = height;
        setNeedUpdate();
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        
        if (childViews.isEmpty()) 
            return;
        
        UI child;
        for (int i = 0; i < childViews.size(); i++) {
            child = childViews.get(i);
            child.setWidth(btnWidth);
            child.setHeight(btnHeight);
            if (i > 0) {
                child.setMargin(btnSpace, 0, 0, 0);
            }
        }
    }

    @Override
    public void addView(UI view, int index) {
        super.addView(view, index);
        updateView();
        resize();
        setToCorner(Corner.RT);
    }
    
    
}
