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

import java.util.ArrayList;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.LinearLayout;

/**
 * 水平按钮组
 * @author huliqing
 */
public class ButtonPanel extends LinearLayout {

    private final ArrayList<Button> btns;
    
    public ButtonPanel(float width, float height, String[] buttons) {
        super(width, height);
        setLayout(Layout.horizontal);
        btns = new ArrayList<Button>(buttons.length);
        for (int i = 0; i < buttons.length; i++) {
            Button button = new Button(buttons[i]);
            addView(button);
            btns.add(button);
        }
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        float btnWidth = width / btns.size();
        for (Button btn : btns) {
            btn.setWidth(btnWidth);
            btn.setHeight(height);
        }
    }

    /**
     * 给指定的按钮添加事件
     * @param buttonIndex
     * @param listener 
     */
    public void addClickListener(int buttonIndex, Listener listener) {
        btns.get(buttonIndex).addClickListener(listener);
    }
    
    
}
