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

/**
 * 默认情况下ObjectData不能直接放基本数据类型的数组，
 * 这个类用于封装基本类型的数组为Savable，以便让ObjectData支持基本类型数组。
 * @author huliqing
 */
@Serializable
public class SavablePrimitiveArray extends SavableWrap {
    
    public final static byte ARRAY_BYTE = 0;
    public final static byte ARRAY_SHORT = 1;
    public final static byte ARRAY_INTEGER = 2;
    public final static byte ARRAY_FLOAT = 3;
    public final static byte ARRAY_LONG = 4;
    public final static byte ARRAY_DOUBLE = 5;
    public final static byte ARRAY_BOOLEAN = 6;
    
    private byte type;
    private Object value;
    
    public SavablePrimitiveArray() {}
    
    public SavablePrimitiveArray(byte type, Object value) {
        if (type < 0 || type > 6) {
            throw new UnsupportedOperationException("Unsupported object type! type=" + type + ", value=" + value);
        }
        this.type = type;
        this.value = value;
    }
    
    @Override
    public Object getValue() {
        return value;
    }
    
    /**
     * 获取基本数组类型，如果是以下数组类型则返回一个类型值，否则返回null.<br/>
     * byte[], short[], int[], float[], long[], double[], boolean[]
     * @param value
     * @return 
     */
    public final static Byte getObjectType(Object value) {
        if (!value.getClass().isArray()) {
            return null;
        }
        if (value instanceof byte[]) {
            return ARRAY_BYTE;
        } else if (value instanceof short[]) {
            return ARRAY_SHORT;
        }  else if (value instanceof int[]) {
            return ARRAY_INTEGER;
        } else if (value instanceof float[]) {
            return ARRAY_FLOAT;
        } else if (value instanceof long[]) {
            return ARRAY_LONG;
        } else if (value instanceof double[]) {
            return ARRAY_DOUBLE;
        } else if (value instanceof boolean[]) {
            return ARRAY_BOOLEAN;
        } else {
            return null;
        }
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(type, "t", (byte) 0);
        switch (type) {
            case ARRAY_BYTE:
                oc.write((byte[]) value, "v", null);
                break;
            case ARRAY_SHORT:
                oc.write((short[]) value, "v", null);
                break;
            case ARRAY_INTEGER:
                oc.write((int[]) value, "v", null);
                break;
            case ARRAY_FLOAT:
                oc.write((float[]) value, "v", null);
                break;
            case ARRAY_LONG:
                oc.write((long[]) value, "v", null);
                break;
            case ARRAY_DOUBLE:
                oc.write((double[]) value, "v", null);
                break;
            case ARRAY_BOOLEAN:
                oc.write((boolean[]) value, "v", null);
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
            case ARRAY_BYTE:
                value = ic.readByteArray("v", null);
                break;
            case ARRAY_SHORT:
                value = ic.readShortArray("v", null);
                break;
            case ARRAY_INTEGER:
                value = ic.readIntArray("v", null);
                break;
            case ARRAY_FLOAT:
                value = ic.readFloatArray("v", null);
                break;
            case ARRAY_LONG:
                value = ic.readLongArray("v", null);
                break;
            case ARRAY_DOUBLE:
                value = ic.readDoubleArray("v", null);
                break;
            case ARRAY_BOOLEAN:
                value = ic.readBooleanArray("v", null);
                break;
            default :
                throw new UnsupportedOperationException("Unknown type of stored data: " + type);
        }
    }
    
}
