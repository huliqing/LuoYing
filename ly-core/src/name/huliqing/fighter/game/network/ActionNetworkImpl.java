/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.network;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.game.state.lan.Network;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.ActionService;
import name.huliqing.fighter.object.action.Action;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 注意：客户端是不能执行任何逻辑和行为的，服务端也不应该把逻辑和行为广播到 客户端。
 * 所以这里基本上只是判断并确定为主机，然后执行行为就行。不需要广播到客户端。
 * @author huliqing
 */
public class ActionNetworkImpl implements ActionNetwork {

    private final static Network network = Network.getInstance();
    private ActorNetwork actorNetwork;
    private ActionService actionService;

    @Override
    public void inject() {
        actorNetwork = Factory.get(ActorNetwork.class);
        actionService = Factory.get(ActionService.class);
    }

    @Override
    public Action loadAction(String actionId) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void playAction(Actor actor, Action action) {
        if (!network.isClient()) {
            actionService.playAction(actor, action);
        }
    }

    @Override
    public void playRun(Actor actor, Vector3f pos) {
        if (!network.isClient()) {
            actionService.playRun(actor, pos);
        }
    }

    @Override
    public void playFight(Actor actor, Actor target, String skillId) {
        // remove20160328,不要在Network层写客户端代码
//        // 如果攻击者是玩家，则更新targetFace
//        if (Common.getPlayState().getPlayer() == actor) {
//            Common.getPlayState().setTarget(target);
//        }

        // 客户端是不能执行逻辑和行为的
        if (!network.isClient()) {
            actorNetwork.setTarget(actor, target);
            actionService.playFight(actor, target, skillId);
        }
    }

    @Override
    public boolean isPlayingFight(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isPlayingRun(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isPlayingFollow(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Action getPlayingAction(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
