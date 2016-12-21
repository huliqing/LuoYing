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
package name.huliqing.luoying.object.drop;

import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * Drop用于处理角色互相伤害后物品、经验等的掉落。当角色A攻击了角色B,并致B死亡时，
 * B可能会掉落各种各样的物品或经验给角色A.
 * @author huliqing
 */
public interface Drop extends DataProcessor<DropData> {
    
    /**
     * 处理掉落物品，物品从source掉落
     * 注意：目标target标记着是谁对source造成了伤害致死，并造成source掉落物品的角色，
     * 这个角色有可能为null的，当source是被一个不实际存在的角色打倒时，如GM、一些未知释放源的魔法、状态致死时，
     * target传入的有可能会是null,当出现这种情况时，根据实现类的情况而定，可以直接将物品掉落到地上，或者不掉落。
     * @param source 掉落源
     * @param target 接受掉落物品的角色,注意：这个目标有可能为null.
     */
    void doDrop(Entity source, Entity target);
}
