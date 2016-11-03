/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.magic;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.MagicData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.ConvertUtils;

/**
 * 可以"持续"影响角色属性"动态值"的魔法，这个影响值可能是“增加”或“减少”
 * @author huliqing
 */
public class AttributeHitMagic extends AbstractMagic {
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    // 影响的属性ID
    private AttributeWrap[] attributes;
    // 时间间隔,单位秒。
    private float interval = 1.0f;
    private float distance = 1.0f;
    
    // ---- inner
    private float intervalUsed;
    
    private final List<Actor> tempStore = new ArrayList<Actor>();
    
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
        distance = data.getAsFloat("distance", distance);
        interval = data.getAsFloat("interval", interval);
        intervalUsed = data.getAsFloat("intervalUsed", intervalUsed);
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("intervalUsed", intervalUsed);
    }

    @Override
    public void magicUpdate(float tpf) {
        super.magicUpdate(tpf); 
        intervalUsed += tpf;
        if (intervalUsed >= interval) {
            intervalUsed = 0;
            applyHit();
        }
    }
    
    private void applyHit() {
        tempStore.clear();
        List<Actor> actors = scene.getEntities(Actor.class, magicRoot.getWorldTranslation(), distance, tempStore);
        if (actors.isEmpty()) 
            return;
        
        hitCheckEl.setSource(source.getAttributeManager());
        for (Entity hitTarget : actors) {
            if (hitCheckEl.setTarget(hitTarget.getAttributeManager()).getValue()) {
                for (AttributeWrap aw : attributes) {
                    entityNetwork.applyNumberAttributeValue(hitTarget, aw.attribute, aw.amount, source);
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
