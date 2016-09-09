/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.network.serializing.Serializable;

/**
 * 天赋
 * @author huliqing
 */
@Serializable
public class TalentData extends ObjectData {
    
//    private int maxLevel;
//    private int level;
//    
//    public int getMaxLevel() {
//        return maxLevel;
//    }
//
//    public void setMaxLevel(int maxLevel) {
//        this.maxLevel = maxLevel;
//    }
//
//    public int getLevel() {
//        return level;
//    }
//
//    public void setLevel(int level) {
//        this.level = level;
//    }
//
//    /**
//     * 判断天赋等级是否已经达到最高
//     * @return 
//     */
//    public boolean isMax() {
//        return level >= maxLevel;
//    }
//    
//    @Override
//    public void write(JmeExporter ex) throws IOException {
//        super.write(ex);
//        OutputCapsule oc = ex.getCapsule(this);
//        oc.write(maxLevel, "maxLevel", 10);
//        oc.write(level, "level", 0);
//    }
//
//    @Override
//    public void read(JmeImporter im) throws IOException {
//        super.read(im);
//        InputCapsule ic = im.getCapsule(this);
//        maxLevel = ic.readInt("maxLevel", 10);
//        level = ic.readInt("level", 0);
//    }
}
