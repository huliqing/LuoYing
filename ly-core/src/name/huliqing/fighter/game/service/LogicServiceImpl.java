/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import java.util.List;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.DataLoaderFactory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.logic.ActorLogic;

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
        LogicData newLogic = DataLoaderFactory.createLogicData(logicId);
        return addLogic(actor, newLogic);
    }

    @Override
    public boolean addLogic(Actor actor, LogicData logicData) {
        ActorLogic logic = Loader.loadLogic(logicData);
        return addLogic(actor, logic);
    }

    @Override
    public boolean addLogic(Actor actor, ActorLogic logic) {
        LogicData data = logic.getData();
        List<LogicData> logics = actor.getData().getLogics();
        if (logics.contains(data)) {
            return false; // 已经存在
        }
        logics.add(data);
        actor.getLogicProcessor().addLogic(logic);// 直接添加
        return true;
    }

    @Override
    public boolean removeLogic(Actor actor, ActorLogic logic) {
        LogicData data = logic.getData();
        List<LogicData> datas = actor.getData().getLogics();
        if (!datas.contains(data)) { // 角色身上没有指定逻辑
            return false;
        }
        datas.remove(data);
        return actor.getLogicProcessor().removeLogic(logic);
    }

    @Override
    public void clearLogics(Actor actor) {
        actor.getData().getLogics().clear();
        actor.getLogicProcessor().cleanup();
    }

    @Override
    public void resetPlayerLogic(Actor actor) {
        clearLogics(actor);
        for (String logic : IdConstants.LOGIC_PLAYER) {
            addLogic(actor, logic);
        }
    }
    
}
