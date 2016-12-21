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

/**
 *
 * @author huliqing
 */
@Serializable
public class SavableArray implements Savable {

    private static final int   BYTE      = 0;
    private static final int   INTEGER      = 1;
    private static final int   FLOAT        = 2;
    private static final int   LONG         = 3;
    private static final int   BOOLEAN      = 4;
    private static final int   STRING       = 5;
    
    private byte type;
    private Object value;
    
    public SavableArray() {}
    
    public SavableArray(Object value) {
        this.type = getObjectType(value);
        this.value = value;
    }
    
    public Object getValue() {
        return value;
    }
    
    public static byte getObjectType(Object value) {
        if (value instanceof byte[]) {
            return BYTE;
        } else if (value instanceof int[]) {
            return INTEGER;
        } else if (value instanceof float[]) {
            return FLOAT;
        } else if (value instanceof long[]) {
            return LONG;
        } else if (value instanceof boolean[]) {
            return BOOLEAN;
        } else if (value instanceof String[]) {
            return STRING;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + value.getClass().getName());
        }
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(type, "t", (byte) 0);
        switch (type) {
            case BYTE:
                oc.write((byte[]) value, "v", null);
                break;
            case INTEGER:
                oc.write((int[]) value, "v", null);
                break;
            case FLOAT:
                oc.write((float[]) value, "v", null);
                break;
            case LONG:
                oc.write((long[]) value, "v", null);
                break;
            case BOOLEAN:
                oc.write((boolean[]) value, "v", null);
                break;
            case STRING:
                oc.write((String[]) value, "v", null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown type of stored data: " + type);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        type = ic.readByte("t", (byte) 0);
        switch (type) {
            case BYTE:
                value = ic.readByteArray("v", null);
                break;
            case INTEGER:
                value = ic.readIntArray("v", null);
                break;
            case FLOAT:
                value = ic.readFloatArray("v", null);
                break;
            case LONG:
                value = ic.readLongArray("v", null);
                break;
            case BOOLEAN:
                value = ic.readBooleanArray("v", null);
                break;
            case STRING:
                value = ic.readStringArray("v", null);
                break;
            default :
                throw new UnsupportedOperationException("Unknown type of stored data: " + type);
        }
    }
    
}
