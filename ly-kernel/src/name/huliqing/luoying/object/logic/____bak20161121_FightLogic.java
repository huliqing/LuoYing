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
//package name.huliqing.luoying.object.logic;
//
//import name.huliqing.luoying.object.action.FightAction;
//import name.huliqing.luoying.data.LogicData;
//import name.huliqing.luoying.object.Loader;
//import name.huliqing.luoying.object.actor.Actor;
//import name.huliqing.luoying.object.entity.Entity;
//import name.huliqing.luoying.object.module.ActionModule;
//
///**
// * 战斗逻辑
// * @author huliqing
// */
//public class FightLogic extends AbstractLogic {
////    private static final Logger LOG = Logger.getLogger(FightLogic.class.getName());
//    
//    private  FightAction fightAction;
//    private ActionModule actionModule;
//    
//    // ---- inner
//    
//    @Override
//    public void setData(LogicData data) {
//        super.setData(data); 
//        fightAction = (FightAction) Loader.load(data.getAsString("fightAction"));
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        actionModule = actor.getModuleManager().getModule(ActionModule.class);
//    }
//    
//    @Override
//    protected void doLogic(float tpf) {
//        // remove20161102
////        Entity t = actorService.getTarget(actor);
////        if (t != null && !actorService.isDead(t) 
////                && actorService.isEnemy(t, actor) 
////                && t.getScene() != null) {
////            
////            fightAction.setEnemy(t);
////            actionService.playAction(actor, fightAction);
////        }
//        
//        Entity target = getTarget();
//        
//        // 只有Actor才有属性
//        if (!(target instanceof Actor)) {
//            return;
//        }
//        if (target.getScene() == null) {
//            return;
//        }
//        
//        if (isEnemy(target)) {
//            fightAction.setEnemy(target);
//            actionModule.startAction(fightAction);
//        }
//    }
//    
//}
