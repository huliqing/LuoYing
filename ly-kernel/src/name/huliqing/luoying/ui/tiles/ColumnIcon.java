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
package name.huliqing.luoying.ui.tiles;

import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.Icon;

/**
 *
 * @author huliqing
 */
public class ColumnIcon extends FrameLayout {
    
    private Icon icon;
    
    public ColumnIcon(float width, float height, String icon) {
        super(width, height);
        this.icon = new Icon(icon);
        this.addView(this.icon);
    }
    
    public void setIcon(String pic) {
        if (pic != null) {
            icon.setImage(pic);
        } else {
            icon.setImage(UIFactory.getUIConfig().getMissIcon());
        }
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        this.icon.setWidth(width * 0.75f);
        this.icon.setHeight(height * 0.75f);
    }

    @Override
    protected void updateViewLayout() {
        super.updateViewLayout();
        icon.setToCorner(Corner.CC);
    }
    
}
