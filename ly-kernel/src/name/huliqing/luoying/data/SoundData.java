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

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * 声音
 * @author huliqing
 */
@Serializable
public class SoundData extends ObjectData {
    
    /**
     * 获取声音文件，如："Sounds/xx.ogg"
     * @return 
     */
    public String getSoundFile() {
        return getAsString("file");
    }
    
    public float getVolume() {
        return getAsFloat("volume", 1.0f);
    }

    public float getTimeOffset() {
        return getAsFloat("timeOffset", 0);
    }

    public boolean isLooping() {
        return getAsBoolean("looping", false);
    }

    public Vector3f getDirection() {
        return getAsVector3f("direction", new Vector3f(0, 0, 1));
    }

    public boolean isDirectional() {
        return getAsBoolean("directional", false);
    }

    public float getInnerAngle() {
        return getAsFloat("innerAngle", 360);
    }

    public float getMaxDistance() {
        return getAsFloat("maxDistance", 200);
    }

    public float getOuterAngle() {
        return getAsFloat("outerAngle", 360);
    }

    public float getPitch() {
        return getAsFloat("pitch", 1);
    }

    public boolean isPositional() {
        return getAsBoolean("positional", false);
    }

    public float getRefDistance() {
        return getAsFloat("refDistance", 10);
    }

    public boolean isReverbEnabled() {
        return getAsBoolean("reverbEnabled", false);
    }

    public Vector3f getVelocity() {
        return getAsVector3f("velocity", new Vector3f());
    }

    public boolean isVelocityFromTranslation() {
        return getAsBoolean("velocityFromTranslation", false);
    }

    public boolean isInstance() {
        return getAsBoolean("instance", false);
    }
    
    
}
