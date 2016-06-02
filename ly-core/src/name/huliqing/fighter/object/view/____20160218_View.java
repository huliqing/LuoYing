///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.view;
//
//import name.huliqing.fighter.data.ViewData;
//import name.huliqing.fighter.object.ProtoObject;
//
///**
// *
// * @author huliqing
// */
//public interface View extends ProtoObject<ViewData> {
//    
//    /**
//     * 获取View数据
//     * @return 
//     */
//    ViewData getData();
//    
//    /**
//     * 开始执行View
//     */
//    void start();
//    
//    /**
//     * 更新View逻辑
//     * @param tpf 
//     */
//    void update(float tpf);
//    
//    /**
//     * 清理数据
//     * 注：cleanup方法一般只清理内部数据释放资源，由外部调用。不要再在cleanup方法在
//     * 调用Service等外部方法，以避免死循环调用，例如在Service中可能又回调cleanup之类的
//     * 循环。
//     */
//    void cleanup();
//    
//}
