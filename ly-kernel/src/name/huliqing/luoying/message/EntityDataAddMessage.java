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
 * 实体添加物品时的消息
 * @author huliqing
 * @param <T>
 */
public class EntityDataAddMessage<T extends ObjectData> extends EntityMessage {

    private final T objectData;
    private final int count;
    
    /**
     * @param stateCode
     * @param message
     * @param entity
     * @param objectData
     * @param count 获得的物体数量
     */
    public EntityDataAddMessage(int stateCode, String message, Entity entity, T objectData, int count) {
        super(stateCode, message, entity);
        this.objectData = objectData;
        this.count = count;
    }

    /**
     * 获取添加的物体
     * @return 
     */
    public T getObjectData() {
        return objectData;
    }
    
    /**
     * 获得的物体数量
     * @return 
     */
    public int getCount() {
        return count;
    }
    
    
}
