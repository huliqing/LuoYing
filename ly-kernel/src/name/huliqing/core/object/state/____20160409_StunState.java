package name.huliqing.core.object.state;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.state;
//
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.StateData;
//import name.huliqing.fighter.game.service.SkillService;
//
///**
// * 晕眩状态，这个状态会锁住角色所有技能．
// * @deprecated 
// * @author huliqing
// */
//public class StunState extends AbstractState{
//    private final SkillService skillService = Factory.get(SkillService.class);
//
//    public StunState(StateData data) {
//        super(data);
//    }
//
//    @Override
//    protected void doInit() {
//        // 让角色处于reset状态，然后锁定所有技能
//        skillService.playReset(actor, null, true);
//        skillService.lockSkillAll(actor);
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
//        // 解锁所有技能
//        skillService.unlockSkillAll(actor);
//    }
//    
//}
