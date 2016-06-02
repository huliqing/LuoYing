///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader;
//
//import name.huliqing.fighter.data.StateData;
//import name.huliqing.fighter.object.state.State;
//import name.huliqing.fighter.object.state.AttributeState;
//import name.huliqing.fighter.object.state.EssentialState;
//import name.huliqing.fighter.object.state.MoveSpeedState;
//import name.huliqing.fighter.object.state.SkillLockedState;
//import name.huliqing.fighter.object.state.SkillState;
//
///**
// *
// * @author huliqing
// */
//class StateLoader {
//    
//    public static State load(StateData data) {
//        String tagName = data.getTagName();
//        
//        if (tagName.equals("stateAttribute")) {
//            return new AttributeState(data);
//        } 
//        
//        // 移动速度
//        if (tagName.equals("stateAttributeMove")) { 
//            return new MoveSpeedState(data);
//        } 
//        
//        // 技能锁定
//        if (tagName.equals("stateSkillLocked")) { 
//            return new SkillLockedState(data);
//        }
//        
//        if (tagName.equals("stateEssential")) {
//            return new EssentialState(data);
//        } 
//        
//        // 执行一个技能
//        if (tagName.equals("stateSkill")) {
//            return new SkillState(data);
//        } 
//        
//        throw new UnsupportedOperationException("Unknow state tagName:" + tagName);
//        
//    }
//   
//}
