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
package name.huliqing.ly.view.actor;

import name.huliqing.luoying.ui.tiles.ColumnBody;
import name.huliqing.luoying.ui.tiles.ColumnText;
import name.huliqing.luoying.ui.tiles.ColumnIcon;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.InterfaceConstants;
import name.huliqing.luoying.data.TalentData;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.ly.view.SimpleRow;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.ListView;

/**
 * 
 * @author huliqing
 */
public class TalentRow extends SimpleRow<TalentData> {
    protected PlayService playService = Factory.get(PlayService.class);
    
    protected TalentData data;
    
    // 物品
    protected ColumnIcon icon;
    protected ColumnBody body;
    protected ColumnText num;
    protected ColumnIcon shortcut;
    
    public TalentRow(ListView parent) {
        super(parent);
        this.setLayout(Layout.horizontal);
        icon = new ColumnIcon(height, height, InterfaceConstants.UI_MISS);
        body = new ColumnBody(height, height, "", "");
        num = new ColumnText(height, height, "");
        shortcut = new ColumnIcon(height, height, InterfaceConstants.UI_UP);
        addView(icon);
        addView(body);
        addView(num);
        addView(shortcut);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float iconSize = height;

        icon.setWidth(iconSize);
        icon.setHeight(iconSize);

        num.setWidth(iconSize);
        num.setHeight(iconSize);

        shortcut.setWidth(iconSize);
        shortcut.setHeight(iconSize);
        shortcut.setPreventEvent(true);

        body.setWidth(width - iconSize * 3);
        body.setHeight(iconSize);
    }

    @Override
    public final void displayRow(TalentData data) {
        this.data = data;
        display(this.data);
    }
    
    public TalentData getData() {
        return this.data;
    }
    
    public void setRowClickListener(Listener listener) {
        addClickListener(listener);
    }
    
    public void setUpListener(Listener listener) {
        shortcut.addClickListener(listener);
    }
    
    protected void display(TalentData td) {
        icon.setIcon(td.getIcon());
        body.setNameText(ResourceManager.getObjectName(td));
        body.setDesText(ResourceManager.getObjectDes(td.getId()));
        num.setText(td.getLevel() + "/" + td.getMaxLevel());
    }
}
