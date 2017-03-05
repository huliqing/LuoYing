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
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.xml.SimpleCloner;

/**
 * 封装Savable类型的数组
 * @param <T>
 */
@Serializable
public class SavableArray<T extends Savable> extends SavableWrap<T[]> {
    
    private T[] list;
    
    public SavableArray() {}
    
    public SavableArray(T[] list) {
        this.list = list;
    }
    
    @Override
    public T[] getValue() {
        return list;
    }

    @Override
    public SavableArray clone() {
        try {
            SavableArray clone = (SavableArray) super.clone();
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
            oc.write(list, "val", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        list = (T[]) ic.readSavableArray("val", null);
    }

}
