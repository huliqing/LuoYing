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

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author huliqing
 */
public interface UIConfig {
    
    void setAssetManager(AssetManager assetManager);
    
    AssetManager getAssetManager(); 
    
    /**
     * 获取屏幕宽度
     * @return 
     */
    float getScreenWidth();
    
    /**
     * 获取屏幕高度
     * @return 
     */
    float getScreenHeight();
    
    /**
     * 获取默认的字体
     * @return 
     */
    BitmapFont getFont();
    
    /**
     * 获取"MISS"图片路径,用于代替一些找不到的资源图片。
     * @return 
     */
    String getMissIcon();
    
    /**
     * 获取背景图片
     * @return 
     */
    String getBackground();

    ColorRGBA getTitleBgColor();
    
    /**
     * 获取默认的title区域高度设置
     * @return 
     */
    float getTitleHeight();
    
    /**
     * 获取默认的标题字体的大小
     * @return 
     */
    float getTitleSize();
    
    ColorRGBA getBodyBgColor();
    
    /**
     * 获取默认的UI内容的字体大小
     * @return 
     */
    float getBodyFontSize();
    
    ColorRGBA getBorderColor();
    
    ColorRGBA getScrollColor();
    
    float getFooterHeight();
    ColorRGBA getFooterBgColor();
    
    // -------------------------------------------------------------------------
    // Button
    // -------------------------------------------------------------------------
    ColorRGBA getButtonBgColor();
    
    ColorRGBA getButtonFontColor();
    
    String getButtonBgFile();
    
    float getButtonWidth();
    
    float getButtonHeight();
    
    float getButtonFontSize();
    
    /**
     * 默认的关闭按钮的图片，参考：window标题栏上的close按钮
     * @return 
     */
    String getButtonClose();
    
    /**
     * 默认的UI组件的“确认”按钮图片,参考Confirm组件
     * @return 
     */
    String getButtonConfirmOk();
    
    /**
     * 默认的UI组件的“取消”按钮图片，参考Confirm组件
     * @return 
     */
    String getButtonConfirmCancel();
    
    /**
     * 获取激活颜色，如按钮被点击时的颜色，UI处于选中时的颜色。
     * @return 
     */
    ColorRGBA getActiveColor();

    
    /**
     * 获取描述文字的字体大小
     * @return 
     */
    float getDesSize();
    
    /**
     * 获取描述文字的字体颜色
     * @return 
     */
    ColorRGBA getDesColor();

    /**
     * 是否开启UI声效
     * @return 
     */
    boolean isSoundEnabled();
    
    /**
     * 获取默认的点击音效.
     * @return 
     */
    String getSoundClick();
    
    /**
     * 获取列表的默认高度
     * @return 
     */
    float getListTitleHeight();

}
