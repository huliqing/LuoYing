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
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.xml.SimpleCloner;

/**
 * 字符串数组Savable包装,这个类借助SavableString,把字符串数组保存成字节。
 * 以避免JME在读取、保存的时候的编码问题。
 */
@Serializable
public final class SavableStringArray extends SavableWrap<String[]> {

    private String[] value;
    
    public SavableStringArray() {}

    public SavableStringArray(String[] value) {
        this.value = value;
    }
    
    @Override
    public String[] getValue() {
        return value;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        if (value != null) {
            ArrayList<SavableString> ss = new ArrayList<SavableString>(value.length);
            for (String s : value) {
                ss.add(new SavableString(s));
            }
            oc.writeSavableArrayList(ss, "strList", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        ArrayList<SavableString> ss = ic.readSavableArrayList("strList", null);
        if (ss != null) {
            value = new String[ss.size()];
            for (int i = 0; i < ss.size(); i++) {
                value[i] = ss.get(i).getValue();
            }
        }
    }
    
    @Override
    public SavableStringArray clone() {
        try {
            SavableStringArray clone = (SavableStringArray) super.clone();
            if (value != null) {
                clone.value = new SimpleCloner().clone(value);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new LuoYingException(e);
        }
    }
}
