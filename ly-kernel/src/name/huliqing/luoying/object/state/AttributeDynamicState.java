/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 可以"持续"改变角色属性值的状态.
 * @author huliqing
 */
public class AttributeDynamicState extends AbstractState {
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);

    // 影响的属性名称
    private String bindNumberAttribute;
    // 每一次间隔要添加的数值
    private float addValue;
    // 时间间隔,单位秒。
    private float interval;
    
    // ---- inner
    private float intervalUsed;
    private Entity sourceActor;
    
    @Override
    public void setData(StateData data) {
        super.setData(data);
        bindNumberAttribute = data.getAsString("bindNumberAttribute");
        addValue = data.getAsFloat("addValue");
        interval = data.getAsFloat("interval", 1.0f);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        intervalUsed += tpf;
        if (intervalUsed >= interval && bindNumberAttribute != null) {
            intervalUsed = 0;
            if (sourceActor == null) {
                sourceActor = actor.getScene().getEntity(data.getSourceActor());
            }
            entityNetwork.hitNumberAttribute(actor, bindNumberAttribute, addValue, sourceActor);
        }
    }

}
