/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.serializer;

import com.jme3.math.Quaternion;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author huliqing
 */
public class QuaternionSerializer extends Serializer{

    @Override
    public Quaternion readObject(ByteBuffer data, Class c) throws IOException {
        float x = data.getFloat();
        float y = data.getFloat();
        float z = data.getFloat();
        float w = data.getFloat();
        return new Quaternion(x,y,z,w);
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        Quaternion qua = (Quaternion) object;
        buffer.putFloat(qua.getX());
        buffer.putFloat(qua.getY());
        buffer.putFloat(qua.getZ());
        buffer.putFloat(qua.getW());
    }
}
