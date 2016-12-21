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
package name.huliqing.luoying.object.logic;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.object.action.FightAction;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActionModule;

/**
 * 战斗逻辑
 * @author huliqing
 */
public class FightLogic extends AbstractLogic {
    private static final Logger LOG = Logger.getLogger(FightLogic.class.getName());
    
    private  FightAction fightAction;
    private String bindAiAttribute;
    
    // ---- inner
    // 最近一个找到的敌人,避免频繁的去查找敌人。
    private Entity lastFindEnemy;
    private ActionModule actionModule;
    
    private BooleanAttribute aiAttribute;
    
    @Override
    public void setData(LogicData data) {
        super.setData(data); 
        fightAction = (FightAction) Loader.load(data.getAsString("fightAction"));
        bindAiAttribute = data.getAsString("bindAiAttribute");
    }

    @Override
    public void initialize() {
        super.initialize();
        actionModule = actor.getModuleManager().getModule(ActionModule.class);
        if (bindAiAttribute != null) {
            aiAttribute = actor.getAttributeManager().getAttribute(bindAiAttribute, BooleanAttribute.class);
            if (aiAttribute == null) {
                LOG.log(Level.WARNING, "Could not find AiAttribute(BooleanAttribute)"
                        + ", bindAiAttribute={0}, actor={1}, logic={2}"
                        , new Object[] {bindAiAttribute, actor.getData().getId(), data.getId()});
            }
        }
    }
    
    private boolean isAvailableEnemy(Entity target) {
        if (target == null // 目标不存在
                || target.getScene() == null // 目标已经移出场景
                || !(target instanceof Actor)) {  // 目标不是角色
            return false;
        }
        return isEnemy(target);
    }
    
    @Override
    protected void doLogic(float tpf) {
        // AI逻辑关闭
        if (aiAttribute != null && !aiAttribute.getValue()) {
            return;
        }
        
        if (targetAttribute == null || viewAttribute == null) {
            return;
        }
        
        // 如果最近找到的敌人仍然是角色的当前目标，并且仍然是有效的敌人，则直接使用作为当前敌人即可。
        if (lastFindEnemy != null 
                && lastFindEnemy.getEntityId() == targetAttribute.longValue() 
                && isAvailableEnemy(lastFindEnemy)) {
            fightTarget(lastFindEnemy);
            return;
        }
        
        // 重新获取角色的目标并判断是否为有效的敌人，
        Entity target = getTarget();
        if (isAvailableEnemy(target)) {
            lastFindEnemy = target;
            fightTarget(lastFindEnemy);
            return;
        } else {
            // 如果目标无效，而fightAction仍在运行，则结束它。
            if (actionModule.getAction() == fightAction) {
                actionModule.startAction(null);
            }
        }
        
    }
    
    private void fightTarget(Entity target) {
        fightAction.setEnemy(target);
        actionModule.startAction(fightAction);
    }
}
