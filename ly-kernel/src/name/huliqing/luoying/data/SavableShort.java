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
 * 包装基本类型
 */
@Serializable
public final class SavableShort extends SavableWrap<Short> {

    private short value;
    
    public SavableShort() {}

    public SavableShort(short value) {
        this.value = value;
    }
    
    @Override
    public Short getValue() {
        return value;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(value, "v", (short) 0);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        value = ic.readShort("v", (short) 0);
    }
    
    @Override
    public SavableShort clone() {
        try {
            SavableShort clone = (SavableShort) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new LuoYingException(e);
        }
    }
}
