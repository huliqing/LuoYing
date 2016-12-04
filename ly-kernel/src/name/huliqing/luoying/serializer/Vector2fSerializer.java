/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.serializer;

import com.jme3.math.Vector2f;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author huliqing
 */
public class Vector2fSerializer extends Serializer{

    @Override
    public Vector2f readObject(ByteBuffer data, Class c) throws IOException {
        Vector2f vec2 = new Vector2f();
        vec2.x = data.getFloat();
        vec2.y = data.getFloat();
        return vec2;
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        Vector2f vec2 = (Vector2f) object;
        buffer.putFloat(vec2.x);
        buffer.putFloat(vec2.y);
    }
}
