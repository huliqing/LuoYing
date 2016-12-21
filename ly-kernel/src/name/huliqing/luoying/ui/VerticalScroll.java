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

import com.jme3.math.Vector3f;
import name.huliqing.luoying.utils.MathUtils;

/**
 * 垂直滚动条,主要用于各UI组件中
 * @author huliqing
 */
public class VerticalScroll extends AbstractUI implements Scroll {
    
    // 侦听器,滚动条滚动时的侦听器.
    private ScrollListener listener;

    // 父UI的宽度和高度,如果父UI的宽度或高度发生变化,scrollLength也会发生变化.
    private float parentWidth;
    private float parentHeight;
    
    // 滚动内容的实际最大长度,即父UI的全部内容的长度
    private float contentLength;
    
    // 可滚动的区域的长度,如垂直滚动条的可滚动长度为 = 父UI的高度-当前UI的高度.
    private float scrollLength;
    // 当前的滚动位置,值在0~1之间.0表示未进行滚动,
    // 通过 contentLength,parentHeight,scrollFactor 来计算出当前已经滚动的长度
    private float scrollFactor;
    
    
    public VerticalScroll(float width) {
        super(width, 1);
        this.setDragEnabled(true);
        this.setBackground(UIFactory.getUIConfig().getBackground(), true);
        this.setBackgroundColor(UIFactory.getUIConfig().getScrollColor(), true);
    }
    
    @Override
    protected void onDragMove(float xAmount, float yAmount) {
        Vector3f pos = getLocalTranslation();
        pos.addLocal(0, yAmount, 0);
        if (pos.y < 0) {
            pos.setY(0);
        } else if (pos.y > scrollLength) {
            pos.setY(scrollLength);
        }
        scrollFactor = (scrollLength - pos.y) / scrollLength;
        this.setLocalTranslation(pos);
        
        if (listener != null) {
            listener.onScroll((contentLength - parentHeight) * scrollFactor);
        }
    }
    
    /**
     * 更新滚动条,该方法需要由支持滚动功能的父UI进行调用,当父UI的宽度,高度发生变化时,或者
     * 可滚动内容发生变化时应该调用这个方法进行更新.
     * @param parentWidth 父UI的宽度
     * @param parentHeight 父UI的高度
     * @param contentLength 滚动内容的长度
     */
    @Override
    public void updateScroll(float parentWidth, float parentHeight, float contentLength) {
        Vector3f pos = this.getLocalTranslation();
        if (!MathUtils.compareFloat(this.contentLength, contentLength) 
                || !MathUtils.compareFloat(this.parentHeight, parentHeight)) {

            this.height = parentHeight / contentLength * parentHeight;

            pos.x = parentWidth - width;
            pos.y = parentHeight - height;

            this.scrollLength = parentHeight - height;

        }
        this.setLocalTranslation(pos);
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        this.contentLength = contentLength;
    }

    @Override
    public void setScrollListener(ScrollListener scrollListener) {
        this.listener = scrollListener;
    }

    @Override
    public ScrollListener getScrollListener() {
        return this.listener;
    }

    @Override
    public float getScrollWidth() {
        return this.width;
    }

    @Override
    public float getScrollHeight() {
        return this.height;
    }
    
    
}
