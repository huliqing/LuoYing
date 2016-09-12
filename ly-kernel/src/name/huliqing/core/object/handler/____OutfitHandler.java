///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.handler;
//
//import name.huliqing.core.Factory;
//import name.huliqing.core.GameException;
//import name.huliqing.core.object.actor.Actor;
//import name.huliqing.core.data.ObjectData;
//import name.huliqing.core.data.SkinData;
//import name.huliqing.core.mvc.service.SkinService;
//
///**
// * 处理装备的使用,用于切换装备
// * @author huliqing
// */
//public class OutfitHandler extends AbstractSkinHandler {
//    private final SkinService skinService = Factory.get(SkinService.class);
//
//    @Override
//    public boolean canUse(Actor actor, ObjectData data) {
//        if (!super.canUse(actor, data)) {
//            return false;
//        }
//        
//        // not a skin
//        if (!(data instanceof SkinData)) {
//            return false;
//        }
//
//        // remove20151204,现在装备可穿可脱
//        // in using
////        SkinData skinData = (SkinData) data;
////        if (skinData.isUsing()) {
////            return false;
////        }
//        
//        return true;
//    }
//
//    @Override
//    protected void useObject(Actor actor, ObjectData pd) {
//        SkinData skinData = (SkinData) pd;
//        if (skinData.isUsed()) {
//            // 脱装备
//            skinService.detachSkin(actor, skinData);
//        } else {
//            // 穿装备
//            skinService.attachSkin(actor, skinData);
//        }
//    }
//
//    @Override
//    public boolean remove(Actor actor,  ObjectData data, int count) throws GameException {
//        if (!(data instanceof SkinData)) {
////            logger.log(Level.WARNING, "OutfitHandler only supported Skin type objects");
//            return false;
//        }
//        SkinData skinData = (SkinData) data;
//        if (skinData.isUsed()) {
//            return false;
//        }
//        return super.remove(actor, data, data.getTotal());
//    }
//    
//    
//}
