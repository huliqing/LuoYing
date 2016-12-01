/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.sceneloader;

import name.huliqing.luoying.data.SceneLoaderData;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 场景载入器,主要用于在场景载入过程中提供一个载入动画及进度。
 * @author huliqing
 */
public interface SceneLoader extends DataProcessor<SceneLoaderData>{
    
    /**
     * 添加一个载入器的侦听器，用于侦听场景的载入是否完成。
     * @param listener 
     */
    void addListener(SceneLoaderListener listener);
    
    /**
     * 移除指定的侦听器
     * @param listener 
     * @return  
     */
    boolean removeListener(SceneLoaderListener listener);
    
    /**
     * 开始载入场景
     * @param scene 
     */
    void loadScene(Scene scene);
}
