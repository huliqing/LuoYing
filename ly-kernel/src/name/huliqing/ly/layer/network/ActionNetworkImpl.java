/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.ly.network.Network;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.ActionService;
import name.huliqing.ly.object.action.Action;
import name.huliqing.ly.object.entity.Entity;

/**
 * 注意：客户端是不能执行任何逻辑和行为的，服务端也不应该把逻辑和行为广播到 客户端。
 * 所以这里基本上只是判断并确定为主机，然后执行行为就行。不需要广播到客户端。
 * @author huliqing
 */
public class ActionNetworkImpl implements ActionNetwork {

    private final static Network NETWORK = Network.getInstance();
    private ActorNetwork actorNetwork;
    private ActionService actionService;

    @Override
    public void inject() {
        actorNetwork = Factory.get(ActorNetwork.class);
        actionService = Factory.get(ActionService.class);
    }

    @Override
    public void playAction(Entity actor, Action action) {
        if (!NETWORK.isClient()) {
            actionService.playAction(actor, action);
        }
    }

    @Override
    public void playRun(Entity actor, Vector3f pos) {
        if (!NETWORK.isClient()) {
            actionService.playRun(actor, pos);
        }
    }

    @Override
    public void playFight(Entity actor, Entity target, String skillId) {
        // 客户端是不能执行逻辑和行为的
        if (!NETWORK.isClient()) {
            actorNetwork.setTarget(actor, target);
            actionService.playFight(actor, target, skillId);
        }
    }

}
