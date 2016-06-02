///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.logic;
//
//import name.huliqing.fighter.object.action.AbstractAction;
//import name.huliqing.fighter.data.LogicData;
//import name.huliqing.fighter.loader.Loader;
//
///**
// * IDLE逻辑。
// * 1.该逻辑只有idle行为，不会自动战斗，不会搜索敌人。适合装饰类的小动物.
// * @author huliqing
// */
//public class SimpleLogic extends ActorLogic {
//    private AbstractAction idleAction;
//    
//    public SimpleLogic() {}
//
//    public SimpleLogic(LogicData logicData) {
//        super(logicData);
//        idleAction = Loader.loadAction(logicData.getProto().getAttribute("idleAction"));
//    }
//
//    public AbstractAction getIdleAction() {
//        return idleAction;
//    }
//
//    public void setIdleAction(AbstractAction idleAction) {
//        this.idleAction = idleAction;
//    }
//
//    @Override
//    protected void doLogic(float tpf) {
//        playAction(idleAction);
//    }
//}
