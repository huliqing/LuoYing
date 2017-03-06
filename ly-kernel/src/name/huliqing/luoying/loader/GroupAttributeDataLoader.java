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
import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.xml.DataLoader;

/**
 * @author huliqing
 */
public class GroupAttributeDataLoader implements DataLoader<AttributeData> {
    
    @Override
    public void load(Proto proto, AttributeData data) {
        String[] attributes = proto.getAsArray("attributes");
        if (attributes != null) {
            List<AttributeData> listData = new ArrayList<AttributeData>(attributes.length);
            for (String aid : attributes) {
                listData.add((AttributeData)Loader.loadData(aid));
            }
            data.setAttributeSavableList("attributes", listData);
        }
    }
    
    
}
