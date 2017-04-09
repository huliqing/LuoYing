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
package name.huliqing.luoying.loader;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.utils.MathUtils;

/**
 * 植被数据载入器，这个类主要用于树森或花草等env的载入，为这些植被模型随机
 * 生成高低、粗壮不同的缩放大小，可使场景中的各种植被有大小不一的感觉。
 * @author huliqing
 */
public class PlantEnvLoader extends EntityDataLoader {
 
    @Override
    public void load(Proto proto, EntityData store) {
        super.load(proto, store);
        
        boolean randomScale = proto.getAsBoolean("randomScale", false);
        if (randomScale) {
            float minScale = proto.getAsFloat("minScale", 1);
            float maxScale = proto.getAsFloat("maxScale", 1);
            float finalScale = MathUtils.getRandomFloat(minScale, maxScale);
            store.setScale(new Vector3f(finalScale,finalScale,finalScale));
        }
    }
    
    
}
