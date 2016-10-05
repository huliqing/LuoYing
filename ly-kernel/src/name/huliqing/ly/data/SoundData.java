/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * 声音
 * @author huliqing
 */
@Serializable
public class SoundData extends ObjectData {
    
    // 声音参数基本上直接从attribute中获取，一般很少甚至基本不需要动态改变，直接使用即可，减少本地变量。
    // 以减少内存及带宽的占用
    
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
