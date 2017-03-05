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
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huliqing
 */
@Serializable
public class DropData extends ObjectData {
    
    // 必须掉落的物品列表。
    private List<DropItem> baseItems;

    // 可随机掉落的物品列表
    private List<DropItem> randomItems;

    public DropData() {}
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        if (baseItems != null)
            oc.writeSavableArrayList(new ArrayList<DropItem>(baseItems), "baseItems", null);
        if (randomItems != null)
            oc.writeSavableArrayList(new ArrayList<DropItem>(randomItems), "randomItems", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        baseItems = ic.readSavableArrayList("baseItems", null);
        randomItems = ic.readSavableArrayList("randomItems", null);
    }

    public List<DropItem> getBaseItems() {
        return baseItems;
    }

    public void setBaseItems(List<DropItem> baseItems) {
        this.baseItems = baseItems;
    }

    public List<DropItem> getRandomItems() {
        return randomItems;
    }

    public void setRandomItems(List<DropItem> randomItems) {
        this.randomItems = randomItems;
    }
  
    /**
     * 设置必然掉落的物品ID列表，默认的掉落数量都为1。
     * @param objectIds 
     */
    public void setBaseDrop(String... objectIds) {
        if (baseItems == null) {
            baseItems = new ArrayList<DropItem>(objectIds.length);
        }
        baseItems.clear();
        for (String id : objectIds) {
            DropItem di = new DropItem(id, 1, 1);
            baseItems.add(di);
        }
    }
    
    /**
     * 添加“必然”掉落的物品id,可指定掉落数量
     * @param id 物品id
     * @param count 掉落数量
     */
    public void addBaseDrop(String id, int count) {
        if (baseItems == null) {
            baseItems = new ArrayList<DropItem>(1);
        }
        DropItem di = new DropItem(id, count, 1);
        baseItems.add(di);
    }
  
}
