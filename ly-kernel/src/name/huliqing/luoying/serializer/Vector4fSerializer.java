/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.serializer;

import com.jme3.math.Vector4f;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author huliqing
 */
public class Vector4fSerializer extends Serializer{

    @Override
    public Vector4f readObject(ByteBuffer data, Class c) throws IOException {
        Vector4f vec4 = new Vector4f();
        vec4.x = data.getFloat();
        vec4.y = data.getFloat();
        vec4.z = data.getFloat();
        vec4.w = data.getFloat();
        return vec4;
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        Vector4f vec4 = (Vector4f) object;
        buffer.putFloat(vec4.x);
        buffer.putFloat(vec4.y);
        buffer.putFloat(vec4.z);
        buffer.putFloat(vec4.w);
    }
}
