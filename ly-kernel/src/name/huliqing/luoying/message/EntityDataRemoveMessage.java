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
package name.huliqing.luoying.message;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 实体移除物品时的消息
 * @author huliqing
 * @param <T>
 */
public class EntityDataRemoveMessage<T extends ObjectData> extends EntityMessage {

    private final T objectData;
    private final int count;
    
    /**
     * @param stateCode 状态码
     * @param message 消息内容
     * @param entity 删除了物品的实体
     * @param objectData 被删除的物品
     * @param count 删除的数量
     */
    public EntityDataRemoveMessage(int stateCode, String message, Entity entity, T objectData, int count) {
        super(stateCode, message, entity);
        this.objectData = objectData;
        this.count = count;
    }

    /**
     * 获取删除的物体
     * @return 
     */
    public T getObjectData() {
        return objectData;
    }

    /**
     * 获取移除的物品数量
     * @return 
     */
    public int getCount() {
        return count;
    }
    
}
