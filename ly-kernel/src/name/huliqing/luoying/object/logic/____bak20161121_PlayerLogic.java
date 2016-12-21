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
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.object.action.FightAction;
//import name.huliqing.luoying.data.LogicData;
//import name.huliqing.luoying.layer.service.ActionService;
//import name.huliqing.luoying.object.Loader;
//import name.huliqing.luoying.object.actor.Actor;
//import name.huliqing.luoying.object.entity.Entity;
//
///**
// * 只用于player角色的逻辑,不会有idle行为，因为不让玩角角色在停下来的时候或
// * 没有目标的时候做idle行为
// * @author huliqing
// */
//public class PlayerLogic extends AbstractLogic {
//    private final ActionService actionService = Factory.get(ActionService.class);
//    
//    private FightAction fightAction;
//    
//    // ---- inner
//    private float viewDistanceSquared;
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
//        if (viewAttribute != null) {
//            viewDistanceSquared = viewAttribute.floatValue() * viewAttribute.floatValue();
//        }
//    }
//    
//    @Override
//    protected void doLogic(float tpf) {
//        if (targetAttribute == null || viewAttribute == null) {
//            return;
//        }
//        
//        // 如果玩家正在控制走路，则不执行逻辑
//        if (actionService.isPlayingRun(actor)) {
//            return;
//        }
//        
//        // 目标不是角色
//        Entity target = getTarget();
//        if (!(target instanceof Actor)) {
//            return;
//        }
//        
//        // 已经从场景移除
//        if (target.getScene() == null) {
//            return;
//        }
//        
//        // 目标在视线之外
//        if (target.getSpatial().getWorldTranslation().distanceSquared(actor.getSpatial().getWorldTranslation()) > viewDistanceSquared) {
//            return;
//        }
//        
//        // 非敌人
//        if (!isEnemy(target)) {
//            return;
//        }
//        
//        // <<<战斗开始>>>
//        fightAction.setEnemy(target);
//        actionService.playAction(actor, fightAction);
//        
//    }
//    
//}
