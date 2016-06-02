///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader;
//
//import name.huliqing.fighter.data.MagicData;
//import name.huliqing.fighter.object.magic.Magic;
//import name.huliqing.fighter.object.magic.SimpleMagic;
//
///**
// * @author huliqing
// */
//class MagicLoader {
//    
//    public static Magic load(MagicData data) {
//        
//        String tagName = data.getProto().getTagName();
//        if (tagName.equals("magic")) {
//            return new SimpleMagic(data);
//        }
//        
//        throw new UnsupportedOperationException("MagicLoader Unknow type, tagName=" + tagName);
//    }
//}
