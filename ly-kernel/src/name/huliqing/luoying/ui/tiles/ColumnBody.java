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
import com.jme3.math.ColorRGBA;
import java.util.List;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;

/**
 *
 * @author huliqing
 */
public class ColumnBody extends LinearLayout {
    
    private Text nameText;
    private Text desText;
    // 黄色
    private final ColorRGBA disableColor = new ColorRGBA(1f, 0.5f, 0, 1);
    
    public ColumnBody(float width, float height, String nameText, String desText) {
        super(width, height);
        this.nameText = new Text(nameText);
        this.desText = new Text(desText);
        this.desText.setFontColor(UIFactory.getUIConfig().getDesColor());
        this.desText.setFontSize(UIFactory.getUIConfig().getDesSize());
        this.addView(this.nameText);
        this.addView(this.desText);
    }
    
    public void setNameText(String name) {
        this.nameText.setText(name);
    }
    
    public void setDesText(String des) {
        this.desText.setText(des);
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        List<UI> cui = getViews();
        
        float textLimitHeight = height * 0.499f;
        float fontSize = UIFactory.getUIConfig().getBodyFontSize();
        for (UI child : cui) {
            Text text = (Text) child;
            text.setWidth(width * 0.999f);
            text.setHeight(textLimitHeight);
            
            text.setFontSize(fontSize > textLimitHeight ? textLimitHeight : fontSize);
            text.setVerticalAlignment(BitmapFont.VAlign.Center);
        }
    }
    
    public void setDisabled(boolean disabled) {
        if (disabled) {
            nameText.setFontColor(disableColor);
        } else {
            nameText.setFontColor(ColorRGBA.White);
        }
    }
    
}

