/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;

/**
 * 角色逻辑数据
 * @author huliqing
 */
@Serializable
public class ActorLogicData extends ProtoData {
    
    private float interval;

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(interval, "interval", 1);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        interval = ic.readFloat("interval", 1);
    }
    
    public ActorLogicData(){}
    
    public ActorLogicData(String id) {
        super(id);
    }

    /**
     * 获取逻辑的执行频率，单位秒
     * @return 
     */
    public float getInterval() {
        return interval;
    }

    /**
     * 设置逻辑的执行频率，单位秒
     * @param interval 
     */
    public void setInterval(float interval) {
        this.interval = interval;
    }

}
