/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 服务端通知客户端，让客户端移除一个角色出场景。这可能发生在如：
 * 踢出一个角色、一个死亡回收、客户端离开游戏等情况
 * @author huliqing
 */
@Serializable
public class MessSCActorRemove extends MessBase {
    
    // 指定要移出场景的角色ID
    private long actorId = -1;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        PlayService playService = Factory.get(PlayService.class);
        Entity actorToRemove = playService.getEntity(actorId);
        if (actorToRemove != null) {
            playService.removeEntity(actorToRemove);
        }
    }
    
}
