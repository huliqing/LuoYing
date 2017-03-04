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

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.xml.SimpleCloner;

/**
 * SavableArrayList主要用于封装集合类型的数据为一个<b>单独的</b>Savable对象
 */
@Serializable
public class SavableList<T extends Savable> extends SavableWrap<List<T>> {
    
    private List<T> list;
    
    public SavableList() {}
    
    public SavableList(List<T> list) {
        this.list = list;
    }
    
    @Override
    public List<T> getValue() {
        return list;
    }

    @Override
    public SavableList clone() {
        try {
            SavableList clone = (SavableList) super.clone();
            clone.list = SimpleCloner.deepClone(list);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new LuoYingException(e);
        }
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        if (list != null) {
            oc.writeSavableArrayList(new ArrayList<T>(list), "list", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        list = ic.readSavableArrayList("list", null);
    }

}
