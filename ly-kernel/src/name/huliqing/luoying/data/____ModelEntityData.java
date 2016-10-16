package name.huliqing.luoying.data;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.ly.data;
//
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.queue.RenderQueue.ShadowMode;
//
///**
// * 场景物体的数据
// * @author huliqing
// */
//public class ModelEntityData extends EntityData {
//    
//    private final static transient Vector3f DEFAULT_LOCATION = new Vector3f(0, 0, 0);
//    private final static transient Quaternion DEFAULT_ROTATION = new Quaternion();
//    private final static transient Vector3f DEFAULT_SCALE = new Vector3f(1,1,1);
//    
//     /**
//     * 获取物体在场景中的位置
//     * @return 
//     */
//    public Vector3f getLocation() {
//        return getAsVector3f("location", DEFAULT_LOCATION);
//    }
//    
//    /**
//     * 设置物体在场景中的位置
//     * @param location 
//     */
//    public void setLocation(Vector3f location) {
//        setAttribute("location", location);
//    }
//    
//    /**
//     * 获取物体在场景中的旋转变换
//     * @return 
//     */
//    public Quaternion getRotation() {
//        return getAsQuaternion("rotation", DEFAULT_ROTATION);
//    }
//    
//    /**
//     * 设置物体在场景中的旋转变换
//     * @param rotation 
//     */
//    public void setRotation(Quaternion rotation) {
//        setAttribute("rotation", rotation);
//    }
//    
//    /**
//     * 获取物体在场景中的缩放
//     * @return 
//     */
//    public Vector3f getScale() {
//        return getAsVector3f("scale", DEFAULT_SCALE);
//    }
//    
//    /**
//     * 设置物体在场景中的缩放
//     * @param scale 
//     */
//    public void setScale(Vector3f scale) {
//        setAttribute("scale", scale);
//    }
//   
//    public ShadowMode getShadowMode() {
//        return identifyShadowMode(getAsString("shadowMode"));
//    }
//    
//    public void setShadowMode(ShadowMode shadowMode) {
//        if (shadowMode == null) {
//            setAttribute("shadowMode", null);
//        } else {
//            setAttribute("shadowMode", shadowMode.name());
//        }
//    }
//    
//    private ShadowMode identifyShadowMode(String shadowMode) {
//        if (shadowMode == null) {
//            return null;
//        }
//        for (ShadowMode sm : ShadowMode.values()) {
//            if (sm.name().equals(shadowMode)) {
//                return sm;
//            }
//        }
//        return null;
//    }
//}
