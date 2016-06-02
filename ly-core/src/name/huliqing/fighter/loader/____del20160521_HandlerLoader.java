///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader;
//
//import name.huliqing.fighter.data.HandlerData;
//import name.huliqing.fighter.object.handler.Handler;
//import name.huliqing.fighter.object.handler.OutfitHandler;
//import name.huliqing.fighter.object.handler.SkillBookHandler;
//import name.huliqing.fighter.object.handler.SkillHandler;
//import name.huliqing.fighter.object.handler.StateGainHandler;
//import name.huliqing.fighter.object.handler.SummonHandler;
//import name.huliqing.fighter.object.handler.SummonSkillHandler;
//import name.huliqing.fighter.object.handler.TestHandler;
//import name.huliqing.fighter.object.handler.AttributeHandler;
//import name.huliqing.fighter.object.handler.ItemSkillHandler;
//import name.huliqing.fighter.object.handler.WeaponHandler;
//
///**
// *
// * @author huliqing
// */
//class HandlerLoader {
//    
//    public static Handler load(HandlerData data) {
//        String tagName = data.getTagName();
//        if (tagName.equals("handlerSummon")) {
//            return new SummonHandler(data);
//        } 
//        
//        if (tagName.equals("handlerAttribute")) {
//            return new AttributeHandler(data);
//        } 
//        
//        if (tagName.equals("handlerOutfit")) {
//            return new OutfitHandler(data);
//        }
//        
//        if (tagName.equals("handlerWeapon")) {
//            return new WeaponHandler(data);
//        } 
//        
//        if (tagName.equals("handlerSkill")) {
//            return new SkillHandler(data);
//        }
//        
//        if (tagName.equals("handlerSummonSkill")) {
//            return new SummonSkillHandler(data);
//        }
//        
//        if (tagName.equals("handlerSkillBook")) {
//            return new SkillBookHandler(data);
//        }
//        
//        if (tagName.equals("handlerStateGain")) {
//            return new StateGainHandler(data);
//        } 
//        
//        if (tagName.equals("handlerItemSkill")) {
//            return new ItemSkillHandler(data);
//        }
//        
//        if (tagName.equals("handlerTest")) {
//            return new TestHandler(data);
//        }
//        throw new UnsupportedOperationException("Unknow tagName=" + tagName);
//    }
//}
