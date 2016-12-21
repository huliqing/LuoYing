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
import com.jme3.math.ColorRGBA;
import name.huliqing.luoying.ui.Checkbox;
import name.huliqing.luoying.ui.Checkbox.ChangeListener;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;

/**
 *
 * @author huliqing
 */
public class SimpleCheckbox extends LinearLayout {

    private Checkbox checkbox; // 如果为true，则过滤掉已经完成的任务
    private Text label;
    
    public SimpleCheckbox(String labelText) {
        super(128, 32);
        checkbox = new Checkbox();
        label = new Text(labelText);
        label.setVerticalAlignment(BitmapFont.VAlign.Center);
        label.addClickListener(new Listener() {
            @Override
            public void onClick(UI view, boolean isPressed) {
                if (isPressed) return;
                checkbox.setChecked(!checkbox.isChecked());
            }
        });
        setLayout(Layout.horizontal);
        addView(checkbox);
        addView(label);
    }
    
    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        checkbox.setWidth(height);
        checkbox.setHeight(height);
        label.setHeight(height);
        label.setMargin(5, 0, 0, 0);
    }
    
    public void setLabel(String label) {
        this.label.setText(label);
    }
    
    public boolean isChecked() {
        return checkbox.isChecked();
    }
    
    public void setChecked(boolean checked) {
        checkbox.setChecked(checked);
    }
    
    public void setFontColor(ColorRGBA color) {
        label.setFontColor(color);
    }
    
    public void setFontSize(float size) {
        label.setFontSize(size);
    }
    
    public void addChangeListener(ChangeListener listener) {
        checkbox.addChangeListener(listener);
    }
    
    public boolean removeChangeListener(Checkbox.ChangeListener listener) {
        return checkbox.removeChangeListener(listener);
    }
}
