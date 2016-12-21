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
package name.huliqing.luoying.object.talent;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.TalentData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.el.LNumberEl;

/**
 * 属性类型的天赋，这种天赋可以增加或减少角色的属性值。
 * @author huliqing
 * @param <T>
 */
public class AttributeTalent<T extends TalentData> extends AbstractTalent<T> {
    private final ElService elService = Factory.get(ElService.class);
    private final EntityService entityService = Factory.get(EntityService.class);

    protected String bindAttribute;
    // valueLevelEl计算出的值会添加到bindAttribute上
    protected LNumberEl valueLevelEl;
    
    // ----
    private float actualApplyValue;
    
    // 判断天赋是否已经apply到角色身上, 需要这个判断是因为有可能角色是从存档中载入的，
    // 由于从存档中载入时角色的属性值已经被当前天赋改变过，所以就不再需要再去改变角色的属性，
    // 否则会造成属性值在存档后再次载入时不停的累加的bug.
    private boolean attributeApplied;

    @Override
    public void setData(T data) {
        super.setData(data); 
        this.bindAttribute = data.getAsString("bindAttribute");
        String temp = data.getAsString("valueLevelEl");
        if (temp != null) {
            valueLevelEl = elService.createLNumberEl(temp);
        }
        this.attributeApplied = data.getAsBoolean("_attributeApplied", attributeApplied);
        this.actualApplyValue = data.getAsFloat("_actualApplyValue", actualApplyValue);
    }
    
    // 天赋data是存放在actorData.objectDatas上的，当角色存档时，天赋的一些参数也需要进行保存。
    // 以便下次载入时可以恢复
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("_attributeApplied", attributeApplied);
        data.setAttribute("_actualApplyValue", actualApplyValue);
    }

    @Override
    public void initialize() {
        super.initialize();
        
        if (attributeApplied) {
            return;
        }

        NumberAttribute attr = actor.getAttributeManager().getAttribute(bindAttribute, NumberAttribute.class);
        if (attr == null) {
            return;
        }
        
        // 期望添加的属性值
        float expectApplyValue = valueLevelEl.setLevel(level).getValue().floatValue();
        // 属性值改变之前的值
        float oldValue = attr.floatValue();
        // 计变属性值
        entityService.hitNumberAttribute(actor, bindAttribute, expectApplyValue, null);
        // 实际作用的属性值
        actualApplyValue = attr.floatValue() - oldValue;
        attributeApplied = true;
    }

    @Override
    public void cleanup() {
        if (attributeApplied) {
            entityService.hitNumberAttribute(actor, bindAttribute, -actualApplyValue, null);
            attributeApplied = false;
        }
        super.cleanup();
    }
    
}
