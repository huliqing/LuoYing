/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actorlogic.ActorLogic;
import name.huliqing.core.object.control.ActorLogicControl;

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
    public ActorLogic loadLogic(String logicId) {
        return Loader.loadLogic(logicId);
    }

    @Override
    public boolean addLogic(Actor actor, String logicId) {
        ActorLogicData newLogic = DataFactory.createData(logicId);
        return addLogic(actor, newLogic);
    }

    @Override
    public boolean addLogic(Actor actor, ActorLogicData logicData) {
        ActorLogic logic = Loader.loadLogic(logicData);
        return addLogic(actor, logic);
    }

    @Override
    public boolean addLogic(Actor actor, ActorLogic logic) {
        ActorLogicControl control = actor.getModel().getControl(ActorLogicControl.class);
        control.addLogic(logic);
        return true;
    }

    @Override
    public boolean removeLogic(Actor actor, ActorLogic logic) {
        ActorLogicControl control = actor.getModel().getControl(ActorLogicControl.class);
        return control.removeLogic(logic);
    }

    @Override
    public void clearLogics(Actor actor) {
        ActorLogicControl control = actor.getModel().getControl(ActorLogicControl.class);
        if (control == null)
            return;
        
        List<ActorLogic> logics = control.getLogics();
        if (logics != null) {
            List<ActorLogic> tempLogics = new ArrayList<ActorLogic>(logics);
            for (ActorLogic logic : tempLogics) {
                control.removeLogic(logic);
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
    
}
