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
package name.huliqing.ly.object.view;

import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.math.ColorRGBA;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;

/**
 * 用于显示文字信息的界面组件
 * @author huliqing
 */
public class TextView extends AbstractView {
    
    protected Text textUI;

    @Override
    public void setData(EntityData data) {
        super.setData(data); 
        String text = data.getAsString("text");
        String textKey = data.getAsString("textKey");
        ColorRGBA color = data.getAsColor("fontColor");
        float fontSize = data.getAsFloat("fontSize", UIFactory.getUIConfig().getBodyFontSize());
        Align align = identifyAlign(data.getAsString("align"));
        VAlign valign = identifyVAlign(data.getAsString("valign"));
        
        if (text != null) {
            textUI = new Text(text);
        } else if (textKey != null) {
            textUI = new Text(ResourceManager.get(textKey));
        }  else {
            textUI = new Text("");
        }
        if (color != null) {
            textUI.setFontColor(color);
        }
        if (align != null) {
            textUI.setAlignment(align);
        }
        if (valign != null) {
            textUI.setVerticalAlignment(valign);
        }
        textUI.setFontSize(fontSize);
        
        viewRoot.addView(textUI);
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("text", textUI.getText());
    }
    
    @Override
    protected void doViewInit() {
        super.doViewInit();
        textUI.setWidth(viewRoot.getWidth());
        textUI.setHeight(viewRoot.getHeight());
    }
    
    private Align identifyAlign(String align) {
        if (align == null)
            return null;
        
        if (align.equals(Align.Center.name())) {
            return Align.Center;
        }
        if (align.equals(Align.Left.name())) {
            return Align.Left;
        }
        if (align.equals(Align.Right.name())) {
            return Align.Right;
        }
        return null;
    }
    
    private VAlign identifyVAlign(String valign) {
        if (valign == null) 
            return null;
        
        if (valign.equals(VAlign.Bottom.name())) {
            return VAlign.Bottom;
        }
        if (valign.equals(VAlign.Center.name())) {
            return VAlign.Center;
        }
        if (valign.equals(VAlign.Top.name())) {
            return VAlign.Top;
        }
        return null;
    }
    
    public void setText(String text) {
        textUI.setText(text);
    }


    
}
