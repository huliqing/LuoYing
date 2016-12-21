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
//import com.jme3.math.FastMath;
//import java.util.List;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.data.StateData;
//import name.huliqing.luoying.layer.network.EntityNetwork;
//import name.huliqing.luoying.layer.service.DefineService;
//import name.huliqing.luoying.object.module.SkillModule;
//import name.huliqing.luoying.object.skill.Skill;
//
///**
// * 让目标角色执行一个技能
// * @author huliqing
// */
//public class SkillState extends AbstractState {
//    private final DefineService defineService = Factory.get(DefineService.class);
//    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
//    private SkillModule skillModule;
//    
//    private long skillTypes;
//
//    @Override
//    public void setData(StateData data) {
//        super.setData(data);
//        skillTypes = defineService.getSkillTypeDefine().convert(data.getAsArray("skillTypes"));
//        
//        // remove20161217
//        // 不再支持这个参数
////        force = data.getAsBoolean("force", force);
//    }
//    
//    @Override
//    public void initialize() {
//        super.initialize();
//        skillModule = entity.getModuleManager().getModule(SkillModule.class);
//        
//        if (skillModule == null)
//            return;
//        
//        List<Skill> tagSkills = skillModule.getSkillByTypes(skillTypes, null);
//        if (tagSkills == null || tagSkills.isEmpty())
//            return;
//        
//        // 这里使用随机数，所以要用skillNetwork.
////        skillNetwork.playSkill(entity, tagSkills.get(FastMath.nextRandomInt(0, tagSkills.size() - 1)), force);
//        
//        Skill skill = tagSkills.get(FastMath.nextRandomInt(0, tagSkills.size() - 1));
//        entityNetwork.useObjectData(entity, skill.getData().getUniqueId());
//    }
//    
//}
