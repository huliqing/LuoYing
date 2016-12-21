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
package name.huliqing.luoying.object.effect;

import com.jme3.scene.Spatial;
import name.huliqing.luoying.object.Loader;

/**
 * 用一个模型作为一个效果
 * @author huliqing
 */
public class ModelEffect extends Effect {

    private Spatial model;
    
    @Override
    public void initEntity() {
        super.initEntity();
        model = Loader.loadModel(data.getAsString("file"));
        animNode.attachChild(model);
    }

    @Override
    public void cleanup() {
        model.removeFromParent();
        super.cleanup(); 
    }
    
}
