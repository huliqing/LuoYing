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
//package name.huliqing.luoying.object.state;
//
//import name.huliqing.luoying.data.StateData;
//import name.huliqing.luoying.object.module.ActorModule;
//
///**
// * @author huliqing
// */
//public class EssentialState extends AbstractState {
//    private ActorModule actorModule;
//    
//    private boolean essential;
//    private boolean restore;
//    private String bindEssentialAttribute;
//    
//    // ---- 保存原始状态以便恢复
//    private boolean oldEssential;
//
//    @Override
//    public void setData(StateData data) {
//        super.setData(data);
//        this.essential = data.getAsBoolean("essential", essential);
//        this.restore = data.getAsBoolean("restore", restore);
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        actorModule = actor.getModuleManager().getModule(ActorModule.class);
//        
//        oldEssential = actorModule.isEssential();
//        actorModule.setEssential(essential);
//    }
//
//    @Override
//    public void cleanup() {
//        if (restore && actorModule != null) {
//            actorModule.setEssential(oldEssential);
//        }
//        super.cleanup();
//    }
//    
//}
