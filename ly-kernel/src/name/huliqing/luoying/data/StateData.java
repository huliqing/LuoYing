/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import name.huliqing.luoying.xml.ObjectData;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 状态
 * @author huliqing
 */
@Serializable
public class StateData extends ObjectData {
    
    private List<DelayAnimData> delayAnimDatas;
    
    /**
     * 获取状态的图标。
     * @return 
     */
    public String getIcon() {
        return getAsString("icon");
    }
    
    /**
     * 状态的产生者，也就是说，这个状态是哪一个角色发出的, 可能为null.
     * @return 
     */
    public long getSourceActor() {
        return getAsLong("sourceActor", 0);
    }

    /**
    * 源角色，这个角色主要是指制造这个状态的源角色, 比如：角色A攻击了角色B, A的这个攻击技能对B产生
    * 了一个“流血”状态。这时A即可以设置为这个“流血”状态的sourceActor。这样状
    * 态在运行时就可以获得源角色的引用，以便知道谁产生了这个状态。对于一些状态
    * 效果非常有用，比如“流血”这类伤害效果状态，这些状态在运行时要计算伤害，并
    * 要知道是谁产生了这些伤害。
    * @param sourceActor 
    */
    public void setSourceActor(long sourceActor) {
        setAttribute("sourceActor", sourceActor);
    }
    
    /**
     * 获取获态的抵消值,这个值取值范围在[0.0~1.0], 如果没有设置则默认返回0.
     * @return 
     * @see #setResist(float) 
     */
    public float getResist() {
        return getAsFloat("resist", 0);
    }
    
    /**
     * 设置状态削弱值，取值[0.0~1.0],该值主要用于削弱状态的作用，根据各
     * 种状态的实际情况各自实现该功能．0表示状态不削弱，1表示状态完全被削
     * 弱．0.5表示削弱一半，依此类推．示例：如实现一个击晕3秒的状态，如果
     * resist=0.3,则可实现最终的击晕时间为 3 * (1 - 0.3) = 2.1秒，换句话说，
     * 击晕效果被削弱了0.9秒．根据实现的不同，削弱方式可以完全不同，如实现
     * 一个中毒状态效果，你可以实现为削弱中毒时间，也可以实现为削弱中毒伤害
     * 等．
     * @param resist 
     */
    public void setResist(float resist) {
        setAttribute("resist", resist);
    }

    public List<DelayAnimData> getDelayAnimDatas() {
        return delayAnimDatas;
    }

    public void setDelayAnimDatas(List<DelayAnimData> anims) {
        this.delayAnimDatas = anims;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        if (delayAnimDatas != null) {
            oc.writeSavableArrayList(new ArrayList<DelayAnimData>(delayAnimDatas), "anims", null);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        delayAnimDatas = ic.readSavableArrayList("anims", null);
    }
    
    
}
