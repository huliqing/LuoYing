/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ActorData;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.LogicService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.LogicModule;

/**
 * 服务端发消息给客户端，告诉客户端载入一个新的角色
 * @author huliqing
 */
@Serializable
public class MessPlayActorLoaded extends MessBase {

    private ActorData actorData;
    private Vector3f location;
    private Vector3f viewDirection;
    
    // 用于同步角色动画,以下数组是一一对应的。
    private String[] channels;
    private String[] anims;
    private byte[] loopModes;
    private float[] speeds;
    private float[] times;
    
    public MessPlayActorLoaded() {}
    
    public MessPlayActorLoaded(ActorData actorData) {
        this.actorData = actorData;
    }

    public ActorData getActorData() {
        return actorData;
    }

    public void setActorData(ActorData actorData) {
        this.actorData = actorData;
    }

    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public Vector3f getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(Vector3f viewDirection) {
        this.viewDirection = viewDirection;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    public String[] getAnims() {
        return anims;
    }

    public void setAnims(String[] anims) {
        this.anims = anims;
    }

    public byte[] getLoopModes() {
        return loopModes;
    }

    public void setLoopModes(byte[] loopModes) {
        this.loopModes = loopModes;
    }

    public float[] getSpeeds() {
        return speeds;
    }

    public void setSpeeds(float[] speeds) {
        this.speeds = speeds;
    }

    public float[] getTimes() {
        return times;
    }

    public void setTimes(float[] times) {
        this.times = times;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        ActorService actorService = Factory.get(ActorService.class);
        LogicService logicService = Factory.get(LogicService.class);
        
        Entity actor = Loader.load(actorData);
        actorService.syncTransform(actor, location, viewDirection);
        actorService.syncAnimation(actor, channels, anims, loopModes, speeds, times);
        
        // 对于客户端来说，角色永远都是无AI的
        LogicModule lm = actor.getModuleManager().getModule(LogicModule.class);
        if (lm != null) {
            lm.setEnabled(false);
        }
        
        playService.addEntity(actor);
        
    }
    
}
