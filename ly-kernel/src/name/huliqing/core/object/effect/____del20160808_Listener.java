///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.effect;
//
///**
// *
// * @author huliqing
// */
//public interface Listener {
//    
//    /**
//     * 当效果执行过程中触发,包含所有各个阶段。
//     * (注意：在该方法内更新effect的状态将可能影响“效果”的正常运行)。
//     * @param effect 
//     */
//    void onEffectPlay(Effect effect);
//    
//    /**
//     * 当效果执行结束的时候触发,只在结束时触发一次，是在end阶段执行完之后
//     * cleanup之前执行。 ... -> end -> (onEffectEnd) -> cleanup
//     * (注意：在该方法内更新effect的状态将可能影响“效果”的正常运行)。
//     * @param effect 
//     */
//    void onEffectEnd(Effect effect);
//    
//}
