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

/**
 * Entity消息类型，用于定义实体添加物品，使用物品，删除物品时的消息。
 * @author huliqing
 */
public class EntityMessage extends AbstractMessage {
    
    private final Entity entity;
    
    /**
     * @param stateCode 消息状态码
     * @param message 消息内容
     * @param entity 消息源实体
     */
    public EntityMessage(int stateCode, String message, Entity entity) {
        super(stateCode, message);
        this.entity = entity;
    }
    
    /**
     * 获取消息的实体源
     * @return 
     */
    public Entity getEntity() {
        return entity;
    }
    
}
