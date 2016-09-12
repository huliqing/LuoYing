///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.item;
//
//import com.jme3.renderer.RenderManager;
//import com.jme3.renderer.ViewPort;
//import com.jme3.scene.control.AbstractControl;
//import com.jme3.util.SafeArrayList;
//import name.huliqing.core.object.PlayObject;
//
///**
// * @deprecated 20160310不再使用
// * @author huliqing
// */
//public class ItemLogicControl extends AbstractControl {
//
//    private SafeArrayList<ItemLogic> logics;
//
//    /**
//     * 添加逻辑
//     * @param logic 
//     */
//    void addLogic(ItemLogic logic) {
//        if (logics == null) {
//            logics = new SafeArrayList<ItemLogic>(ItemLogic.class);
//        }
//        logics.add(logic);
//    }
//    
//    /**
//     * 清理所有逻辑
//     */
//    void clearLogics() {
//        if (logics != null) {
//            logics.clear();
//        }
//    }
//
//    @Override
//    protected void controlUpdate(float tpf) {
//        if (logics != null) {
//            for (PlayObject logic : logics.getArray()) {
//                logic.update(tpf);
//            }
//        }
//    }
//
//    @Override
//    protected void controlRender(RenderManager rm, ViewPort vp) {}
//    
//}
