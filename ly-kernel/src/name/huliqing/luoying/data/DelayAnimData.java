/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
@Serializable
public class DelayAnimData extends ObjectData{
    
    public AnimData getAnimData() {
        return getAsSavable("animData");
    }

    public void setAnimData(AnimData animData) {
        setAttribute("animData", animData);
    }

    public float getDelayTime() {
        return getAsFloat("delayTime", 0);
    }

    public void setDelayTime(float delayTime) {
        setAttribute("delayTime", delayTime);
    }
    
}
