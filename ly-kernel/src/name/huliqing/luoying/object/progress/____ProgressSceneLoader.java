package name.huliqing.luoying.object.progress;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.object.sceneloader;
//
//import com.jme3.scene.control.Control;
//import java.util.List;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.data.EntityData;
//import name.huliqing.luoying.layer.network.PlayNetwork;
//import name.huliqing.luoying.object.Loader;
//import name.huliqing.luoying.object.entity.Entity;
//import name.huliqing.luoying.object.module.AdapterControl;
//import name.huliqing.luoying.object.scene.Scene;
//
///**
// * 
// * @author huliqing
// */
//public abstract class ProgressSceneLoader extends AbstractProgress {
//    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
//    
//    // 载入器控制器
//    private Control loadControl;
//
//    // 当前的载入数
//    private int count;
//    
//    private Scene scene;
//    private List<EntityData> entityDatas;
//
//    @Override
//    protected final void loadEntities(Scene scene, List<EntityData> entityDatas) {
//        this.scene = scene;
//        this.entityDatas = entityDatas;
//        loadControl = new AdapterControl() {
//            @Override
//            public void update(float tpf) {
//                updateLoader(tpf);
//            }
//        };
//        scene.getRoot().addControl(loadControl);
//        
//        // 初始化进度条。
//        initProgress(scene);
//    }
//    
//    private void updateLoader(float tpf) {
//        if (count >= entityDatas.size()) {
//            notifyLoadedOK();
//            cleanup();
//            return;
//        }
//        
//        // 载入实体。
//        EntityData ed = entityDatas.get(count);
//        Entity entity = Loader.load(ed);
//        playNetwork.addEntity(scene, entity);
//        count++;
//        
//        // 更新进度条
//        displayProgress((float) count / entityDatas.size());
//    }
//    
//    /**
//     * 载入完成后该方法会被调用，以清理SceneLoader.
//     */
//    protected void cleanup() {
//        scene.getRoot().removeControl(loadControl);
//    }
//    
//    /**
//     * 在载入器运行之前该方法会被调用一次， 子类可实现这个方法来初始化进度条指示器。
//     * @param scene 
//     */
//    protected abstract void initProgress(Scene scene);
//    
//    /**
//     * 更新并渲染进度条，给定的参数progress取值为 0.0~1.0， 1.0表示100%载入完成, 0.5表示%50完成，依此类推。
//     * 该方法会在场景实体载入过程中被持续调用，直到场景所有实体载入完成。
//     * @param progress 
//     */
//    protected abstract void displayProgress(float progress);
//    
//}
