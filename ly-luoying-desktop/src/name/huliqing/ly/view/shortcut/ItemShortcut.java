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
package name.huliqing.ly.view.shortcut;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.luoying.object.entity.DataListener;

/**
 * 用于普通物品(Item)的快捷方式
 * @author huliqing
 */
public class ItemShortcut extends BaseUIShortcut<ItemData> implements DataListener {
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
        
    @Override
    public void initialize() {
        super.initialize();
        entity.addDataListener(this);
    }

    @Override
    public void cleanup() {
        entity.removeDataListener(this);
        super.cleanup(); 
    }
    
    @Override
    public void removeObject() {
        gameNetwork.removeObjectData(entity, objectData.getUniqueId(), objectData.getTotal());
    }
    
    @Override
    public void onShortcutClick(boolean pressed) {
        if (!pressed) {
            // 一些物品在执行前必须设置目标对象。
            // 注意：这个方法必须放在这里，playService.getTarget()是获取当前游戏主目标，是“玩家行为”，不能把它
            // 放到skillNetwork.playSkill中去。
            Entity target = gameService.getTarget();
            if (target != null) {
                gameNetwork.setTarget(entity, target.getEntityId());
            }
            gameNetwork.useObjectData(entity, objectData.getUniqueId());
        }
    }
    
    @Override
    public void onDataAdded(ObjectData data, int amount) {
        if (!data.getId().equals(objectData.getId()))
            return;
        ItemData temp = entity.getData().getObjectData(objectData.getId());
        if (temp == null) {
            this.objectData.setTotal(0);
        } else if (temp != this.objectData) {
            this.objectData = temp;
        }
        updateObjectData();
    }

    @Override
    public void onDataRemoved(ObjectData data, int amount) {
        if (!data.getId().equals(objectData.getId()))
            return;
        
        ItemData temp = entity.getData().getObjectData(objectData.getId());
        if (temp == null) {
            this.objectData.setTotal(0);
        } else if (temp != this.objectData) {
            this.objectData = temp;
        }
        updateObjectData();
    }

    @Override
    public void onDataUsed(ObjectData data) {
        // ignore
    }
    
    
}
