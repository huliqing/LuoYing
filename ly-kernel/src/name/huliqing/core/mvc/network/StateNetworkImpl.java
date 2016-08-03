/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.network.Network;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.StateData;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.mess.MessStateAdd;
import name.huliqing.core.mess.MessStateRemove;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.StateListener;
import name.huliqing.core.object.state.State;

/**
 * 重要：对于客户端和服务端的同步，一般应先广播到所有客户端，然后再在服务端自
 * 身执行逻辑。这可避免在服务端执行逻辑（未广播之前），修改了角色属性或其它，
 * 而这些修改又广播到了客户端，这会导致这个操作在广播到客户端之后，状态已经
 * 与服务端执行这个操作之前的状态发生了变化。
 * @author huliqing
 */
public class StateNetworkImpl implements StateNetwork {
    private final static Network network = Network.getInstance(); 
    private StateService stateService;

    @Override
    public void inject() {
        stateService = Factory.get(StateService.class);
    }
    
    @Override
    public float checkAddState(Actor actor, String stateId) {
        return stateService.checkAddState(actor, stateId);
    }
    
    @Override
    public boolean addState(Actor actor, String stateId, Actor sourceActor) {
        if (!network.isClient()) {
            float resist = stateService.checkAddState(actor, stateId);
            if (resist < 1) {
                if (network.hasConnections()) {
                    // 先广播
                    MessStateAdd mess = new MessStateAdd();
                    mess.setActorId(actor.getData().getUniqueId());
                    mess.setStateId(stateId);
                    mess.setResist(resist);
                    mess.setSourceActorId(sourceActor != null ? sourceActor.getData().getUniqueId() : -1);
                    network.broadcast(mess);
                }
                
                // 再执行服务端添加逻辑
                stateService.addStateForce(actor, stateId, resist, sourceActor);
            } else {
                // TODO:添加状态被抵抗，需要把提示信息广播到客户端
            }
        }
        return false;
    }

    @Override
    public void addStateForce(Actor actor, String stateId, float resist, Actor sourceActor) {
        if (!network.isClient()) {
            if (network.hasConnections()) {
                // 先广播
                MessStateAdd mess = new MessStateAdd();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setStateId(stateId);
                mess.setResist(resist);
                mess.setSourceActorId(sourceActor != null ? sourceActor.getData().getUniqueId() : -1);
                network.broadcast(mess);
            }

            // 再执行服务端添加逻辑
            stateService.addStateForce(actor, stateId, resist, sourceActor);
        }
    }

    @Override
    public boolean removeState(Actor actor, String stateId) {
        if (!network.isClient()) {
            if (network.hasConnections()) {
                // 先广播
                MessStateRemove mess = new MessStateRemove();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setStateId(stateId);
                network.broadcast(mess);
            }
            stateService.removeState(actor, stateId);
            return true;
        }
        return false;
    }

    @Override
    public State findState(Actor actor, String stateId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearStates(Actor actor) {
        throw new UnsupportedOperationException();
    }

    // remove20160803
//    @Override
//    public boolean existsState(String stateId) {
//        throw new UnsupportedOperationException();
//    }

    @Override
    public boolean existsState(Actor actor, String stateId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<StateData> getStates(Actor actor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addListener(Actor actor, StateListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeListener(Actor actor, StateListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
