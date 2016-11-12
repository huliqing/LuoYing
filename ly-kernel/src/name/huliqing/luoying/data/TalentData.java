/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.network.serializing.Serializable;

/**
 * 天赋
 * @author huliqing
 */
@Serializable
public class TalentData extends ObjectData {
    
    public String getIcon() {
        return getAsString("icon");
    }
    
    public int getMaxLevel() {
        return getAsInteger("maxLevel", 10);
    }

    public void setMaxLevel(int maxLevel) {
        setAttribute("maxLevel", maxLevel);
    }
    
    public int getLevel() {
        return getAsInteger("level", 0);
    }

    public void setLevel(int level) {
        setAttribute("level", level);
    }

    /**
     * 判断天赋等级是否已经达到最高
     * @return 
     */
    public boolean isMax() {
        return getLevel() >= getMaxLevel();
    }
    
}
