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
package name.huliqing.luoying.object.drop;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 掉落组，GroupDrop可以包含多个子掉落设置.
 * @author huliqing
 */
public class GroupDrop extends AbstractDrop {
//    private static final Logger LOG = Logger.getLogger(GroupDrop.class.getName());
    
    private String[] dropIds;
    private List<Drop> drops;
    
    @Override
    public void setData(DropData data) {
        super.setData(data);
        dropIds = data.getAsArray("drops");
    }
    
    @Override
    public void doDrop(Entity source, Entity target) {
        if (drops == null && dropIds != null) {
             if (dropIds != null && dropIds.length > 0) {
                drops = new ArrayList<Drop>(dropIds.length);
                for (String dropId : dropIds) {
                    drops.add((Drop)Loader.load(dropId));
                }
            }
        }
        boolean hasDrop = false;
        if (drops != null) {
            for (int i = 0; i < drops.size(); i++) {
                drops.get(i).doDrop(source, target);
                hasDrop = true;
            }
        }
        if (hasDrop) {
            playDropSounds(source);
        }
    }
    
}
