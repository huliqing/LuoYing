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
package name.huliqing.ly.view;

import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UI;
import name.huliqing.ly.state.MenuTool;

/**
 * 顶点按钮栏
 * @author huliqing
 */
public class ToolsView extends LinearLayout implements MenuTool {

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
    public final void addView(UI view, int index) {
        super.addView(view, index);
        updateView();
        resize();
        setToCorner(Corner.RT);
    }

    @Override
    public void addMenu(UI menu) {
        addView(menu, childViews.size());
    }

    @Override
    public void addMenu(UI menu, int index) {
        addView(menu, index);
    }

    @Override
    public boolean removeMenu(UI menu) {
        return removeView(menu);
    }
    
    
}
