/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.network.serializing.Serializable;

/**
 * 状态
 * @author huliqing
 */
@Serializable
public class StateData extends ObjectData {
    
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
    
}
