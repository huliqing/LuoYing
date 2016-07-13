///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader.data;
//
//import name.huliqing.fighter.data.Proto;
//import name.huliqing.fighter.data.SlotData;
//
///**
// *
// * @author huliqing
// */
//public class SlotDataLoader implements DataLoader<SlotData>{
//
//    @Override
//    public SlotData loadData(Proto proto) {
//        SlotData data = new SlotData(proto.getId());
//        
//        data.setBindBone(proto.getAttribute("bindBone"));
//        data.setLocalTranslation(proto.getAsVector3f("localTranslation"));
//        data.setLocalRotation(proto.getAsFloatArray("localRotation"));
//        data.setLocalScale(proto.getAsVector3f("localScale"));
//        data.setLeftHandSkinSkill(proto.getAttribute("leftHandSkinSkill"));
//        data.setRightHandSkinSkill(proto.getAttribute("rightHandSkinSkill"));
//        return data;
//    }
//    
//}
