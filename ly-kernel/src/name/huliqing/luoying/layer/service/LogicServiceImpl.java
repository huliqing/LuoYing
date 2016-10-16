/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.logic.Logic;
import name.huliqing.luoying.object.module.LogicModule;

/**
 *
 * @author huliqing
 */
public class LogicServiceImpl implements LogicService {

    @Override
    public void inject() {
        // 
    }

    @Override
    public Logic loadLogic(String logicId) {
        return Loader.load(logicId);
    }

    @Override
    public void addLogic(Entity actor, String logicId) {
        LogicData newLogic = DataFactory.createData(logicId);
        addLogic(actor, newLogic);
    }

    @Override
    public void addLogic(Entity actor, LogicData logicData) {
        Logic logic = Loader.load(logicData);
        addLogic(actor, logic);
    }

    @Override
    public void addLogic(Entity actor, Logic logic) {
        LogicModule module = actor.getEntityModule().getModule(LogicModule.class);
        module.addLogic(logic);
    }

    @Override
    public boolean removeLogic(Entity actor, Logic logic) {
        LogicModule module = actor.getEntityModule().getModule(LogicModule.class);
        return module != null && module.removeLogic(logic);
    }

    @Override
    public void clearLogics(Entity actor) {
        LogicModule module = actor.getEntityModule().getModule(LogicModule.class);
        if (module == null)
            return;
        
        List<Logic> logics = module.getLogics();
        if (logics != null) {
            List<Logic> tempLogics = new ArrayList<Logic>(logics);
            for (Logic logic : tempLogics) {
                module.removeLogic(logic);
            }
        }
    }

//    @Override
//    public void resetPlayerLogic(Entity actor) {
//        clearLogics(actor);
//        for (String logic : IdConstants.LOGIC_PLAYER) {
//            addLogic(actor, logic);
//        }
//    }

    @Override
    public boolean isAutoLogic(Entity actor) {
        LogicModule module = actor.getEntityModule().getModule(LogicModule.class);
        return module != null && module.isAutoLogic();
    }

    @Override
    public void setAutoLogic(Entity actor, boolean enabled) {
        LogicModule module = actor.getEntityModule().getModule(LogicModule.class);
        if (module != null) {
            module.setAutoLogic(enabled);
        }
    }
    
    @Override
    public boolean isAutoDetect(Entity actor) {
        LogicModule module = actor.getEntityModule().getModule(LogicModule.class);
        return module != null && module.isAutoDetect();
    }
    
    @Override
    public void setAutoDetect(Entity actor, boolean autoDetect) {
        LogicModule module = actor.getEntityModule().getModule(LogicModule.class);
        if (module != null) {
            module.setAutoDetect(autoDetect);
        }
    }
}
