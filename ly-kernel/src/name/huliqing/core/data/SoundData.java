/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import name.huliqing.core.xml.ProtoData;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class SoundData extends ProtoData {
    
    private float volume;
    private float offset;
    private boolean loop;
    
    public SoundData() {}
    
    public SoundData(String id) {
        super(id);
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    
    /**
     * 获取声音文件，如："Sounds/xx.ogg"
     * @return 
     */
    public String getSoundFile() {
        return getAttribute("file");
    }
}
