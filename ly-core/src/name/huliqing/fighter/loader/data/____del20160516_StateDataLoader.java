///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader.data;
//
//import name.huliqing.fighter.data.Proto;
//import name.huliqing.fighter.data.StateData;
//
///**
// *
// * @author huliqing
// */
//public class StateDataLoader implements DataLoader<StateData>{
//
//    @Override
//    public StateData loadData(Proto proto) {
//        StateData data = new StateData(proto.getId());
//        
//        data.setUseTime(proto.getAsFloat("useTime", 30));
//        data.setInterval(proto.getAsFloat("interval", 3f));
//        data.setEffects(data.getProto().getAsArray("effects"));
//        
//        // remove20160110,不再使用startEffect这个参数
////        data.setStartEffect(proto.getAttribute("startEffect"));
////        data.setAddTime(proto.getAsFloat("addTime", 0.75f));
//        return data;
//    }
//    
//}
