/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ActorData;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.play.LanClientListener;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 告诉客户端载入一个新的角色
 * @author huliqing
 */
@Serializable
public class MessSCActorLoaded extends MessBase {

    private ActorData actorData;
    private Vector3f location;
    private Vector3f viewDirection;
    
    // 用于同步角色动画,以下数组是一一对应的。
    private String[] channels;
    private String[] anims;
    private byte[] loopModes;
    private float[] speeds;
    private float[] times;
    
    public MessSCActorLoaded() {}
    
    public MessSCActorLoaded(ActorData actorData) {
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
        
        // 对于客户端来说，角色永远都是无AI的
        Actor actor = actorService.loadActor(actorData);
        actorService.setAutoAi(actor, false);
        actorService.syncTransform(actor, location, viewDirection);
        actorService.syncAnimation(actor, channels, anims, loopModes, speeds, times);
        playService.addObject(actor.getModel(), false);
        // 如果唯一ID配置，则说明刚好是客户端选择的角色ID
        if (actor.getData().getUniqueId() == LanClientListener.playerActorUniqueId) {
            playService.setAsPlayer(actor);
        }
        if (Config.debug) {
            Logger.getLogger(MessSCActorLoaded.class.getName())
                    .log(Level.INFO, "客户端载入角色,actorId={0}, uniqueId={1}, playerActorUniqueId={2}"
                    , new Object[] {actor.getData().getId(), actor.getData().getUniqueId(), LanClientListener.playerActorUniqueId});
        }
    }
    
}
