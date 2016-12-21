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

import com.jme3.math.FastMath;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 掉落普通物品设置 
 * @author huliqing
 */
public class ItemDrop extends AbstractDrop {
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    private String item;
    private int count;
    /** 掉落率: 取值0.0~1.0 */
    private float rate;
    
    @Override
    public void setData(DropData data) {
        super.setData(data);
        item = data.getAsString("item");
        count = data.getAsInteger("count", 1);
        rate = MathUtils.clamp(data.getAsFloat("rate", 1.0f), 0f, 1.0f);
    }
    
    // 注意：因为这里涉及到机率，所以要使用network版本（***Network.addData）
    // // 这里使用ProtoNetwork就可以，不需要直接使用ItemNetwork
    @Override
    public void doDrop(Entity source, Entity target) {
        if (target == null) {
            return;
        }
        if (item == null || count <= 0 || rate <= 0) {
            return;
        }
        
        // 注：如果rate>=1.0, 则忽略其它设置(dropFactor)的影响，把物品视为始终掉落的。
        if (rate >= 1.0f) {
            entityNetwork.addObjectData(target, Loader.loadData(item), count);
            playDropSounds(source);
            return;
        }
        
        // 按机率掉落
        if (rate >= FastMath.nextRandomFloat()) {
            entityNetwork.addObjectData(target, Loader.loadData(item), count);
            playDropSounds(source);
        }
    }
    
}
