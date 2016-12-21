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

import com.jme3.font.BitmapFont;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class ItemTitle extends Title {

    private final Text icon;
    private final Text name;
    private final Text total;
    
    public ItemTitle(float width, float height) {
        super(width, height);
        icon = new Text("");
        icon.setHeight(height);
        icon.setFontSize(UIFactory.getUIConfig().getDesSize());
        icon.setFontColor(UIFactory.getUIConfig().getDesColor());
        icon.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        name = new Text(ResourceManager.get(ResConstants.COMMON_NAME));
        name.setHeight(height);
        name.setFontSize(UIFactory.getUIConfig().getDesSize());
        name.setFontColor(UIFactory.getUIConfig().getDesColor());
        name.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        total = new Text(ResourceManager.get(ResConstants.COMMON_TOTAL));
        total.setHeight(height);
        total.setFontSize(UIFactory.getUIConfig().getDesSize());
        total.setFontColor(UIFactory.getUIConfig().getDesColor());
        total.setVerticalAlignment(BitmapFont.VAlign.Center);
        addView(icon);
        addView(name);
        addView(total);
    }
    
    public void setColumnsWidth(float[] widths) {
        icon.setWidth(widths[0]);
        name.setWidth(widths[1]);
        total.setWidth(widths[2]);
        setNeedUpdate();
    }
    
}
