///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader.data;
//
//import name.huliqing.fighter.data.Proto;
//import name.huliqing.fighter.data.ResistData;
//
///**
// *
// * @author huliqing
// */
//public class ResistDataLoader implements DataLoader<ResistData> {
//
//    @Override
//    public ResistData loadData(Proto proto) {
//        ResistData data = new ResistData(proto.getId());
//        // 默认抵抗率为0,也就是如果没有设置，也就没有抵抗
//        data.setFactor(proto.getAsFloat("factor", 0));
//        return data;
//    }
//    
//}
