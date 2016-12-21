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

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.el.STBooleanEl;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 角色逻辑
 * @author huliqing
 */
public abstract class AbstractLogic implements Logic<LogicData>{
    private final ElService elService = Factory.get(ElService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    /** 逻辑数据 */
    protected LogicData data;
    
    /** 逻辑的执行频率,单位秒，默认0秒 */
    protected float interval;
    
    /** 当前逻辑已经运行的时间，单位秒 */
    protected float timeUsed;
    
    /** 默认情况下逻辑始终是打开的，除些可以绑定一个EL表达式来判断是否要打开该逻辑功能。*/
    protected SBooleanEl enabledCheckEl;
    
    /** 绑定角色的“目标”属性， */
    protected String bindTargetAttribute;
    
    /** 绑定角色的“视角”范围属性 */
    protected String bindViewAttribute;
    
    /** 用于判断一个目标对象是否为当前角色的敌人的表达式 */
    protected STBooleanEl enemyCheckEl;
    
    // ---- inner
    
    /** 运行当前逻辑的角色. */
    protected Entity actor;
    
    /** 存放角色当前目标对象的属性 */
    protected NumberAttribute targetAttribute;
    
    /** 存放角色视角范围的属性 */
    protected NumberAttribute viewAttribute;
    
    /** 判断逻辑是否已经初始化 */
    protected boolean initialized;

    @Override
    public void setData(LogicData data) {
        this.data = data;
        interval = data.getAsFloat("interval", interval);
        timeUsed = data.getAsFloat("timeUsed", 0);
        String tempEnabledCheckEl = data.getAsString("enabledCheckEl");
        if (tempEnabledCheckEl != null) {
            enabledCheckEl = elService.createSBooleanEl(tempEnabledCheckEl);
        }
        bindTargetAttribute = data.getAsString("bindTargetAttribute");
        bindViewAttribute = data.getAsString("bindViewAttribute");
        String tempEnemyCheckEl = data.getAsString("enemyCheckEl");
        if (tempEnemyCheckEl != null) {
            enemyCheckEl = elService.createSTBooleanEl(tempEnemyCheckEl);
        }
    }
    
    @Override
    public LogicData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        data.setAttribute("interval", interval);
        data.setAttribute("timeUsed", timeUsed);
    }

    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Logic already initialized, logic id=" + data.getId() + ", actorId=" + actor.getData().getId());
        }
        initialized = true;
        if (enabledCheckEl != null) {
            enabledCheckEl.setSource(actor.getAttributeManager());
        }
        if (bindTargetAttribute != null) {
            targetAttribute = actor.getAttributeManager().getAttribute(bindTargetAttribute, NumberAttribute.class);
        }
        if (bindViewAttribute != null) {
            viewAttribute = actor.getAttributeManager().getAttribute(bindViewAttribute, NumberAttribute.class);
        }
        enemyCheckEl.setSource(actor.getAttributeManager());
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public final void update(float tpf) {
        if (enabledCheckEl != null && !enabledCheckEl.getValue()) {
            return;
        }
        timeUsed += tpf;
        if (timeUsed >= interval) {
            doLogic(tpf);
            timeUsed = 0;
        }
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
    /**
     * 设置执行逻辑的角色。
     * @param self 
     */
    @Override
    public void setActor(Entity self) {
        this.actor = self;
    }

    protected void setInterval(float interval) {
        this.interval = interval;
    }

    protected float getInterval() {
        return interval;
    }
    
    /**
     * 获取角色的当前目标对象。
     * @return 
     */
    protected Entity getTarget() {
        if (targetAttribute != null) {
            return actor.getScene().getEntity(targetAttribute.longValue());
        }
        return null;
    }
    
    /**
     * 设置角色的目标对象
     * @param target 
     */
    protected void setTarget(Entity target) {
        if (targetAttribute != null) {
            entityNetwork.hitAttribute(actor, targetAttribute.getName(), target.getEntityId(), null);
        }
    }
    
    /**
     * 获取角色的的视角范围，如果没有绑定角色属性，或者角色属性不存在，则这个方法将返回0.
     * @return 
     */
    protected float getViewDistance() {
        if (viewAttribute != null) {
            return viewAttribute.floatValue();
        }
        return 0;
    }
    
    /**
     * 判断一个目标是否为当前角色的敌人
     * @param target
     * @return 
     */
    protected boolean isEnemy(Entity target) {
        if  (enemyCheckEl != null) {
            return enemyCheckEl.setTarget(target.getAttributeManager()).getValue();
        }
        return false;
    }
    
    /**
     * 更新逻辑
     * @param tpf 
     */
    protected abstract void doLogic(float tpf);
}
