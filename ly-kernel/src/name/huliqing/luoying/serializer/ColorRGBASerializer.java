/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.serializer;

import com.jme3.math.ColorRGBA;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author huliqing
 */
public class ColorRGBASerializer extends Serializer{

    @Override
    public ColorRGBA readObject(ByteBuffer data, Class c) throws IOException {
        float r = data.getFloat();
        float g = data.getFloat();
        float b = data.getFloat();
        float a = data.getFloat();
        return new ColorRGBA(r,g,b,a);
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        ColorRGBA c = (ColorRGBA) object;
        buffer.putFloat(c.r);
        buffer.putFloat(c.g);
        buffer.putFloat(c.b);
        buffer.putFloat(c.a);
    }
    
}
