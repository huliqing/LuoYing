/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.drop;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.el.STNumberEl;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 掉落属性值设置
 * @author huliqing
 */
public class AttributeDrop extends AbstractDrop {
    private final ElService elService = Factory.get(ElService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    // 属性名称
    private String attribute;
    // 这个公式定义角色source可以掉落多少属性值给target。
    private STNumberEl valueHitEl;
    
    @Override
    public void setData(DropData data) {
        super.setData(data);
        attribute = data.getAsString("attribute");
        valueHitEl = elService.createSTNumberEl(data.getAsString("valueHitEl", "#{0}"));
    }

    @Override
    public void doDrop(Entity source, Entity target) {
        // 属性掉是掉给target角色的，如果target不存在，则就没有意义。
        if (target == null) {
            return;
        }
        valueHitEl.setSource(source.getAttributeManager());
        valueHitEl.setTarget(target.getAttributeManager());
        entityNetwork.hitNumberAttribute(target, attribute, valueHitEl.getValue().floatValue(), source);
        playDropSounds(source);
    }

}
