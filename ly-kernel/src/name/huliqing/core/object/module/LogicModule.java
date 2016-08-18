/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.data.module.LogicModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actorlogic.ActorLogic;

/**
 * 逻辑控制器，控制角色的所有逻辑的运行。
 * @author huliqing
 * @param <T>
 */
public class LogicModule<T extends LogicModuleData> extends AbstractModule<T> {

    private Actor actor;
    private final SafeArrayList<ActorLogic> logics = new SafeArrayList<ActorLogic>(ActorLogic.class);

    private Control updateControl;
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;

//        // 从存档中载入状态，如果不是存档则从原始参数中获取
//        List<ActorLogicData> logicInits = (List<ActorLogicData>) data.getAttribute("logicDatas");
//        if (logicInits == null) {
//            String[] logicArr = data.getAsArray("logics");
//            if (logicArr != null) {
//                logicInits = new ArrayList<ActorLogicData>(logicArr.length);
//                for (String logicId : logicArr) {
//                    logicInits.add((ActorLogicData) DataFactory.createData(logicId));
//                }
//            }
//        }
//        if (logicInits != null) {
//            for (ActorLogicData stateData : logicInits) {
//                addLogic((ActorLogic)Loader.load(stateData));
//            }
//        }
//        data.setAttribute("logicDatas", logicDatas);


        if (data.getLogics() != null) {
            List<ActorLogicData> tempLogics = new ArrayList<ActorLogicData>(data.getLogics());
            data.clear();
            for (ActorLogicData ld : tempLogics) {
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
        if (data.getLogics() == null) {
            data.setLogics(new ArrayList<ActorLogicData>());
        }
        data.getLogics().add(logic.getData());
        logic.setActor(actor);
        logic.initialize();
    }

    public boolean removeLogic(ActorLogic logic) {
        if (!logics.contains(logic))
            return false;
        
        data.getLogics().remove(logic.getData());
        logics.remove(logic);
        logic.cleanup();
        return true;
    }
    
    public List<ActorLogic> getLogics() {
        return logics;
    }
}
