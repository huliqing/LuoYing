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
 * 普通的抗性设置。
 * @author huliqing
 */
public class SimpleResist extends AbstractResist {
    
    private List<String> states;
    
    // ---- inner
    private boolean statesChanged;

    @Override
    public void setData(ResistData data) {
        super.setData(data);
        states = data.getAsStringList("states");
    }
    
    @Override
    public void updateDatas() {
        if (statesChanged && states != null && states.size() > 0) {
            data.setAttributeStringList("states", states);
        }
    }
    
    @Override
    public boolean isResistState(String state) {
        return states != null && states.contains(state);
    }
    
    @Override
    public void addState(String state) {
        if (states == null) {
            states = new ArrayList<String>();
        }
        if (!states.contains(state)) {
            states.add(state);
            statesChanged = true;
        }
    }

    @Override
    public boolean removeState(String state) {
        if (states != null && states.remove(state)) {
            statesChanged = true;
            return true;
        }
        return false;
    }
    
}
