/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.magic;

import com.jme3.app.Application;
import java.util.List;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.MagicData;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.skill.HitUtils;
import name.huliqing.ly.utils.ConvertUtils;

/**
 * 可以"持续"影响角色属性"动态值"的魔法，这个影响值可能是“增加”或“减少”
 * @author huliqing
 */
public class AttributeHitMagic extends Magic {
//    private final AttributeService attributeService = Factory.get(AttributeService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    
    // 影响的属性ID
    private AttributeWrap[] attributes;
    // 时间间隔,单位秒。
    private float interval;
    private float distance;
    
    // 暂不要使用范围限制
//    // 注：这里是弧度
//    private float angle;
    
    // ---- inner
    private float intervalUsed;
    // distance的平方，用于优化判断
    private float distanceSquared;
    
    @Override
    public void setData(MagicData data) {
        super.setData(data); 
        // attributes 格式："attribute|value,attribute|value,..."
        String[] attributesArr = data.getAsArray("attributes");
        attributes = new AttributeWrap[attributesArr.length];
        for (int i = 0; i < attributesArr.length; i++) {
            String[] attr = attributesArr[i].split("\\|");
            attributes[i] = new AttributeWrap(attr[0], ConvertUtils.toFloat(attr[1], 0));
        }
        interval = data.getAsFloat("interval", 1.0f);
        distance = data.getAsFloat("distance", 1.0f);
        
//        // 注意：这里把角度转换成了弧度
//        angle = FastMath.DEG_TO_RAD * data.getAsFloat("angle", 360);
    }

    @Override
    public void initialize(Application app) {
        super.initialize(app);
        distanceSquared = distance * distance;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); 
        intervalUsed += tpf;
        if (intervalUsed >= interval) {
            intervalUsed = 0;
            applyHit();
        }
    }
    
    private void applyHit() {
        List<Actor> actors = playService.findAllActor();
        if (actors.isEmpty()) 
            return;
        
        for (Actor hitTarget : actors) {
            if (hitTarget.getSpatial().getWorldTranslation().distanceSquared(localRoot.getWorldTranslation()) <= distanceSquared) {
                if (hitChecker == null || hitChecker.canHit(source, hitTarget)) {
                    for (AttributeWrap aw : attributes) {
                        HitUtils.getInstance().applyHit(source, hitTarget, aw.attribute, aw.amount);
                    }
                }
            }
        }

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
