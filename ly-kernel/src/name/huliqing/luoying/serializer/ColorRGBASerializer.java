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
