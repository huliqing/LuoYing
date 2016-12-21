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
package name.huliqing.luoying.object;

import com.jme3.scene.Spatial;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * 这个类只允许在loader包内调用，在其它包的类不要直接调用该类（除测试用之外）。
 * @author huliqing
 */
public class AssetLoader {
    
    /**
     * 载入模型，但是会根据系统是否使用light进行makeUnshaded
     * @param file
     * @return 
     */
    public static Spatial loadModel(String file) {
        return LuoYing.getAssetManager().loadModel(file);
    }
    
    /**
     * 载入模型，这个方法载入模型时会偿试把模型的材质替换为unshaded的。
     * @param file
     * @return 
     */
    public static Spatial loadModelUnshaded(String file) {
        Spatial model = LuoYing.getAssetManager().loadModel(file);
        GeometryUtils.makeUnshaded(model);
        return model;
    }
    
}
