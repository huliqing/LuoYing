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
import java.util.List;
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.xml.SimpleCloner;

/**
 * 字符串列表Savable包装,这个类借助SavableString,把字符串数组保存成字节。
 * 以避免JME在读取、保存的时候的编码问题。
 */
@Serializable
public final class SavableStringList extends SavableWrap<List<String>> {

    private List<String> value;
    
    public SavableStringList() {}

    public SavableStringList(List<String> value) {
        this.value = value;
    }
    
    @Override
    public List<String> getValue() {
        return value;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        if (value != null) {
            ArrayList<SavableString> ss = new ArrayList<SavableString>(value.size());
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
            value = new ArrayList<String>();
            for (SavableString s : ss) {
                value.add(s.getValue());
            }
        }
    }
    
    @Override
    public SavableStringList clone() {
        try {
            SavableStringList clone = (SavableStringList) super.clone();
            if (value != null) {
                clone.value = new SimpleCloner().clone(value);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new LuoYingException(e);
        }
    }
}
