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
package name.huliqing.luoying.object.position;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.PositionData;
import name.huliqing.luoying.ui.UI.Corner;

/**
 * GUI界面上的位置点
 * @author huliqing
 * @param <T>
 */
public class ViewPosition<T extends PositionData> extends AbstractPosition<T> {
    
    // 在视图界面上的角落位置
    private Corner corner;
    // left,top,right,bottom. 权重取值0.0~1.0
    private float[] marginWeight;

    @Override
    public void setData(T data) {
        super.setData(data);
        corner = Corner.identify(data.getAsString("corner"));
        marginWeight = data.getAsFloatArray("marginWeight");
    }

    @Override
    public Vector3f getPoint(Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        
        float x = 0;
        float y = 0;
        float sw = LuoYing.getSettings().getWidth();
        float sh = LuoYing.getSettings().getHeight();
        
        float marginLeft = 0;
        float marginTop = 0;
        float marginRight = 0;
        float marginBottom = 0;
        if (marginWeight != null) {
            marginLeft = marginWeight[0] * sw;
            marginTop = marginWeight[1] * sh;
            marginRight = marginWeight[2] * sw;
            marginBottom = marginWeight[3] * sh;
        }
        
        // center
        if (corner == Corner.CB) {
            x = sw * 0.5f;
            x += marginLeft;
            x -= marginRight;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.CC) {
            x = sw * 0.5f;
            x += marginLeft;
            x -= marginRight;
            y = sh * 0.5f;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.CT) {
            x = sw * 0.5f;
            x += marginLeft;
            x -= marginRight;
            y = sh;
            y -= marginTop;
            y += marginBottom;
        }
        // left 
        else if (corner  == Corner.LB) {
            x = y = 0;
            x += marginLeft;
            x -= marginRight;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.LC) {
            x = 0;
            x += marginLeft;
            x -= marginRight;
            y = sh * 0.5f;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.LT) {
            x = 0;
            x += marginLeft;
            x -= marginRight;
            y = sh;
            y -= marginTop;
            y += marginBottom;
        }
        // right
        else if (corner == Corner.RB) {
            x = sw;
            x += marginLeft;
            x -= marginRight;
            y = 0;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.RC) {
            x = sw;
            x += marginLeft;
            x -= marginRight;
            y = sh * 0.5f;
            y -= marginTop;
            y += marginBottom;
        } else if (corner == Corner.RT) {
            x = sw;
            x += marginLeft;
            x -= marginRight;
            y = sh;
            y -= marginTop;
            y += marginBottom;
        }
        
        store.setX(x);
        store.setY(y);
        
        return store;
    }
    
}
