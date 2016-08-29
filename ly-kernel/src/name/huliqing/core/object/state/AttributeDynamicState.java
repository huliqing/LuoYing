/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import name.huliqing.core.data.StateData;
import name.huliqing.core.object.skill.HitUtils;

/**
 * 可以"持续"改变角色属性值的状态.
 * @author huliqing
 */
public class AttributeDynamicState extends State {

    // 影响的属性名称
    private String attributeName;
    // 每一次间隔要影响的数值
    private float value;
    // 时间间隔,单位秒。
    private float interval;
    
    // ---- inner
    private float intervalUsed;
    
    @Override
    public void setData(StateData data) {
        super.setData(data);
        attributeName = data.getAsString("attributeName");
        value = data.getAsFloat("value");
        interval = data.getAsFloat("interval", 1.0f);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        intervalUsed += tpf;
        if (intervalUsed >= interval && attributeName != null) {
            intervalUsed = 0;
            HitUtils.getInstance().applyHit(sourceActor, actor, attributeName, value);
        }
    }

}
