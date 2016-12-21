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
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.DataLoader;

/**
 * 用于载入场景数据
 * @author huliqing
 * @param <T>
 */
public class SceneDataLoader<T extends SceneData> implements DataLoader<T> {

    @Override
    public void load(Proto proto, T store) {        
        // 环境物体
        String[] envIds = proto.getAsArray("entities");
        if (envIds != null && envIds.length > 0) {
            List<EntityData> edStore = store.getEntityDatas();
            if (edStore == null) {
                edStore = new ArrayList<EntityData>(envIds.length);
                store.setEntityDatas(edStore);
            }
            for (String eid : envIds) {
                EntityData ed = DataFactory.createData(eid);
                edStore.add(ed);
            }
        }
    }
    
}
