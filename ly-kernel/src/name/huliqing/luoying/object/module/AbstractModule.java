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
package name.huliqing.luoying.object.module;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.layer.service.MessageService;
import name.huliqing.luoying.message.EntityDataAddMessage;
import name.huliqing.luoying.message.EntityDataRemoveMessage;
import name.huliqing.luoying.message.EntityDataUseMessage;
import name.huliqing.luoying.message.EntityMessage;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * Module的抽象类,所有角色模块可以直接继承自这个基类
 * @author huliqing
 */
public abstract class AbstractModule implements Module {
    private static final Logger LOG = Logger.getLogger(AbstractModule.class.getName());
    private final MessageService messageService = Factory.get(MessageService.class);

    protected Entity entity;
    protected ModuleData data;
    protected boolean initialized;
    protected boolean enabled = true;
    protected int moduleOrder;
    
    // 绑定一个BooleanAttribute来确定是否打开或关闭模块的Message消息。
    // 具体记录什么游戏消息由各个模块自行决定。
    protected String bindMessageEnabledAttribute;
    
    // ---- inner
    protected BooleanAttribute messageEnabledAttribute;
    
    @Override
    public void setData(ModuleData data) {
        this.data = data;
        this.enabled = data.getAsBoolean("enabled", enabled);
        this.moduleOrder = data.getAsInteger("moduleOrder", moduleOrder);
        this.bindMessageEnabledAttribute = data.getAsString("bindMessageEnabledAttribute");
    }

    @Override
    public ModuleData getData() {
        return data;
    }
    
    @Override
    public void updateDatas() {
        data.setAttribute("enabled", enabled);
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
    @Override
    public Entity getEntity() {
        return entity;
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Module is already initialized! module=" + getClass());
        }
        this.initialized = true;
        messageEnabledAttribute = getAttribute(bindMessageEnabledAttribute, BooleanAttribute.class);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
    /**
     * 获取实体属性
     * @param <T>
     * @param attributeName
     * @param type
     * @return 
     */
    protected <T extends Attribute> T getAttribute(String attributeName, Class<T> type) {
        if (attributeName == null || attributeName.equals("")) {
            return null;
        }
        T attribute = entity.getAttributeManager().getAttribute(attributeName, type);
        if (attribute == null) {
            LOG.log(Level.WARNING
                    , "Attribute not found by attributeName={0}, attributeType={1}, entity={2}, entityUniqueId={3}, logClass={4}"
                    , new Object[] {attributeName
                            , type.getName()
                            , entity.getData().getId()
                            , entity.getData().getUniqueId()
                            , getClass().getName()
                    });
        }
        return attribute;
    }
    
    /**
     * 判断实体是否打开了游戏消息功能。
     * @return 
     */
    protected boolean isMessageEnabled() {
        return messageEnabledAttribute != null && messageEnabledAttribute.getValue();
    }
    
    /**
     * 添加实体消息。
     * @param message 
     */
    protected void addEntityMessage(EntityMessage message) {
        if (!isMessageEnabled()) {
            return;
        }
        messageService.addMessage(message);
    }
    
    /**
     * 添加消息： 实体<b>添加</b>物品的消息,
     * @param stateCode
     * @param objectData
     * @param count 
     */
    protected void addEntityDataAddMessage(int stateCode, ObjectData objectData, int count) {
        if (!isMessageEnabled()) {
            return;
        }
        String message = entity.getData().getId() + " add data " + objectData.getId();
        messageService.addMessage(new EntityDataAddMessage(stateCode, message, entity, objectData, count));
    }
    
    /**
     * 添加消息： 实体<b>移除</b>物品的消息
     * @param stateCode 状态码
     * @param objectData
     * @param count 
     */
    protected void addEntityDataRemoveMessage(int stateCode, ObjectData objectData, int count) {
        if (!isMessageEnabled()) {
            return;
        }
        String message = entity.getData().getId() + " remove data " + objectData.getId();
        messageService.addMessage(new EntityDataRemoveMessage(stateCode, message, entity, objectData, count));
    }
    
    /**
     * 添加消息： 实体<b>使用</b>物品时的消息.
     * @param stateCode
     * @param objectData 
     */
    protected void addEntityDataUseMessage(int stateCode, ObjectData objectData) {
        if (!isMessageEnabled()) {
            return;
        }
        String message = entity.getData().getId() + " use data " + objectData.getId();
        messageService.addMessage(new EntityDataUseMessage(stateCode, message, entity, objectData));
    }
    
    
}
