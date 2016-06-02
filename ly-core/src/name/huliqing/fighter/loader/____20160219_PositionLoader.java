///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader;
//
//import com.jme3.effect.shapes.EmitterBoxShape;
//import com.jme3.effect.shapes.EmitterPointShape;
//import com.jme3.effect.shapes.EmitterShape;
//import com.jme3.effect.shapes.EmitterSphereShape;
//import name.huliqing.fighter.data.PositionData;
//import name.huliqing.fighter.object.position.EmitterCircle;
//import name.huliqing.fighter.object.position.EmitterCirclePlaneShape;
//import name.huliqing.fighter.object.position.EmitterCircleShape;
//import name.huliqing.fighter.object.position.EmitterCylinderShape;
//
///**
// *
// * @author huliqing
// */
//class PositionLoader {
//    
//    public static EmitterShape load(PositionData data) {
//        String tagName = data.getTagName();
//        if ("point".equals(tagName)) {
//            return createPointShape(data);
//        } else if ("box".equals(tagName)) {
//            return createBoxShape(data);
//        } else if ("sphere".equals(tagName)) {
//            return createSphereShape(data);
//        } else if ("circle".equals(tagName)) {
//            return new EmitterCircle(data);
//        }
//        
//        // out date,20150515
//        else if ("cylinder".equals(tagName)) {
//            return new EmitterCylinderShape(data);
//        } else if ("circleOld".equals(tagName)) {
//            return new EmitterCircleShape(data);
//        } else if ("circlePlane".equals(tagName)) {
//            return new EmitterCirclePlaneShape(data);
//        }
//        return null;
//    }
//    
//    private static EmitterShape createPointShape(PositionData esd) {
//        EmitterPointShape eps = new EmitterPointShape(esd.getAsVector3f("point"));
//        return eps;
//    }
//    
//    private static EmitterShape createBoxShape(PositionData esd) {
//        EmitterBoxShape ebs = new EmitterBoxShape(esd.getAsVector3f("min")
//                , esd.getAsVector3f("max"));
//        return ebs;
//    }
//    
//    private static EmitterShape createSphereShape(PositionData esd) {
//        EmitterSphereShape ebs = new EmitterSphereShape(esd.getAsVector3f("center")
//                , esd.getAsFloat("radius", 1));
//        return ebs;
//    }
//    
// 
//}
