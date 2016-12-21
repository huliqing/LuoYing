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
package name.huliqing.luoying.ui;

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
