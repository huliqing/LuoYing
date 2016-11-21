/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.object.action.Action;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 注意：客户端是不能执行任何逻辑和行为的，服务端也不应该把逻辑和行为广播到 客户端。
 * 所以这里基本上只是判断并确定为主机，然后执行行为就行。不需要广播到客户端。
 * @author huliqing
 */
public class ActionNetworkImpl implements ActionNetwork {

    private final Network network = Network.getInstance();
    private ActionService actionService;

    @Override
    public void inject() {
        actionService = Factory.get(ActionService.class);
    }

    @Override
    public void playAction(Entity actor, Action action) {
        if (!network.isClient()) {
            actionService.playAction(actor, action);
        }
    }

    @Override
    public void playRun(Entity actor, Vector3f pos) {
        if (!network.isClient()) {
            actionService.playRun(actor, pos);
        }
    }

    @Override
    public void playFight(Entity actor, Entity target, String skillId) {
        // 客户端是不能执行逻辑和行为的
        if (!network.isClient()) {
            
            // remove20161103
//            actorNetwork.setTarget(actor, target);

            actionService.playFight(actor, target, skillId);
        }
    }

}
