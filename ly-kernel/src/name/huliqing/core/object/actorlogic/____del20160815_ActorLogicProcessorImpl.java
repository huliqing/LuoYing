///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.actorlogic;
//
//import com.jme3.app.Application;
//import com.jme3.util.SafeArrayList;
//import name.huliqing.core.object.actor.Actor;
//
///**
// *
// * @author huliqing
// */
//public class ActorLogicProcessorImpl implements ActorLogicProcessor {
//    
//    private final Application app;
//    private final Actor actor;
//    private final SafeArrayList<ActorLogic> logics = new SafeArrayList<ActorLogic>(ActorLogic.class);
//
//    public ActorLogicProcessorImpl(Application app, Actor actor) {
//        this.app = app;
//        this.actor = actor;
//    }
//    
//    @Override
//    public void update(float tpf) {
//        for (ActorLogic logic : logics.getArray()) {
//            logic.update(tpf);
//        }
//    }
//
//    @Override
//    public void addLogic(ActorLogic logic) {
//        if (!logics.contains(logic)) {
//            logic.setActor(actor);
//            logic.initialize(app);
//            logics.add(logic);
//        }
//    }
//
//    @Override
//    public boolean removeLogic(ActorLogic logic) {
//        if (!logics.contains(logic))
//            return false;
//        
//        logic.cleanup();
//        return logics.remove(logic);
//    }
//
//    @Override
//    public void cleanup() {
//        for (ActorLogic logic : logics) {
//            logic.cleanup();
//        }
//        logics.clear();
//    }
//    
//}
