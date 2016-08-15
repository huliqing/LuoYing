/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.util.SafeArrayList;
import name.huliqing.core.data.ControlData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actorlogic.ActorLogic;

/**
 * 逻辑控制器，控制角色的所有逻辑的运行。
 * @author huliqing
 */
public class ActorLogicControl extends ActorControl<ControlData> {

    private Actor actor;
    private final SafeArrayList<ActorLogic> logics = new SafeArrayList<ActorLogic>(ActorLogic.class);

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;
    }

    @Override
    public void cleanup() {
        for (ActorLogic logic : logics) {
            logic.cleanup();
        }
        logics.clear();
        super.cleanup(); 
    }
    
    @Override
    public void actorUpdate(float tpf) {
        for (ActorLogic logic : logics.getArray()) {
            logic.update(tpf);
        }
    }

    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {
    }
    
    public void addLogic(ActorLogic logic) {
        if (!logics.contains(logic)) {
            logic.setActor(actor);
            logic.initialize();
            logics.add(logic);
        }
    }

    public boolean removeLogic(ActorLogic logic) {
        if (!logics.contains(logic))
            return false;
        
        logic.cleanup();
        return logics.remove(logic);
    }
}
