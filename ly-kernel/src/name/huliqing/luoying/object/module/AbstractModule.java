/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public abstract class AbstractModule implements Module<ModuleData> {
    private static final Logger LOG = Logger.getLogger(AbstractModule.class.getName());
    private final MessageService messageService = Factory.get(MessageService.class);

    protected ModuleData data;
    protected boolean initialized;
    protected int moduleOrder;
    protected Entity entity;
    
    // 绑定一个BooleanAttribute来确定是否打开或关闭模块的Message消息。
    // 具体记录什么游戏消息由各个模块自行决定。
    protected String bindMessageEnabledAttribute;
    
    // ---- inner
    protected BooleanAttribute messageEnabledAttribute;
    
    @Override
    public void setData(ModuleData data) {
        this.data = data;
        this.moduleOrder = data.getAsInteger("moduleOrder", moduleOrder);
        this.bindMessageEnabledAttribute = data.getAsString("bindMessageEnabledAttribute");
    }

    @Override
    public ModuleData getData() {
        return data;
    }
    
    @Override
    public void initialize(Entity entity) {
        if (initialized) {
            throw new IllegalStateException("Module is already initialized! module=" + getClass());
        }
        this.initialized = true;
        this.entity = entity;
        messageEnabledAttribute = getAttribute(bindMessageEnabledAttribute, BooleanAttribute.class);
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
    @Override
    public Entity getEntity() {
        return entity;
    }
    
    /**
     * 获取实体属性
     * @param <T>
     * @param attributeName
     * @param type
     * @return 
     */
    protected <T extends Attribute> T getAttribute(String attributeName, Class<T> type) {
        if (attributeName == null) {
            return null;
        }
        T attribute = entity.getAttributeManager().getAttribute(attributeName, type);
        if (attribute == null) {
            LOG.log(Level.WARNING, "Attribute not found by attributeName={0}, attributeType={1}, entity={2}, entityUniqueId={3}"
                    , new Object[] {attributeName, type.getName(), entity.getData().getId(), entity.getData().getUniqueId()});
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
    
//    /**
//     * 添加实体消息，用于向控制台输出关于当前实体的行为消息，例如：实体添加、使用或删除了一个物体。
//     * @param stateCode 消息状态码,具体参考：{@link StateCode}
//     * @param objectData Entity消息的物体对象,这表示这个物品被添加、使用或删除
//     * @see #addEntityMessage(name.huliqing.luoying.xml.ObjectData, int, java.lang.String) 
//     */
//    protected void addEntityMessage(ObjectData objectData, int stateCode) {
//        // 使用物品时的游戏消息
//        if (!isMessageEnabled()) {
//            return;
//        }
//        String resKey;
//        switch (stateCode) {
//            case StateCode.DATA_ADD:
//                resKey = ResConstants.DATA_ADD;
//                break;
//            case StateCode.DATA_REMOVE:
//                resKey = ResConstants.DATA_REMOVE;
//                break;
//            case StateCode.DATA_REMOVE_FAILURE:
//            case StateCode.DATA_REMOVE_FAILURE_NOT_FOUND:
//            case StateCode.DATA_REMOVE_FAILURE_UN_DELETABLE:
//                resKey = ResConstants.DATA_REMOVE_FAILURE;
//                break;
//            case StateCode.DATA_USE:
//                resKey = ResConstants.DATA_USE;
//                break;
//            case StateCode.DATA_USE_FAILURE:
//            case StateCode.DATA_USE_FAILURE_CHECK_EL:
//            case StateCode.DATA_USE_FAILURE_NOT_ENOUGH:
//            case StateCode.DATA_USE_FAILURE_SKILL_EXISTS:
//                resKey = ResConstants.DATA_USE_FAILURE;
//                break;
//            default:
//                resKey = null;
//        }
//        String message = resKey != null ? ResManager.get(resKey, ResManager.get(objectData.getId() + ".name"))
//                : "Unknow state code!";
//        messageService.addMessage(new EntityMessage(stateCode, message, entity, objectData));
//    }
//    
//    /**
//     * 添加实体消息，用于向控制台输出关于当前实体的行为消息，例如：实体添加、使用或删除了一个物体。
//     * @param objectData 物体目标
//     * @param stateCode 消息状态码
//     * @param message 消息内容
//     * @see #addEntityMessage(name.huliqing.luoying.xml.ObjectData, int) 
//     */
//    protected void addEntityMessage(ObjectData objectData, int stateCode, String message) {
//        // 使用物品时的游戏消息
//        if (!isMessageEnabled()) {
//            return;
//        }
//        messageService.addMessage(new EntityMessage(stateCode, message, entity, objectData));
//    }
    
    
}
