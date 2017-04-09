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
package name.huliqing.luoying.object.gamelogic;

import com.jme3.util.SafeArrayList;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;

/**
 * 用于修改场景中角色属性值的游戏逻辑，可用于如：为场景中所有角色提示生命值恢复，魔法值恢复，等。
 * 但是并不局限于恢复，还可用于减少。
 * @author huliqing
 */
public class AttributeChangeGameLogic extends AbstractGameLogic {

    private static final Logger LOG = Logger.getLogger(AttributeChangeGameLogic.class.getName());
    
    private final PlayService playService = Factory.get(PlayService.class);
    private final EntityService entityService = Factory.get(EntityService.class);
    private final ElService elService = Factory.get(ElService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);

    // 指定要修改的角色的属性值,角色必须有这个属性，否则没有意义。
    private String applyAttribute;
    
    // 指定要影响最终修改值的目标角色的属性值。
    private String bindFactorAttribute;
    
    // 基本的值，用于修改角色的属性
    private float baseValue;
    
    // 速度值，值越大，改变的量越大。
    private float speed = 1.0f;
    
    // 这个EL用于判断一个角色是否可以应用于当前的逻辑。
    private SBooleanEl checkEl;
    
    // ---- inner
    private final SafeArrayList<Actor> tempStore = new SafeArrayList<Actor>(Actor.class);
    
    @Override
    public void setData(GameLogicData data) {
        super.setData(data); 
        applyAttribute = data.getAsString("applyAttribute");
        bindFactorAttribute = data.getAsString("bindFactorAttribute");
        baseValue = data.getAsFloat("baseValue", baseValue);
        speed = data.getAsFloat("speed", speed);
        checkEl = elService.createSBooleanEl(data.getAsString("checkEl", "#{true}"));

        // 不允许interval小于0
        if (interval <= 0) {
            interval = 1;
        }
    }

    @Override
    protected void logicInit(Game game) {}
    
    @Override
    protected void logicUpdate(float tpf) {
        tempStore.clear();
        playService.getEntities(Actor.class, tempStore);
        if (tempStore.isEmpty())
            return;
        
        for (Actor actor : tempStore.getArray()) {
            if (checkEl.setSource(actor.getAttributeManager()).getValue()) {
                updateAttribute(actor);
            }
        }
    }
    
    private void updateAttribute(Entity actor) {
        // bindFactorAttribute是角色的已有属性，这个属性的值将影响最终的apply值。
        // 比如角色的属性（生命恢复速度)将影响这个游戏逻辑最终要修改角色生命值的属性。
        float factorValue = entityService.getNumberAttributeValue(actor, bindFactorAttribute, 0).floatValue();
        
        float applyValue = (baseValue + factorValue) * interval * speed;
            
        // remove20161220,不要用递增方式，容易产生累积误差, 并且由于网络延迟原因，
        // 技能并不能始终保证在服务端和客户端同步执行，而引起客户端和服务端属性消耗不一致.
//            entityNetwork.hitNumberAttribute(actor, applyAttribute, applyValue, null);

        NumberAttribute attr = actor.getAttributeManager().getAttribute(applyAttribute, NumberAttribute.class);
        if (attr != null) {
            float attrNewValue = attr.floatValue() + applyValue;
            entityNetwork.hitAttribute(actor, applyAttribute, attrNewValue, actor);
            
//            LOG.log(Level.INFO, "applyAttribute={0}, applyValue={1}, entity={2}"
//                    , new Object[] {applyAttribute, applyValue, actor.getData().getId()});
        }
    }
}
