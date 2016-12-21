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
package name.huliqing.ly.view.transfer;

import java.util.List;
import name.huliqing.ly.view.ItemPanel;
import name.huliqing.luoying.ui.Row;
import name.huliqing.ly.view.ItemList.RowClickListener;
import name.huliqing.luoying.transfer.Transfer;
import name.huliqing.luoying.transfer.TransferData;

/**
 * @author huliqing
 */
public class SimpleTransferPanel extends TransferPanel {
    private ItemPanel itemPanel;
    
    public SimpleTransferPanel(float width, float height) {
        super(width, height);
        
        itemPanel = new ItemPanel(width, height, getDatas());
        itemPanel.setRowClickListener(new RowClickListener() {
            @Override
            public void onClick(Row row, boolean isPressed, TransferData data) {
                if (isPressed) return;
                transfer(data);
            }
        });
        addView(itemPanel);
    }

    @Override
    public void setDatas(List<TransferData> datas) {
        super.setDatas(datas); 
        itemPanel.refresh();
    }
    
    @Override
    public void onAdded(Transfer transfer, TransferData data, int count) {
        itemPanel.refresh();
    }

    @Override
    public void onRemoved(Transfer transfer, TransferData data, int count) {
        itemPanel.refresh();
    }
    
}
