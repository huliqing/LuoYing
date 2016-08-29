/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actorlogic.ActorLogic;

/**
 * 逻辑控制器，控制角色的所有逻辑的运行。
 * @author huliqing
 */
public class LogicModule extends AbstractModule {
    private final ActorService actorService = Factory.get(ActorService.class);

    private Actor actor;
    private final SafeArrayList<ActorLogic> logics = new SafeArrayList<ActorLogic>(ActorLogic.class);

    private Control updateControl;
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;
        
        List<ActorLogicData> logicDatas = actor.getData().getObjectDatas(ActorLogicData.class, null);
        if (logicDatas != null) {
            for (ActorLogicData ld : logicDatas) {
                addLogic((ActorLogic) Loader.load(ld));
            }
        }
        
        updateControl = new AdapterControl() {
            @Override
            public void update(float tpf) {logicUpdate(tpf);}
        };
        this.actor.getSpatial().addControl(updateControl);
    }
    
    private void logicUpdate(float tpf) {
        if (actorService.isDead(actor)) {
            return;
        }
        if (!actorService.isAutoAi(actor)) {
            return;
        }
        for (ActorLogic logic : logics.getArray()) {
            logic.update(tpf);
        }
    }

    @Override
    public void cleanup() {
        for (ActorLogic logic : logics) {
            logic.cleanup();
        }
        logics.clear();
        if (updateControl != null) {
            actor.getSpatial().removeControl(updateControl);
        }
        super.cleanup(); 
    }

    public void addLogic(ActorLogic logic) {
        if (logics.contains(logic))
            return;
        
        logics.add(logic);
        actor.getData().addObjectData(logic.getData());
        
        logic.setActor(actor);
        logic.initialize();
    }

    public boolean removeLogic(ActorLogic logic) {
        if (!logics.contains(logic))
            return false;
        
        actor.getData().removeObjectData(logic.getData());
        logics.remove(logic);
        logic.cleanup();
        return true;
    }
    
    public List<ActorLogic> getLogics() {
        return logics;
    }
}
