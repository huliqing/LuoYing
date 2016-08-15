/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import name.huliqing.core.Factory;
import name.huliqing.core.data.StateData;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.skill.HitUtils;
import name.huliqing.core.utils.ConvertUtils;

/**
 * 可以"持续"改变角色属性"动态值"的状态.
 * @author huliqing
 */
public class AttributeDynamicState extends State {

//    private final AttributeService attributeService = Factory.get(AttributeService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    
    // 影响的属性ID
    private AttributeWrap[] attributes;
    // 时间间隔,单位秒。
    private float interval;
    
    // ---- inner
    private float intervalUsed;
    // 产生这个状态的源角色
    private Actor sourceActor;
    
    @Override
    public void setData(StateData data) {
        super.setData(data);
        // attributes 格式："attribute|value,attribute|value,..."
        String[] attributesArr = data.getAsArray("attributes");
        attributes = new AttributeWrap[attributesArr.length];
        for (int i = 0; i < attributesArr.length; i++) {
            String[] attr = attributesArr[i].split("\\|");
            attributes[i] = new AttributeWrap(attr[0], ConvertUtils.toFloat(attr[1], 0));
        }
        interval = data.getAsFloat("interval", 1.0f);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (data.getSourceActor() > 0) {
            sourceActor = playService.findActor(data.getSourceActor());
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        intervalUsed += tpf;
        if (intervalUsed >= interval) {
            intervalUsed = 0;
            for (AttributeWrap aw : attributes) {
                applyHit(aw.attribute, aw.amount);
            }
        }
    }

    @Override
    public void cleanup() {
        sourceActor = null;
        super.cleanup(); 
    }
    
    private void applyHit(String attribute, float value) {
        // 要注意的是: applyHit并不一定是伤害值，也可能是增益值。
        HitUtils.getInstance().applyHit(sourceActor, actor, attribute, value);
    }
    
    private class AttributeWrap {
        // 要改变的属性ID
        String attribute;
        // 每次要改变的量，可正可负
        float amount;
        
        public AttributeWrap(String attribute, float amount) {
            this.attribute = attribute;
            this.amount = amount;
        }
    }
}
