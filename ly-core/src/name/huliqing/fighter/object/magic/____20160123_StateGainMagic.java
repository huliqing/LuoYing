///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.magic;
//
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.MagicData;
//import name.huliqing.fighter.game.service.StateService;
//import name.huliqing.fighter.utils.MathUtils;
//
///**
// * 用于获得一个状态的魔法
// * @author huliqing
// */
//public class StateGainMagic extends AbstractMagic {
//    private final static StateService stateService = Factory.get(StateService.class);
//    
//    private String state;
//    private float stateTimePoint;
//    
//    // ---- inner
//    private boolean added;
//    private float timeAdd;
//    
//    public StateGainMagic(MagicData data) {
//        super(data);
//        state = data.getProto().getAttribute("state");
//        stateTimePoint = MathUtils.clamp(data.getProto().getAsFloat("stateTimePoint", 0), 0, 1f);
//    }
//
//    @Override
//    protected void doInit() {
//        super.doInit();
//        timeAdd = data.getUseTime() * stateTimePoint;
//    }
//
//    @Override
//    protected void doLogic(float tpf) {
//        if (!added && timeUsed >= timeAdd) {
//            if (target != null) {
//                stateService.addState(target, state);
//            }
//            added = true;
//        }
//    }
//
//    @Override
//    public void cleanup() {
//        added = false;
//        super.cleanup(); 
//    }
//    
//}
