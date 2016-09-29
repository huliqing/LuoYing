/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import com.jme3.math.Vector3f;
import name.huliqing.core.network.Network;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.ActionService;
import name.huliqing.core.object.action.Action;
import name.huliqing.core.object.actor.Actor;

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
    public void playAction(Actor actor, Action action) {
        if (!NETWORK.isClient()) {
            actionService.playAction(actor, action);
        }
    }

    @Override
    public void playRun(Actor actor, Vector3f pos) {
        if (!NETWORK.isClient()) {
            actionService.playRun(actor, pos);
        }
    }

    @Override
    public void playFight(Actor actor, Actor target, String skillId) {
        // 客户端是不能执行逻辑和行为的
        if (!NETWORK.isClient()) {
            actorNetwork.setTarget(actor, target);
            actionService.playFight(actor, target, skillId);
        }
    }

}
