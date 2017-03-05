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
package name.huliqing.luoying.serializer;

import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.BooleanSerializer;
import com.jme3.network.serializing.serializers.ByteSerializer;
import com.jme3.network.serializing.serializers.DoubleSerializer;
import com.jme3.network.serializing.serializers.FloatSerializer;
import com.jme3.network.serializing.serializers.IntSerializer;
import com.jme3.network.serializing.serializers.LongSerializer;
import com.jme3.network.serializing.serializers.ShortSerializer;
import java.io.IOException;
import java.nio.ByteBuffer;
import name.huliqing.luoying.data.SavablePrimitiveArray;

/**
 * 该类用于序列化SavablePrimitiveArray数据类型，因为SavablePrimitiveArray中的数据类型为基本类型数组。
 * JME没有内置对基本类型数组的支持，不能通过简单添加@Serializable来实现支持.
 * @author huliqing
 */
public class SavablePrimitiveArraySerializer extends Serializer {

    @Override
    public SavablePrimitiveArray readObject(ByteBuffer data, Class c) throws IOException {
        byte type = data.get();
        int len = data.getInt();
        
        Serializer ser;
        switch (type) {
            case SavablePrimitiveArray.ARRAY_BOOLEAN:
                ser = new BooleanSerializer();
                boolean[] booleanArr = new boolean[len];
                for (int i = 0; i < len; i++) {
                    ser.readObject(data, c);
                }
                return new SavablePrimitiveArray(type, booleanArr);
                
            case SavablePrimitiveArray.ARRAY_BYTE:
                ser = new ByteSerializer();
                byte[] byteArr = new byte[len];
                for (int i = 0; i < len; i++) {
                    ser.readObject(data, c);
                }
                return new SavablePrimitiveArray(type, byteArr);

            case SavablePrimitiveArray.ARRAY_SHORT:
                ser = new ShortSerializer();
                short[] shortArr = new short[len];
                for (int i = 0; i < len; i++) {
                    ser.readObject(data, c);
                }
                return new SavablePrimitiveArray(type, shortArr);

            case SavablePrimitiveArray.ARRAY_INTEGER:
                ser = new IntSerializer();
                int[] intArr = new int[len];
                for (int i = 0; i < len; i++) {
                    ser.readObject(data, c);
                }
                return new SavablePrimitiveArray(type, intArr);

            case SavablePrimitiveArray.ARRAY_FLOAT:
                ser = new FloatSerializer();
                float[] floatArr = new float[len];
                for (int i = 0; i < len; i++) {
                    ser.readObject(data, c);
                }
                return new SavablePrimitiveArray(type, floatArr);

            case SavablePrimitiveArray.ARRAY_LONG:
                ser = new LongSerializer();
                long[] longArr = new long[len];
                for (int i = 0; i < len; i++) {
                    ser.readObject(data, c);
                }
                return new SavablePrimitiveArray(type, longArr);

            case SavablePrimitiveArray.ARRAY_DOUBLE:
                ser = new DoubleSerializer();
                double[] doubleArr = new double[len];
                for (int i = 0; i < len; i++) {
                    ser.readObject(data, c);
                }
                return new SavablePrimitiveArray(type, doubleArr);

            default:
                throw new UnsupportedOperationException("type=" + type);
        }
        
    }
    
    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        SavablePrimitiveArray ud = (SavablePrimitiveArray) object;
        Object value = ud.getValue();
        byte type = SavablePrimitiveArray.getObjectType(value);
        buffer.put(type);
        
        Serializer ser;
        switch (type) {
            case SavablePrimitiveArray.ARRAY_BOOLEAN:
                ser = new BooleanSerializer();
                boolean[] booleanArr = (boolean[]) value;
                buffer.putInt(booleanArr.length);
                for (boolean v : booleanArr) {
                    ser.writeObject(buffer, v);
                }
                break;
            case SavablePrimitiveArray.ARRAY_BYTE:
                ser = new ByteSerializer();
                byte[] byteArr = (byte[]) value;
                buffer.putInt(byteArr.length);
                for (byte v : byteArr) {
                    ser.writeObject(buffer, v);
                }
                break;
            case SavablePrimitiveArray.ARRAY_SHORT:
                ser = new ShortSerializer();
                short[] shortArr = (short[]) value;
                buffer.putInt(shortArr.length);
                for (short v : shortArr) {
                    ser.writeObject(buffer, v);
                }
                break;
            case SavablePrimitiveArray.ARRAY_INTEGER:
                ser = new IntSerializer();
                int[] intArr = (int[]) value;
                buffer.putInt(intArr.length);
                for (int v : intArr) {
                    ser.writeObject(buffer, v);
                }
                break;
            case SavablePrimitiveArray.ARRAY_FLOAT:
                ser = new FloatSerializer();
                float[] floatArr = (float[]) value;
                buffer.putInt(floatArr.length);
                for (float v : floatArr) {
                    ser.writeObject(buffer, v);
                }
                break;
            case SavablePrimitiveArray.ARRAY_LONG:
                ser = new LongSerializer();
                long[] longArr = (long[]) value;
                buffer.putInt(longArr.length);
                for (long v : longArr) {
                    ser.writeObject(buffer, v);
                }
                break;
            case SavablePrimitiveArray.ARRAY_DOUBLE:
                ser = new DoubleSerializer();
                double[] doubleArr = (double[]) value;
                buffer.putInt(doubleArr.length);
                for (double v : doubleArr) {
                    ser.writeObject(buffer, v);
                }
                break;
            default:
                throw new UnsupportedOperationException("type=" + type);
        }
    }
    
}
