/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public abstract class DefaultMessageHandler implements MessageHandler {
    
    @Override
    public void handle(Message message) {
        if (message instanceof EntityDataAddMessage) {
            handleDataAddMessage((EntityDataAddMessage) message);
            
        } else if (message instanceof EntityDataRemoveMessage) {
            handleDataRemoveMessage((EntityDataRemoveMessage) message);
            
        } else if (message instanceof EntityDataUseMessage) {
            handleDataUseMessage((EntityDataUseMessage) message);
            
        } else if (message instanceof EntitySkillUseMessage) {
            handleSkillUseMessage((EntitySkillUseMessage) message);
            
        } else if (message instanceof EntityLevelUpMessage) {
            handleLevelUpMessage((EntityLevelUpMessage) message);
            
        } else if (message instanceof EntityTalentPointAddedMessage) {
            handleTalentPointAddedMessage((EntityTalentPointAddedMessage) message);
            
        } else {
            
            displayMessage(message, message.getMessage());
        }
    }

    /**
     * 处理“物体添加”的消息，子类可以覆盖这个方法来拦截并自行处理消息。
     * @param mess 
     */
    protected void handleDataAddMessage(EntityDataAddMessage mess) {
        switch (mess.getStateCode()) {
            case StateCode.DATA_ADD:
                displayMessage(mess, ResManager.get(ResConstants.DATA_ADD, getObjectName(mess.getObjectData())));
                break;
            case StateCode.DATA_ADD_FAILURE:
                displayMessage(mess, ResManager.get(ResConstants.DATA_ADD_FAILURE, getObjectName(mess.getObjectData())));
                break;
            case StateCode.DATA_ADD_FAILURE_DATA_EXISTS:
                displayMessage(mess, ResManager.get(ResConstants.DATA_ADD_FAILURE_DATA_EXISTS, getObjectName(mess.getObjectData())));
                break;
            default :
                displayMessage(mess, mess.getMessage());
        }
    }
    
    /**
     * 处理“物体移除”的消息，子类可以覆盖这个方法来拦截并自行处理消息。
     * @param message 
     */
    protected void handleDataRemoveMessage(EntityDataRemoveMessage message) {
        switch (message.getStateCode()) {
            case StateCode.DATA_REMOVE:
                displayMessage(message, ResManager.get(ResConstants.DATA_REMOVE, getObjectName(message.getObjectData())));
                break;
            case StateCode.DATA_REMOVE_FAILURE:
                displayMessage(message, ResManager.get(ResConstants.DATA_REMOVE_FAILURE, getObjectName(message.getObjectData())));
                break;
            case StateCode.DATA_REMOVE_FAILURE_IN_USING:
                displayMessage(message, ResManager.get(ResConstants.DATA_REMOVE_FAILURE_IN_USING, getObjectName(message.getObjectData())));
                break;
            case StateCode.DATA_REMOVE_FAILURE_NOT_FOUND:
                displayMessage(message, ResManager.get(ResConstants.DATA_REMOVE_FAILURE_NOT_FOUND, getObjectName(message.getObjectData())));
                break;
            case StateCode.DATA_REMOVE_FAILURE_UN_DELETABLE:
                displayMessage(message, ResManager.get(ResConstants.DATA_REMOVE_FAILURE_UN_DELETABLE, getObjectName(message.getObjectData())));
                break;
            default :
                displayMessage(message, message.getMessage());
        }
    }
    
    /**
     * 处理“物体使用”的消息(不包含技能使用)，子类可以覆盖这个方法来拦截并自行处理消息。
     * @param message 
     */
    protected void handleDataUseMessage(EntityDataUseMessage message) {
        switch (message.getStateCode()) {
            case StateCode.DATA_USE:
                displayMessage(message, ResManager.get(ResConstants.DATA_USE, getObjectName(message.getObjectData())));
                break;
            case StateCode.DATA_USE_FAILURE:
                displayMessage(message, ResManager.get(ResConstants.DATA_USE_FAILURE, getObjectName(message.getObjectData())));
                break;
            case StateCode.DATA_USE_FAILURE_CHECK_EL:
                displayMessage(message, ResManager.get(ResConstants.DATA_USE_FAILURE_CHECK_EL, getObjectName(message.getObjectData())));
                break;
            case StateCode.DATA_USE_FAILURE_NOT_ENOUGH:
                displayMessage(message, ResManager.get(ResConstants.DATA_USE_FAILURE_NOT_ENOUGH, getObjectName(message.getObjectData())));
                break;
            case StateCode.DATA_USE_FAILURE_NOT_FOUND:
                displayMessage(message, ResManager.get(ResConstants.DATA_USE_FAILURE_NOT_FOUND, getObjectName(message.getObjectData())));
                break;
            default :
                displayMessage(message, message.getMessage());
        }
    }
    
    /**
     * 处理“技能使用”的消息，子类可以覆盖这个方法来拦截技能使用消息。
     * @param message 
     */
    protected void handleSkillUseMessage(EntitySkillUseMessage message) {
        switch (message.getStateCode()) {
            case StateCode.SKILL_USE_OK:
                displayMessage(message, ResManager.get(ResConstants.DATA_USE, getObjectName(message.getSkillData())));
                break;
            case StateCode.SKILL_USE_FAILURE:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE));
                break;
            case StateCode.SKILL_USE_FAILURE_NOT_FOUND:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_NOT_FOUND));
                break;
            case StateCode.SKILL_USE_FAILURE_LOCKED:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_LOCKED, getObjectName(message.getSkillData())));
                break;
            case StateCode.SKILL_USE_FAILURE_COOLDOWN:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_COOLDOWN, getObjectName(message.getSkillData())));
                break;
            case StateCode.SKILL_USE_FAILURE_WEAPON_NOT_ALLOW:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_WEAPON_NOT_ALLOW, getObjectName(message.getSkillData())));
                break;
            case StateCode.SKILL_USE_FAILURE_WEAPON_NEED_TAKE_ON:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_WEAPON_NEED_TAKE_ON));
                break;
            case StateCode.SKILL_USE_FAILURE_ATTRIBUTE_NOT_ENOUGH:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_ATTRIBUTE_NOT_ENOUGH));
                break;
            case StateCode.SKILL_USE_FAILURE_ELCHECK:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_ELCHECK, getObjectName(message.getSkillData())));
                break;
            case StateCode.SKILL_USE_FAILURE_TARGET_NOT_FOUND:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_TARGET_NOT_FOUND));
                break;
            case StateCode.SKILL_USE_FAILURE_TARGET_OUT_OF_RANGE:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_TARGET_OUT_OF_RANGE));
                break;
            case StateCode.SKILL_USE_FAILURE_TARGET_UNSUITABLE_BY_ELCHECK:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_TARGET_UNSUITABLE_BY_ELCHECK, getObjectName(message.getSkillData())));
                break;
            case StateCode.SKILL_USE_FAILURE_CAN_NOT_INTERRUPT:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_CAN_NOT_INTERRUPT));
                break;
            case StateCode.SKILL_USE_FAILURE_BY_HOOK:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_BY_HOOK, getObjectName(message.getSkillData())));
                break;
            case StateCode.SKILL_USE_FAILURE_ACTOR_DEAD:
                displayMessage(message, ResManager.get(ResConstants.SKILL_USE_FAILURE_ACTOR_DEAD));
                break;
            default :
                displayMessage(message, message.getMessage());
        }
    }
    
    /**
     * 处理“等级提升”的消息，子类可以覆盖这个方法来拦截技能使用消息。
     * @param message 
     */
    protected void handleLevelUpMessage(EntityLevelUpMessage message) {
        displayMessage(message, ResManager.get(ResConstants.LEVEL_UP, message.getLevel()));
    }
    
    /**
     * 处理“天赋点数获得”的消息，子类可以覆盖这个方法来拦截技能使用消息。
     * @param message 
     */
    protected void handleTalentPointAddedMessage(EntityTalentPointAddedMessage message) {
        displayMessage(message, ResManager.get(ResConstants.TALENT_POINTS_ADDED, message.getTalentPointsAdded()));
    }
    
    private String getObjectName(ObjectData data) {
        return ResManager.get(data.getId() + ".name");
    }
    
    /**
     * 显示信息，子类需要实现这个方法来把消息显示到游戏界面上。
     * @param message
     * @param details
     */
    public abstract void displayMessage(Message message, String details);
    
}
