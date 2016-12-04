/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public interface SceneService extends Inject {

    /**
     * 获取当前游戏场景指定位置高度点，如果位置点超出地形外部，则该方法返回null.
     * @param x
     * @param z
     * @return null或地形高度位置点
     */
    Vector3f getSceneHeight(float x, float z);
    
    /**
     * 获取当前游戏场景指定位置的高度点，并将结果存放到store中，如果位置点超出地形外部则该方法什么也不会做，
     * 只返回store.
     * @param x
     * @param z
     * @param store 如果值为null则自动创建(默认(0,0,0))
     * @return 返回地形高度位置点或直接返回store.
     */
    Vector3f getSceneHeight(float x, float z, Vector3f store);
    
    /**
     * 获取场景指定位置高度点，如果位置点超出地形外部，则该方法返回null.
     * @param scene
     * @param x
     * @param z
     * @return 
     */
    Vector3f getSceneHeight(Scene scene, float x, float z);
    

}
