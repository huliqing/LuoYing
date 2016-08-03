/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import name.huliqing.core.xml.ProtoData;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 * 天赋
 * @author huliqing
 */
@Serializable
public class TalentData extends ProtoData {
    // 天赋的最高等级
    private int maxLevel;
    private int level;
    private float interval;
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(maxLevel, "maxLevel", 10);
        oc.write(level, "level", 0);
        oc.write(interval, "interval", 10);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        maxLevel = ic.readInt("maxLevel", 10);
        level = ic.readInt("level", 0);
        interval = ic.readFloat("interval", 10);
    }
    
    public TalentData(){}
    
    public TalentData(String id) {
        super(id);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getInterval() {
        return interval;
    }

    public void setInterval(float interval) {
        this.interval = interval;
    }

    /**
     * 判断天赋等级是否已经达到最高
     * @return 
     */
    public boolean isMax() {
        return level >= maxLevel;
    }
}
