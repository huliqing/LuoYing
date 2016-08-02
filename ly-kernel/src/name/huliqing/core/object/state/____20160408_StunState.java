package name.huliqing.core.object.state;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.state;
//
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.StateData;
//import name.huliqing.fighter.game.network.SkillNetwork;
//import name.huliqing.fighter.object.state.AbstractState;
//
///**
// * 晕眩状态，这个状态会锁住角色所有技能．
// * @author huliqing
// */
//public class StunState extends AbstractState{
//    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
//
//    public StunState(StateData data) {
//        super(data);
//    }
//
//    @Override
//    protected void doInit() {
//        
//        skillNetwork.playReset(actor, null, true);
//        skillNetwork.setLocked(actor, true);
//        
//        // 削弱时间
//        totalUseTime = data.getUseTime() - data.getUseTime() * data.getResist();
//    }
//
//    @Override
//    protected void doLogic(float tpf) {}
//
//    @Override
//    protected void doCleanup() {
//        skillNetwork.setLocked(actor, false);
//    }
//    
//}
