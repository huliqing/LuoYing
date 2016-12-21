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

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 武器面板列表
 * @author huliqing
 */
public class WeaponPanel extends ListView<SkinData> implements ActorPanel{
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    
    private Entity actor;
    private final List<SkinData> datas = new ArrayList<SkinData>();
    
    public WeaponPanel(float width, float height) {
        super(width, height);
    }

    @Override
    protected Row createEmptyRow() {
        final WeaponRow row = new WeaponRow();
        row.setRowClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    gameNetwork.useObjectData(actor, row.getData().getUniqueId());
                    refreshPageData();
                }
            }
        });
        row.setShortcutListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    gameService.addShortcut(actor, row.getData());
                }
            }
        });
        return row;
    }

    @Override
    public void setPanelVisible(boolean visible) {
        this.setVisible(visible);
    }

    @Override
    public void setPanelUpdate(Entity actor) {
        this.actor = actor;
        getDatas();
        super.refreshPageData();
    }

    @Override
    public List<SkinData> getDatas() {
        if (actor != null) {
            datas.clear();
            List<SkinData> skins = actor.getData().getObjectDatas(SkinData.class, null);
            if (skins != null && !skins.isEmpty()) {
                for (SkinData s : skins) {
                    if (filter(s)) {
                        continue;
                    }
                    datas.add(s);
                }
            }
        }
        return datas;
    }

    @Override
    protected boolean filter(SkinData data) {
        if (data.isBaseSkin()) {
            return true;
        }
        if (data.isWeapon()) {
            return false;
        }
        return true;
    }
  
    @Override
    public boolean removeItem(SkinData data) {
        throw new UnsupportedOperationException();
    }
    
    private class WeaponRow extends ItemRow<SkinData> {
        
        @Override
        public void display(SkinData skinData) {
            icon.setIcon(skinData.getIcon());
            body.setNameText(ResourceManager.getObjectName(skinData));
            body.setDesText(SkinUtils.getSkinDes(skinData));
            num.setText(String.valueOf(skinData.getTotal()));
            setBackgroundVisible(data.isUsed());
        }
        
        @Override
        protected void clickEffect(boolean isPress) {
            super.clickEffect(isPress);
            setBackgroundVisible(data.isUsed());
        }
        
        @Override
        public void onRelease() {
            setBackgroundVisible(data.isUsed());
        }
    }
}
