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

import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class IconPanel extends LinearLayout {
    
    private Icon tabIcon;
    private float factor;
    public IconPanel(float width, float height, String icon) {
        this(width, height, icon, 0.75f);
    }
    
    public IconPanel(float width, float height, String icon, float iconFactor) {
        super(width, height);
        this.factor = iconFactor;
        this.tabIcon = new Icon(icon);
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        addView(tabIcon);
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        tabIcon.setWidth(height * factor);
        tabIcon.setHeight(height * factor);
    }

    @Override
    protected void updateViewLayout() {
        super.updateViewLayout();
        tabIcon.setToCorner(Corner.CC);
    }
}
