///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader;
//
//import name.huliqing.fighter.object.action.AbstractAction;
//import name.huliqing.fighter.object.action.impl.FightDynamicAction;
//import name.huliqing.fighter.object.action.impl.IdlePatrolAction;
//import name.huliqing.fighter.object.action.impl.FollowPathAction;
//import name.huliqing.fighter.object.action.impl.IdleDynamicAction;
//import name.huliqing.fighter.object.action.impl.RunPathAction;
//import name.huliqing.fighter.object.action.impl.RunSimpleAction;
//import name.huliqing.fighter.object.action.impl.IdleStaticAction;
//import name.huliqing.fighter.data.ActionData;
//
///**
// * @author huliqing
// */
//class ActionLoader {
//    
//    public static AbstractAction load(ActionData ad) {
//        // 1.如果指定了特定的处理类
//        String clazz = ad.getAttribute("class");
//        if ("RunSimpleAction".equals(clazz)) {
//            return new RunSimpleAction(ad);
//        }
//        
//        // 2.根据tag name自动判断
//        String tn = ad.getProto().getTagName();
//        
//        if (tn.equals("idleStaticAction")) {
//            
//            return new IdleStaticAction(ad);
//            
//        } else if (tn.equals("idleDynamicAction")) {
//            
//            return new IdleDynamicAction(ad);
//            
//        } else if (tn.equals("idlePatrolAction")) {
//            
//            return new IdlePatrolAction(ad);
//            
//        } else if (tn.equals("runAction")) {
//            
//            return new RunPathAction(ad);
//            
//        } else if (tn.equals("fightAction")) {
//            
//            return new FightDynamicAction(ad);
//
//        } else if (tn.equals("followAction")) {
//            
//            return new FollowPathAction(ad);
//            
//        } 
//        throw new UnsupportedOperationException("Unknow tagName=" + tn);
//    }
//}
