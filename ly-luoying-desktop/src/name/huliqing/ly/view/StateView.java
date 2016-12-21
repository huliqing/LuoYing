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

import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.Icon;

/**
 * StateView显示状态图标
 * @author huliqing
 */
public class StateView extends FrameLayout {
    
    private StateData state;
    
    private Icon icon;
    
    public StateView(float width, float height) {
        super(width, height);
    }
    
    public final void setState(StateData state) {
        // 如果是同一个state,则不需要重设
        if (state == this.state) {
            return;
        }
        this.state = state;
        if (icon == null) {
            icon = new Icon();
            addView(icon);
        }
        icon.setImage(state.getIcon());
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        icon.setWidth(width);
        icon.setHeight(height);
    }
    
}
