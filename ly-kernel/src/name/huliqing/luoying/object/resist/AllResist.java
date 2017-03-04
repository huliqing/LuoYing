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

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ResistData;

/**
 * 除了excepts所示例表之外，所有其它状态都一律抵抗
 * @author huliqing
 */
public class AllResist extends AbstractResist {

    /**
     * 例外的，不进行抵抗的属性
     */
    private List<String> excepts;
    
    // ---- inner
    private boolean statesChanged;
    
    @Override
    public void setData(ResistData data) {
        super.setData(data);
        excepts = data.getAsStringList("excepts");
    }
    
    @Override
    public void updateDatas() {
        if (statesChanged && excepts != null && excepts.size() > 0) {
            data.setAttributeStringList("excepts", excepts);
        }
    }

    @Override
    public boolean isResistState(String state) {
        if (excepts != null && excepts.contains(state)) {
            return false;
        }
        return true;
    }

    @Override
    public void addState(String state) {
        if (excepts != null) {
            excepts.remove(state);
            statesChanged = true;
        }
    }

    @Override
    public boolean removeState(String state) {
        if (excepts == null) {
            excepts = new ArrayList<String>();
        }
        if (!excepts.contains(state)) {
            excepts.add(state);
            statesChanged = true;
            return true;
        }
        return false;
    }
    
}
