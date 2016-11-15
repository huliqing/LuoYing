/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 设置角色的视角方向
 * @author huliqing
 */
@Serializable
public class MessActorViewDir extends MessBase {
    
    private long actorId;
    // 视角方向，注：不是位置
    private Vector3f viewDir;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public Vector3f getViewDir() {
        return viewDir;
    }

    /**
     * 视角方向，不需要归一，最好在设置时会统一归一
     * @param viewDir 
     */
    public void setViewDir(Vector3f viewDir) {
        this.viewDir = viewDir;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        PlayService playService = Factory.get(PlayService.class);
        ActorService actorService = Factory.get(ActorService.class);
        Entity actor = playService.getEntity(actorId);
        if (actor != null) {
            actorService.setViewDirection(actor, viewDir);
        }
    }
    
    
}
