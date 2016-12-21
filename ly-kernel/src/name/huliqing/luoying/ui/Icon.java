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

import com.jme3.material.MatParamTexture;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author huliqing
 */
public class Icon extends AbstractUI {
    
    public Icon() {
        super();
        if (background == null) {
            createBackground();
            background.setUseAlpha(true); // default alpha
        }
    }
    
    public Icon(String file) {
        this();
        setImage(file);
    }
    
    public final void setImage(String file) {
        background.setFile(file);
        
        // 如果没有指定初始宽高,则默认使用图片的宽高.
        if (width <= 0 && height <= 0) {
            MatParamTexture matParam = background.getMaterial().getTextureParam("Texture");
            com.jme3.texture.Image image = matParam.getTextureValue().getImage();
            width = image.getWidth();
            height = image.getHeight();
        }
        
        setNeedUpdate();
    }
    
    public final void setColor(ColorRGBA color) {
        background.setColor(color);
    }

    public final void setUseAlpha(boolean useAlpha) {
        background.setUseAlpha(useAlpha);
    }

    @Override
    protected void clickEffect(boolean isPressed) {
        if (background != null) {
            background.setColor(isPressed ? UIFactory.getUIConfig().getActiveColor() : ColorRGBA.White);
        }
    }

    public Material getMaterial() {
        return background.getMaterial();
    }
}
