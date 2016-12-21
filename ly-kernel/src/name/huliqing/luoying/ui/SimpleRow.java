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

import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class SimpleRow<T> extends Row<T> {
    
    protected Text text;
    
    public SimpleRow() {
        super();
        text = new Text("");
        text.setWidth(width);
        text.setHeight(height);
        addView(text);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        text.setWidth(width);
        text.setHeight(height);
    }
    
    public void setAlignment(Align align) {
        text.setAlignment(align);
        setNeedUpdate();
    }
    
    public void setVerticalAlignment(VAlign align) {
        text.setVerticalAlignment(align);
        setNeedUpdate();
    }

    @Override
    public void displayRow(T data) {
//        if (datas.size() <= rowIndex) 
//            return;
        Object o = data;
        if (o == null)
            return;
        text.setText(o.toString());
    }
    
}
