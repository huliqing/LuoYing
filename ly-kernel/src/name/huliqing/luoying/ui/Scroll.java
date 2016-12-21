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
 * 滚动条功能
 * @author huliqing
 */
public interface Scroll {
    
    /**
     * 更新滚动条layout及位置
     * @param parentWidth 父UI(支持滚动功能的UI)的可视宽度.
     * @param parentHeight 父UI的可视高度.
     * @param contentLength 父UI的所有内容的高度或宽度.
     */
    void updateScroll(float parentWidth, float parentHeight, float contentLength);
    
    /**
     * 设置侦听器,滚动条在滚动过程中会调用该侦听器
     * @param scrollListener 
     */
    void setScrollListener(ScrollListener scrollListener);
    
    /**
     * 获取侦听器
     * @return 
     */
    ScrollListener getScrollListener();
    
    /**
     * 获取滚动条的宽度.
     * @return 
     */
    float getScrollWidth();
    
    /**
     * 获取滚动条的高度
     * @return 
     */
    float getScrollHeight();
}
