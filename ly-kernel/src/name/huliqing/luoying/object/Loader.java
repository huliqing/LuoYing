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

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.DataProcessor;
import name.huliqing.luoying.xml.ObjectData;

public class Loader {
    
    public static <T extends ObjectData> T loadData(String id) {
        return DataFactory.createData(id);
    }
    
    public static <T extends DataProcessor> T load(String id) {
        return load(DataFactory.createData(id));
    }
    
    public static <T extends DataProcessor> T load(ObjectData data) {
        T dp = DataFactory.createProcessor(data);
        if (dp instanceof Entity) {
            ((Entity) dp).initialize();
        }
        return dp;
    }
    
    public static Spatial loadModel(String file) {
        return AssetLoader.loadModel(file);
    }
    
    public static Material loadMaterial(String j3mFile) {
        AssetManager am = LuoYing.getAssetManager();
        return am.loadMaterial(j3mFile);
    }
}
