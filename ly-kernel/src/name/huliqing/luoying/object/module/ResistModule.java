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
package name.huliqing.luoying.object.module;

import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.resist.Resist;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public class ResistModule extends AbstractModule {

    private SafeArrayList<Resist> resists;
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        if (resists != null) {
            for (Resist r : resists.getArray()) {
                r.updateDatas();
            }
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        List<ResistData> rds = entity.getData().getObjectDatas(ResistData.class, new ArrayList<ResistData>());
        for (ResistData rd : rds) {
            addResistInner(rd);
        }
    }

    @Override
    public void cleanup() {
        if (resists != null) {
            for (Resist r : resists.getArray()) {
                r.cleanup();
            }
            resists.clear();
        }
        super.cleanup(); 
    }

    @Override
    public boolean handleDataAdd(ObjectData hData, int amount) {
        if (!(hData instanceof ResistData)) 
            return false;
            
        // 移除旧的,抗性不允许重复。
        removeResistInner((ResistData) hData);
        
        // 增加新的
        addResistInner((ResistData) hData);
        return true;
    }
    
    @Override
    public boolean handleDataRemove(ObjectData hData, int amount) {
        if (!(hData instanceof ResistData)) 
            return false;
            
        return removeResistInner((ResistData) hData);
    }
    
    @Override
    public boolean handleDataUse(ObjectData hData) {
        return false;
    }
    
    /**
     * 获取对某个状态的抗性值。
     * @param state
     * @return 
     */
    public float getResist(String state) {
        if (resists == null || resists.isEmpty()) {
            return 0;
        }
        // 从所有可以抵抗指定状态的抗性中找出一个抵抗值最大的
        float maxValue = 0;
        for (Resist r : resists.getArray()) {
            if (r.isResistState(state) && r.getResist() > maxValue) {
                maxValue = r.getResist();
            }
        }
        return maxValue;
    }
    
//    /**
//     * 移除对指定状态的抵抗能力,这个方法会从所有的抗性中移除对指定状态的抵抗能力。
//     * @param state 
//     */
//    private void removeResistState(String state) {
//        if (resists == null || resists.isEmpty()) 
//            return;
//        
//        for (Resist r : resists.getArray()) {
//            r.removeState(state);
//        }
//    }
    
    private void addResistInner(ResistData newRd) {
        if (resists == null) {
            resists = new SafeArrayList<Resist>(Resist.class);
        }
        Resist newR = Loader.load(newRd);
        entity.getData().addObjectData(newRd);
        resists.add(newR);
        newR.setEntity(entity);
        newR.initialize();
    }
    
    private boolean removeResistInner(ResistData rdRemove) {
        if (resists == null || resists.isEmpty()) {
            return false;
        }
        for (Resist resist : resists.getArray()) {
            if (resist.getData().getUniqueId() == rdRemove.getUniqueId()) {
                entity.getData().removeObjectData(resist.getData());
                resists.remove(resist);
                resist.cleanup();
                return true;
            }
        }
        return false;
    }

}
