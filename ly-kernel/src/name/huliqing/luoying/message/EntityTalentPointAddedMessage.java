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
 * 实体获得天赋点数的消息。
 * @author huliqing
 */
public class EntityTalentPointAddedMessage extends EntityMessage {

    private final int talentPointsAdded;
    
    public EntityTalentPointAddedMessage(int stateCode, String message, Entity entity, int talentPointsAdded) {
        super(stateCode, message, entity);
        this.talentPointsAdded = talentPointsAdded;
    }

    /**
     * 获得<b>新增</b>的天赋点数。
     * @return 
     */
    public int getTalentPointsAdded() {
        return talentPointsAdded;
    }
    
}
