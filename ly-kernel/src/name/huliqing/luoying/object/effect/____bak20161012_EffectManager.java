package name.huliqing.luoying.object.effect;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.ly.object.effect;
//
//import com.jme3.app.Application;
//import com.jme3.app.SimpleApplication;
//import com.jme3.app.state.AbstractAppState;
//import com.jme3.app.state.AppStateManager;
//import com.jme3.scene.Node;
//import com.jme3.util.SafeArrayList;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 特效管理器,对于添加到EffectManager中的特效可以进行自动管理,当特效结束的时候EffectManager会将特效从场景中移除。
// * @author huliqing
// */
//public class EffectManager extends AbstractAppState {
//    
//    private final static EffectManager INSTANCE = new EffectManager();
//    
//    // 等待添加到effectRoot中的效果,这里不是直接添加到effectRoot，因为在添加到effectRoot之前要确保effect进行了初始化
//    // initialize.
//    private final List<Effect> initializing = new ArrayList<Effect>();
//    
//    // 特效根节点，这个根节点是所有添加到EffectManager的特效的父节点，执行所有特效的更新等。
//    private Node effectRoot;
//    // 特效列表，维持一个额外的特效列表，runningEffects中的特效和effectRoot中的特效是一样的，
//    // 这个列表只为方便直接获得特效列表的引用。
//    private final SafeArrayList<Effect> runningEffects = new SafeArrayList<Effect>(Effect.class);
//    
//    private EffectManager() {}
//    
//    public static EffectManager getInstance() {
//        return INSTANCE;
//    }
//
//    @Override
//    public void initialize(AppStateManager stateManager, Application app) {
//        super.initialize(stateManager, app);
//        // 如果没有指定一个特效根节点，则生成一个，并偿试添加到SimpleApplication类型的根节点下面。
//        // 这要求目标application必须是SimpleApplication类型，否则报错。
//        if (effectRoot == null) {
//            if (app instanceof SimpleApplication) {
//                effectRoot = new Node("EffectRoot");
//                ((SimpleApplication) app).getRootNode().attachChild(effectRoot);
//            } else {
//                throw new IllegalStateException("EffectManager need a parent node to attach effectRoot node, but this"
//                        + " application type could not support! or esle you can specify a custom effectRoot before the "
//                        + "EffectManager initialize. Current application=" + app.getClass());
//            }
//        }
//    }
//    
//    @Override
//    public void update(float tpf) {
//        super.update(tpf);
//        
//        synchronized (initializing) {
//            // 将initializing中的effect初始化后添加到effectRoot中，然后清空列表
//            if (!initializing.isEmpty()) {
//                for (int i = 0; i < initializing.size(); i++) {
//                    Effect effect = initializing.get(i);
//                    // 在initialize之前应该先将effect添加到场景中,因为initialize某些实现可能依赖于父节点。
//                    runningEffects.add(effect);
//                    effectRoot.attachChild(effect);
//                    
//                    // 这里要判断一个以避免重复初始化，因为effect有可能从外部调用initialize方法
//                    if (!effect.isInitialized()) {
//                        effect.initialize();
//                    }
//                }
//                initializing.clear();
//            }
//        }
//        
//        // 检查并移除已经执行完的特效。
//        if (!runningEffects.isEmpty()) {
//            for (Effect e : runningEffects.getArray()) {
//                if (e.isEnd()) {
//                    removeEffect(e);
//                }
//            }
//        }
//    }
//    
//    /**
//     * 清理掉当前场景中的所有特效。
//     */
//    @Override
//    public void cleanup() {
//        // 清理initializing列表，默认情况下initializing中的effect为未初始化的，但:有可能一些effect会从外部调用initialize方法，
//        // 则这些从外部初始化的效果也要cleanup,以确保不会内存涉漏.
//        synchronized (initializing) {
//            for (Effect e : initializing) {
//                if (e.isInitialized()) {
//                    e.cleanup();
//                }
//            }
//            initializing.clear();
//        }
//        
//        // 清理effectRoot下的物效。
//        for (Effect e : runningEffects.getArray()) {
//            e.cleanup();
//        }
//        runningEffects.clear();
//        
//        // 注：如果effectRoot下添加了其它类型的节点，则会一起被清理掉。
//        effectRoot.detachAllChildren();
//    }
//    
//    /**
//     * 获取Effect根节点。
//     * @return 
//     */
//    public Node getEffectRoot() {
//        return effectRoot;
//    }
//    
//    /**
//     * 指定一个特效的根节点, 如果没有特别设置这个根节点，则EffectManager会自动生成一个，
//     * 并偿试添加到SimpleApplication的rootNode下，这要求应用类型必须是SimpleApplication。
//     * @param effectRoot 
//     */
//    public void setEffectRoot(Node effectRoot) {
//        this.effectRoot = effectRoot;
//    }
//    
//    /**
//     * 添加效果，添加的effect并不立即添加到effectRoot中，而是先添加到队列中，在下一帧才会进行初始化(initialize),并添加到
//     * effectRoot中进行显示。
//     * @param effect 
//     * @return  
//     */
//    public boolean addEffect(Effect effect) {
//        synchronized (initializing) {
//            if (!initializing.contains(effect) && effect.getParent() != effectRoot) {
//                initializing.add(effect);
//                return true;
//            }
//            return false;
//        }
//    }
//    
//    /**
//     * 从EffectManager中移除指定的特效,并进行清理。注：该方法只移除添加到EffectManager中的特效，如果目标特效不是
//     * 添加到EffectManager上的，则什么也不做, 并返回false, 对于添加到其它节点下的特效需要自行清理和移除。
//     * @param effect
//     * @return 如果从EffectManager中成功移除了特效则返回true, 如果特效不由EffectManager管理则返回false.
//     */
//    public boolean removeEffect(Effect effect) {
//        // 从运行时
//        if (effect.getParent() == effectRoot) {
//            effect.cleanup();
//            effect.removeFromParent();
//            runningEffects.remove(effect);
//            return true;
//        }
//        
//        synchronized (initializing) {
//            if (initializing.remove(effect)) {
//                effect.cleanup();
//                effect.removeFromParent();
//                return true;
//            }
//        }
//        
//        return false;
//    }
//    
//}
