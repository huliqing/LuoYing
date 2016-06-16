/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.serializing.Serializable;

/**
 * 服务端向客户端响应消息,如果服务端成功载入客户端的存档角色则result=true
 * @author huliqing
 */
@Serializable
public class MessPlayLoadSavedActorResult  extends MessBase {
    
    private boolean success;
    // 成功载入的客户端玩家的角色的唯一ID
    private long actorId;
    
    public MessPlayLoadSavedActorResult() {}

    public MessPlayLoadSavedActorResult(boolean success) {
        this.success = success;
    }

    /**
     * 服务端是否成功载入客户端的存档角色,如果该方法返回true，则说明客户端“不”需要重新选择一个新角色开始游戏，否则
     * 客户端需要打开角色选择面板进行选择一个角色来进行游戏。
     * @return 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 如果服务端无法为客户端载入存档资料或载入失败等则设置为false,如果存档载入成功则设置为true.
     * @param success 
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 获取服务端成功载入的角色的唯一ID。这个角色的ID即是客户端的存档资料的角色的ID
     * @return 
     */
    public long getActorId() {
        return actorId;
    }

    /**
     * 设置服务端为客户端存档载入的角色的唯一ID。这个角色的ID即是客户端的存档资料的角色的ID
     * @param actorId 
     */
    public void setActorId(long actorId) {
        this.actorId = actorId;
    }
    
}
