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
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.Text;

/**
 * 图标和Label
 * @author huliqing
 * @param <T>
 */
public class IconLabel<T> extends LinearLayout {
    // 标识
    private T id;
    
    private final Icon icon;
    private final Text label;
    
    public IconLabel(T id, String picFile, String text) {
        super(32, 32);
        this.id = id;
        icon = new Icon(picFile);
        label = new Text(text);
        label.setVerticalAlignment(BitmapFont.VAlign.Center);
        setLayout(Layout.horizontal);
        addView(icon);
        addView(label);
//        setDebug(true);
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        float iconWidth = height * 0.75f;
        float margin = 5;
        icon.setWidth(iconWidth);
        icon.setHeight(iconWidth);
        icon.setMargin(0, height * 0.125f, 0, 0);
        label.setWidth(width - iconWidth - margin);
        label.setHeight(height);
        label.setMargin(margin, 0, 0, 0);
    }
    
    public void setIcon(String iconFile) {
        icon.setImage(iconFile);
    }
    
    public void setLabel(String text) {
        label.setText(text);
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
    
}
