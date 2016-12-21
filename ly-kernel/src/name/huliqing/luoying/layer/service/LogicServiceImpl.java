/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.layer.service;

import java.util.ArrayList;
import java.util.List;
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
        LogicModule module = actor.getModuleManager().getModule(LogicModule.class);
        module.addLogic(logic);
    }

    @Override
    public boolean removeLogic(Entity actor, Logic logic) {
        LogicModule module = actor.getModuleManager().getModule(LogicModule.class);
        return module != null && module.removeLogic(logic);
    }

    @Override
    public void clearLogics(Entity actor) {
        LogicModule module = actor.getModuleManager().getModule(LogicModule.class);
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
//
//    @Override
//    public boolean isAutoLogic(Entity actor) {
//        LogicModule module = actor.getModuleManager().getModule(LogicModule.class);
//        return module != null && module.isAutoLogic();
//    }
//
//    @Override
//    public void setAutoLogic(Entity actor, boolean enabled) {
//        LogicModule module = actor.getModuleManager().getModule(LogicModule.class);
//        if (module != null) {
//            module.setAutoLogic(enabled);
//        }
//    }
//    
//    @Override
//    public boolean isAutoDetect(Entity actor) {
//        LogicModule module = actor.getModuleManager().getModule(LogicModule.class);
//        return module != null && module.isAutoDetect();
//    }
//    
//    @Override
//    public void setAutoDetect(Entity actor, boolean autoDetect) {
//        LogicModule module = actor.getModuleManager().getModule(LogicModule.class);
//        if (module != null) {
//            module.setAutoDetect(autoDetect);
//        }
//    }
}
