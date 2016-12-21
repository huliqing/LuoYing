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
package name.huliqing.ly.view.system;

import com.jme3.font.BitmapFont;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class RowSimple extends Row<SystemData> {

    private Text rowName;
    private Text rowDes;
    
    public RowSimple(ListView listView, String name, String des) {
        super(listView);
        this.rowName = new Text(name);
        this.rowName.setFontSize(UIFactory.getUIConfig().getBodyFontSize());
        this.rowDes = new Text(des);
        this.rowDes.setFontSize(UIFactory.getUIConfig().getDesSize());
        this.rowDes.setFontColor(UIFactory.getUIConfig().getDesColor());
        
        addView(this.rowName);
        addView(this.rowDes);
        
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        setBackgroundVisible(false);
    }

    public void setRowName(String rowName) {
        this.rowName.setText(rowName);
    }

    public void setRowDes(String rowDes) {
        this.rowDes.setText(rowDes);
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren(); 
        float mw = 5;
        float rowW = width - mw * 2;
        float rowH = height * 0.5f;
        
        this.rowName.setWidth(rowW);
        this.rowName.setHeight(rowH);
        this.rowName.setMargin(mw, 0, 0, 0);
        this.rowName.setAlignment(BitmapFont.Align.Left);
        this.rowName.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        this.rowDes.setWidth(rowW);
        this.rowDes.setHeight(rowH);
        this.rowDes.setMargin(mw, 0, 0, 0);
        this.rowDes.setAlignment(BitmapFont.Align.Left);
        this.rowDes.setVerticalAlignment(BitmapFont.VAlign.Center);
    }
    
    @Override
    protected void clickEffect(boolean isPress) {
        if (isPress) {
            setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        }
        setBackgroundVisible(isPress);
    }

    @Override
    public void onRelease() {
        setBackgroundVisible(false);
    }
    
    @Override
    public void displayRow(SystemData data) {
        // ignore
    }
    
    
    
}
