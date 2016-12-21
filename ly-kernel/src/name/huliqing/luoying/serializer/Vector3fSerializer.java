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

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author huliqing
 */
public class Vector3fSerializer extends Serializer{

    @Override
    public Vector3f readObject(ByteBuffer data, Class c) throws IOException {
        Vector3f vec3 = new Vector3f();
        vec3.x = data.getFloat();
        vec3.y = data.getFloat();
        vec3.z = data.getFloat();
        return vec3;
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        Vector3f vec3 = (Vector3f) object;
        buffer.putFloat(vec3.x);
        buffer.putFloat(vec3.y);
        buffer.putFloat(vec3.z);
    }
}
