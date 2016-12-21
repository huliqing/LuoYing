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
package name.huliqing.luoying.object.resist;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.object.Loader;

/**
 * GroupResist只用于打包
 * @author huliqing
 */
public class GroupResist extends AbstractResist {
    private final EntityService entityService = Factory.get(EntityService.class);

    private boolean applied;
    private long[] appliedIds;

    @Override
    public void setData(ResistData data) {
        super.setData(data);
        applied = data.getAsBoolean("_applied", applied);
        appliedIds = data.getAsLongArray("_appliedIds");
    }
    
    @Override
    public void updateDatas() {
        data.setAttribute("_applied", applied);
        data.setAttribute("_appliedIds", appliedIds);
    }
    
    @Override
    public void initialize() {
        super.initialize(); 
        if (applied) {
            return;
        }
        
        // 格式："resist1|value, resist2|value,..."
        String[] tempResists = data.getAsArray("resists");
        if (tempResists != null && tempResists.length > 0) {
            appliedIds = new long[tempResists.length];
            for (int i = 0; i < tempResists.length; i++) {
                String[] tempArr = tempResists[i].split("\\|");
                ResistData resistData = Loader.loadData(tempArr[0]);
                if (tempArr.length > 1) {
                    resistData.setValue(Float.parseFloat(tempArr[1]));
                }
                entityService.addObjectData(entity, resistData, 1);
                // 记住通过GroupResist添加的抗性,在退出时要清理掉。
                appliedIds[i] = resistData.getUniqueId();
            }
        }
        applied = true;
    }

    @Override
    public void cleanup() {
        if (applied) {
            if (appliedIds != null) {
                for (long resistId : appliedIds) {
                    entityService.removeObjectData(entity, resistId, 1);
                }
                appliedIds = null;
            }
            applied = false;
        }
        super.cleanup();
    }
    
    @Override
    public float getResist() {
        return 0;
    }

    @Override
    public void addResist(float resist) {
        // ignore
    }

    @Override
    public boolean isResistState(String state) {
        return false;
    }

    @Override
    public void addState(String state) {
        // ignore
    }

    @Override
    public boolean removeState(String state) {
        return false; // ignore
    }
    
}
