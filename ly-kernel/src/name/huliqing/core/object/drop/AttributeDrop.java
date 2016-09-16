/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.drop;

import name.huliqing.core.Factory;
import name.huliqing.core.data.DropData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.mvc.service.ElService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.el.AttributeEl;

/**
 * 掉落属性值设置
 * @author huliqing
 */
public class AttributeDrop extends AbstractDrop {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final ElService elService = Factory.get(ElService.class);
    
    // 属性名
    private String attributeName;
    // 绑定一条公式（ElAttribute类型）,用来计算要掉落的动态值.这个值在计算后会和fixedValue一起增加到属性上
    private String attributeEl;
    //固定的掉落值，设置了这个参数后，这个值会始终增加到属性上。
    private float fixedValue;

    @Override
    public void setData(DropData data) {
        super.setData(data);
        attributeName = data.getAsString("attributeName");
        attributeEl = data.getAsString("attributeEl");
        fixedValue = data.getAsFloat("fixedValue", 0);
    }

    @Override
    public boolean doDrop(Actor source, Actor target) {
        float value = fixedValue;
        if (attributeEl != null) {
            AttributeEl el = elService.getAttributeEl(attributeEl);
            value += el.getValue(source, target);
        }
        attributeService.addNumberAttributeValue(target, attributeName, value);
        playDropSounds(source);
        return true;
    }

}
