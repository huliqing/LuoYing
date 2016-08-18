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
import name.huliqing.core.data.module.ModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actorlogic.ActorLogic;
import name.huliqing.core.object.module.AbstractModule;
import name.huliqing.core.xml.DataFactory;

/**
 * 逻辑控制器，控制角色的所有逻辑的运行。
 * @author huliqing
 * @param <T>
 */
public class LogicModule<T extends ModuleData> extends AbstractModule<T> {

    private Actor actor;
    private final SafeArrayList<ActorLogic> logics = new SafeArrayList<ActorLogic>(ActorLogic.class);
    private final List<ActorLogicData> logicDatas = new ArrayList<ActorLogicData>();

    private Control updateControl;
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;

        // 从存档中载入状态，如果不是存档则从原始参数中获取
        List<ActorLogicData> logicInits = (List<ActorLogicData>) data.getAttribute("logicDatas");
        if (logicInits == null) {
            String[] logicArr = data.getAsArray("logics");
            if (logicArr != null) {
                logicInits = new ArrayList<ActorLogicData>(logicArr.length);
                for (String logicId : logicArr) {
                    logicInits.add((ActorLogicData) DataFactory.createData(logicId));
                }
            }
        }
        if (logicInits != null) {
            for (ActorLogicData stateData : logicInits) {
                addLogic((ActorLogic)Loader.load(stateData));
            }
        }
        
        data.setAttribute("logicDatas", logicDatas);
        
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
        if (updateControl != null) {
            actor.getSpatial().removeControl(updateControl);
        }
        for (ActorLogic logic : logics) {
            logic.cleanup();
        }
        logics.clear();
        logicDatas.clear();
        super.cleanup(); 
    }

    public void addLogic(ActorLogic logic) {
        if (!logics.contains(logic)) {
            logic.setActor(actor);
            logic.initialize();
            logics.add(logic);
            logicDatas.add(logic.getData());
        }
    }

    public boolean removeLogic(ActorLogic logic) {
        if (!logics.contains(logic))
            return false;
        
        logics.remove(logic);
        logicDatas.remove(logic.getData());
        logic.cleanup();
        return true;
    }
    
    public List<ActorLogic> getLogics() {
        return logics;
    }
}
