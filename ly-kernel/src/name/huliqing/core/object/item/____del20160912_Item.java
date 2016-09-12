///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.item;
//
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Spatial;
//
///**
// * @deprecated 20160310不再使用
// * 简单基本物体定义
// * @author huliqing
// */
//public interface Item {
//    
//    /**
//     * 获取物体原始模型，如果物体不存在模型，则返回null.
//     * @return 
//     */
//    Spatial getModel();
//    
//    /**
//     * 给物体添加逻辑
//     * @param logic 
//     */
//    void addLogic(ItemLogic logic);
//    
//    /**
//     * 清理所有逻辑
//     */
//    void clearLogics();
//    
//    /**
//     * 获取物体的实时世界坐标。
//     * @return 
//     */
//    Vector3f getLocation();
//    
//    /**
//     * 设置物体的坐标
//     * @param location 
//     */
//    void setLocation(Vector3f location);
//    
//    /**
//     * 设置物体的旋转
//     * @param quaternion 
//     */
//    void setRotation(Quaternion quaternion);
//}
