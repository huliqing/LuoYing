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

import java.util.List;
import name.huliqing.luoying.transfer.TransferData;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class ItemPanel extends LinearLayout {
    private final ItemTitle title;
    private final ItemList itemList;
        
    public ItemPanel(float width, float height, List<TransferData> datas) {
        super(width, height);
        float titleHeight = UIFactory.getUIConfig().getListTitleHeight();
        title = new ItemTitle(width, titleHeight);
        itemList = new ItemList(width, height - titleHeight, datas);
        addView(title);
        addView(itemList);
    }
        
    public void setRowClickListener(ItemList.RowClickListener rcl) {
        itemList.setRowClickListener(rcl);
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        float titleHeight = UIFactory.getUIConfig().getListTitleHeight();
        itemList.setWidth(width);
        itemList.setHeight(height - titleHeight);
        itemList.updateView();
        title.setWidth(width);
        title.setHeight(titleHeight);
        title.setColumnsWidth(itemList.getColumnsWidth());
    }
    
    public void refresh() {
        itemList.refreshPageData();
    }
}
