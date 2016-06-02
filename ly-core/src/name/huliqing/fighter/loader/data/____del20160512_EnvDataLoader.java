///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader.data;
//
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import name.huliqing.fighter.data.EnvData;
//import name.huliqing.fighter.data.Proto;
//import name.huliqing.fighter.utils.MathUtils;
//
///**
// *
// * @author huliqing
// */
//public class EnvDataLoader implements DataLoader<EnvData>{
//
//    @Override
//    public EnvData loadData(Proto proto) {
//        EnvData data = new EnvData(proto.getId());
//        Vector3f location = proto.getAsVector3f("location");
//        if (location != null) {
//            data.setLocation(location);
//        }
//        Quaternion rotation = proto.getAsQuaternion("rotation");
//        if (rotation != null) {
//            data.setRotation(rotation);
//        }
//        Vector3f scale = proto.getAsVector3f("scale");
//        if (scale != null) {
//            data.setScale(scale);
//        }
//        data.setMass(proto.getAsFloat("mass", 0));
//        data.setPhysics(proto.getAsBoolean("physics", false));
//        
//        // Plant
//        String tagName = proto.getTagName();
//        if (tagName.equals("tree") || tagName.equals("grass")) {
//            boolean randomScale = proto.getAsBoolean("randomScale", false);
//            if (randomScale) {
//                float minScale = proto.getAsFloat("minScale", 1);
//                float maxScale = proto.getAsFloat("maxScale", 1);
//                float finalScale = MathUtils.getRandomFloat(minScale, maxScale);
//                data.setScale(new Vector3f(finalScale,finalScale,finalScale));
//            }
//        }
//        
//        return data;
//    }
//    
//}
