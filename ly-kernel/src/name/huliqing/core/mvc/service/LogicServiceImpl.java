/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.data.LogicData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.logic.Logic;
import name.huliqing.core.object.module.LogicModule;

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
        return Loader.loadLogic(logicId);
    }

    @Override
    public void addLogic(Actor actor, String logicId) {
        LogicData newLogic = DataFactory.createData(logicId);
        addLogic(actor, newLogic);
    }

    @Override
    public void addLogic(Actor actor, LogicData logicData) {
        Logic logic = Loader.loadLogic(logicData);
        addLogic(actor, logic);
    }

    @Override
    public void addLogic(Actor actor, Logic logic) {
        LogicModule module = actor.getModule(LogicModule.class);
        module.addLogic(logic);
    }

    @Override
    public boolean removeLogic(Actor actor, Logic logic) {
        LogicModule module = actor.getModule(LogicModule.class);
        return module != null && module.removeLogic(logic);
    }

    @Override
    public void clearLogics(Actor actor) {
        LogicModule module = actor.getModule(LogicModule.class);
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

    @Override
    public void resetPlayerLogic(Actor actor) {
        clearLogics(actor);
        for (String logic : IdConstants.LOGIC_PLAYER) {
            addLogic(actor, logic);
        }
    }

    @Override
    public boolean isAutoLogic(Actor actor) {
        LogicModule module = actor.getModule(LogicModule.class);
        return module != null && module.isAutoLogic();
    }

    @Override
    public void setAutoLogic(Actor actor, boolean enabled) {
        LogicModule module = actor.getModule(LogicModule.class);
        if (module != null) {
            module.setAutoLogic(enabled);
        }
    }
    
    @Override
    public boolean isAutoDetect(Actor actor) {
        LogicModule module = actor.getModule(LogicModule.class);
        return module != null && module.isAutoDetect();
    }
    
    @Override
    public void setAutoDetect(Actor actor, boolean autoDetect) {
        LogicModule module = actor.getModule(LogicModule.class);
        if (module != null) {
            module.setAutoDetect(autoDetect);
        }
    }
}
