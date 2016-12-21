/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
    public void updateMagicLogic(float tpf) {
        super.updateMagicLogic(tpf); 
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
        
        if (source != null) {
            hitCheckEl.setSource(source.getAttributeManager());
        }
        for (Entity hitTarget : actors) {
            if (hitCheckEl.setTarget(hitTarget.getAttributeManager()).getValue()) {
                for (AttributeWrap aw : attributes) {
                    entityNetwork.hitNumberAttribute(hitTarget, aw.attribute, aw.amount, source);
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
