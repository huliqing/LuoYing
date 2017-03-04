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
import name.huliqing.luoying.LuoYingException;

/**
 * 这个类主要是解决JME在保存中文字符时候的BUG， 一些正常UTF8的中文字符仍然会被错误解读为"ISO8859_1",
 * 这导致保存后再解读的时候乱码，这个类直接把字符串保存成字节码数组，这样就不会导致JME在判断UTF8时候的问题。
 * @author huliqing
 * @see BinaryInputCapsule#readString(byte[])
 */
@Serializable
public final class SavableString extends SavableWrap<String> {

    private String value;
    
    public SavableString() {}

    public SavableString(String value) {
        this.value = value;
    }
    
    @Override
    public String getValue() {
        return value;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        if (value != null) {
            oc.write(value.getBytes(), "v", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        byte[] temp = ic.readByteArray("v", null);
        if (temp != null) {
            value = new String(temp);
        }
    }
    
    @Override
    public SavableString clone() {
        try {
            SavableString clone = (SavableString) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new LuoYingException(e);
        }
    }
}
