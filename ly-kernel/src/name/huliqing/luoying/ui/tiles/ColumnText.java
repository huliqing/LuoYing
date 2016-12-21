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

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class ColumnText extends FrameLayout {
    
    private Text text;
    
    public ColumnText(float width, float height, String text) {
        super(width, height);
        this.text = new Text(text);
        this.text.setWidth(width);
        this.text.setHeight(height);
        this.text.setAlignment(BitmapFont.Align.Center);
        this.text.setVerticalAlignment(BitmapFont.VAlign.Center);
        this.addView(this.text);
    }
    
    public void setText(String text) {
        this.text.setText(text);
    }
    
    public void setAlignment(Align aligh) {
        this.text.setAlignment(aligh);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        this.text.setWidth(width);
        this.text.setHeight(height);
        float fontSize = UIFactory.getUIConfig().getBodyFontSize();
        if (fontSize > height) {
            this.text.setFontSize(height * 0.4f);
        } else {
            this.text.setFontSize(fontSize);
        }
    }
    
}
