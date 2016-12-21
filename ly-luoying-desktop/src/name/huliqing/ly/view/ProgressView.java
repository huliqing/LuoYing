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

import com.jme3.math.ColorRGBA;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.ly.constants.InterfaceConstants;

/**
 * 进度条
 * @author huliqing
 */
public class ProgressView extends FrameLayout {
    
    // 进度条外框
    private Icon progress;
    // 进度条
    private Icon core;
    // 最大值
    private float maxValue;
    // 当前值
    private float value;
    
    public ProgressView() {
        this(100, 75);
    }
    
    public ProgressView(float maxValue, float value) {
        super();
        this.maxValue = maxValue;
        this.value = value;
        init();
    }
    
    private void init() {
        progress = new Icon();
        core = new Icon();
        progress.setImage(InterfaceConstants.UI_PROGRESS_BAR);
        progress.setUseAlpha(true);
        core.setImage(InterfaceConstants.UI_PROGRESS_CORE);
        core.setUseAlpha(true);
        addView(progress);
        addView(core);
    }

    public float getMaxValue() {
        return maxValue;
    }
    
    public float getValue() {
        return value;
    }

    public void setColor(ColorRGBA color) {
        core.setColor(color);
    }

    public void setMaxValue(float maxValue) {
        if (this.maxValue != maxValue) {
            this.maxValue = maxValue;
            setNeedUpdate();
        }
    }

    public void setValue(float value) {
        if (this.value != value) {
            this.value = value;
            setNeedUpdate();
        }
    }
    
    // ==== End getter and setter 

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        progress.setWidth(width);
        progress.setHeight(height);
        core.setHeight(height * 0.8f);
        core.setMargin(0, height * 0.1f, 0, 0);
        if (maxValue <= 0) {
            core.setWidth(0);
        } else {
            float coreWidth = value / maxValue * width;
            core.setWidth(coreWidth > width ? width : coreWidth);
        }
        
    }
    
    
}
