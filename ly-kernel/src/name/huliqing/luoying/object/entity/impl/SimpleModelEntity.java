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
package name.huliqing.luoying.object.entity.impl;

import com.jme3.scene.Spatial;
import name.huliqing.luoying.object.AssetLoader;
import name.huliqing.luoying.object.entity.ModelEntity;

/**
 * 普通模型类环境物体,所有不需要归类或不需要特别类型的模型类实体都可以直接继承这个类。
 * @author huliqing
 */
public class SimpleModelEntity extends ModelEntity {

    @Override
    protected Spatial loadModel() {
        return AssetLoader.loadModel(data.getAsString("file"));
    }
    
}
