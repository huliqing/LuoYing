///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.action;
//
///**
// * 角色行为逻辑执行器
// * @author huliqing
// */
//public class ActionProcessor {
//    
//    // 两个默认行为,当角色接收玩家控制时需要这两个默认行为
//    // see ActionServcice.playRun,playFight
//    private RunAction defRunAction;
//    private FightAction defFightAction;
//    
//    // 当前正在执行的行为逻辑
//    private Action current;
//    
//    public ActionProcessor() {}
//    
//    /**
//     * 更新行为逻辑
//     * @param tpf 
//     */
//    public void update(float tpf) {
//        if (current != null) {
//            if (current.isEnd()) {
//                current.cleanup();
//                current = null;
//            } else {
//                current.update(tpf);
//            }
//        }
//    }
//    
//    /**
//     * 执行行为逻辑，如果当前没有正在执行的逻辑，则立即执行．否则偿试打断正在执
//     * 行的逻辑，如果打断成功，则执行新逻辑，否则直接返回．
//     * @param action 当为null时，将打断当前行为。
//     */
//    public void startAction(Action action) {
//        if (current == action) {
//            return;
//        }
//        
//        if (current != null) {
//            current.cleanup();
//        }
//        
//        current = action;
//        if (current != null) {
//            current.initialize();
//        }
//    }
//    
//    /**
//     * 获取当前正在执行的行为,如果没有则返回null.
//     * @return 
//     */
//    public Action getAction() {
//        return current;
//    }
//
//    public RunAction getDefRunAction() {
//        return defRunAction;
//    }
//
//    public void setDefRunAction(RunAction defRunAction) {
//        this.defRunAction = defRunAction;
//    }
//
//    public FightAction getDefFightAction() {
//        return defFightAction;
//    }
//
//    public void setDefFightAction(FightAction defFightAction) {
//        this.defFightAction = defFightAction;
//    }
//    
//    public void cleanup() {
//        
//        // remove20151231,暂不支持cleanup,可能部分子类需要在cleanup中调用service,暂未想好如何处理。
////        if (current != null) {
////            current.cleanup();
////            current = null;
////        }
//    }
//}
