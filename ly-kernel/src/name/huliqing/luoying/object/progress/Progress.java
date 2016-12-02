/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.progress;

import com.jme3.scene.Node;
import name.huliqing.luoying.data.ProgressData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 场景载入器,主要用于在场景载入过程中提供一个载入动画及进度。
 * @author huliqing
 */
public interface Progress extends DataProcessor<ProgressData>{
    
    /**
     * 初始化进度条.
     * @param viewRoot 用于进度条视图的父节点，比如GUI的根节点。
     */
    void initialize(Node viewRoot);
    
    /**
     * 判断是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 更新并渲染进度条，给定的参数progress取值为 0.0~1.0， 1.0表示100%载入完成, 0.5表示%50完成，依此类推。
     * 该方法会在场景实体载入过程中被持续调用，直到场景所有实体载入完成。
     * @param progress 
     */
    void display(float progress);
    
    /**
     * 清理并释放资源
     */
    void cleanup();
    
//    /**
//     * 添加一个载入器的侦听器，用于侦听场景的载入是否完成。
//     * @param listener 
//     */
//    void addListener(ProgressListener listener);
//    
//    /**
//     * 移除指定的侦听器
//     * @param listener 
//     * @return  
//     */
//    boolean removeListener(ProgressListener listener);
//    
//    /**
//     * 开始载入场景
//     * @param scene 
//     */
//    void loadScene(Scene scene);
    
}
