///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader;
//
//import com.jme3.bullet.control.RigidBodyControl;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Spatial;
//import name.huliqing.fighter.manager.ResourceManager;
//import name.huliqing.fighter.data.ItemData;
//import name.huliqing.fighter.data.Proto;
//import name.huliqing.fighter.object.item.Item;
//import name.huliqing.fighter.object.item.SimpleItem;
//
///**
// * @deprecated 20160310不再使用
// * @author huliqing
// */
//class ItemLoader {
//    
////    public static Item loadObj(ItemData data) {
////        Proto proto = data.getProto();
////        
////        Spatial model = AssetLoader.loadModel(proto.getFile());
////        model.setName(ResourceManager.getObjectName(data));
////        String tempScale = proto.getAttribute("scale", null);
////        Vector3f scale;
////        if (tempScale != null) {
////            if (tempScale.split(",").length >= 3) {
////                scale = proto.getAsVector3f("scale", new Vector3f(1,1,1));
////            } else {
////                float factor = proto.getAsFloat("scale", 1.0f);
////                scale = new Vector3f(factor, factor, factor);
////            }
////        } else {
////            scale = new Vector3f(1,1,1);
////        }
////        model.setLocalScale(scale);
//        
////        boolean physics = proto.getAsBoolean("physics", false);
////        float mass = proto.getAsFloat("mass", 0);
////        if (physics) {
////            model.addControl(new RigidBodyControl(mass));
////        }
////        Item obj = new SimpleItem(model);
////        return obj;
////    }
//   
//}
