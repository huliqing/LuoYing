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

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataLoader;
import name.huliqing.luoying.xml.Proto;

/**
 *
 * @author huliqing
 */
public class PrivateGroupStateDataLoader implements DataLoader<StateData> {

    @Override
    public void load(Proto proto, StateData data) {
        String[] stateArr = proto.getAsArray("states");
        if (stateArr != null) {
            List<StateData> stateDatas = new ArrayList(stateArr.length);
            for (int i = 0; i < stateArr.length; i++) {
                stateDatas.add((StateData) Loader.loadData(stateArr[i]));
            }
            data.setAttributeSavableList("states", stateDatas);
        }
    }
    
}
