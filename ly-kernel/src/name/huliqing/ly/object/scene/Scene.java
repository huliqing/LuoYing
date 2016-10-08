/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.scene;

import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.post.SceneProcessor;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Collection;
import java.util.List;
import name.huliqing.ly.data.SceneData;
import name.huliqing.ly.xml.DataProcessor;
import name.huliqing.ly.object.SceneObject;

/**
 * 游戏场景，存取游戏场景中对象的容器。创场景场景之后需要通过{@link #getRoot() }获取场景的根节点，
 * 并把场景根节点放到游戏根节点下才能进行渲染和显示。
 * @author huliqing
 */
public interface Scene extends DataProcessor<SceneData> {

    /**
     * 设置场景数据，一般不要手动去调用。
     * @param data 
     */
    @Override
    public void setData(SceneData data);

    /**
     * 获取场景data，这个data包含了当前场景中的状态信息，如果需要获得场景的<b>实时</b>状态，应该先调用
     * {@link #updateDatas() }来进行更新。
     * @return 
     */
    @Override
    public SceneData getData();

    /**
     * 更新当前场景中的所有物体的状态到data中.一般在需要对场景进行存档之前需要调用这个方法来更新状态到SceneData中去。
     * @see #getData() 
     */
    @Override
    public void updateDatas();
    
    /**
     * 初始化entity容器
     */
    void initialize();
    
    /**
     * 判断容器是否已经初始化完毕
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理释放资源
     */
    void cleanup();
    
    /**
     * 获取场景根节点
     * @return 
     */
    Node getRoot();
    
    /**
     * 添加一个场景物体,在添加之前必须确保场景已经初始化。
     * @param sceneObject 
     */
    void addSceneObject(SceneObject sceneObject);
    
    /**
     * 从容器中移除指定场景物体，如果成功则返回true,移除失败或者物体不存在则返回false.
     * @param sceneObject 
     * @return  
     */
    boolean removeSceneObject(SceneObject sceneObject);
    
    /**
     * 添加场景物体
     * @param spatialAdded 
     */
    void addSpatial(Spatial spatialAdded);
    
    /**
     * 移除场景物体
     * @param spatialRemoved 
     * @return  
     */
    boolean removeSpatial(Spatial spatialRemoved);
    
    /**
     * 通过唯一id来查找场景物体,如果不存在则反回null.
     * @param objectId
     * @return 
     */
    SceneObject getSceneObject(long objectId);
    
    /**
     * 在某一个位置点上，向周围一定的范围内查找场景物体，在这个范围内的物体都将一起返回。
     * @param location 指定的位置点
     * @param radius 指定的范围
     * @param store 存放结果
     * @return 
     */
    Collection<SceneObject> getSceneObjects(Vector3f location, float radius, List<SceneObject> store);
    
    /**
     * 获取场景中的所有物体，这个方法会返回整个场景中的物体，如果只要查找一定范围内的物体可以使用
     * {@link #getSceneObjects(com.jme3.math.Vector3f, float, java.util.List) }.
     * 注：返回的列表不能直接修改。
     * @return 
     * @see #removeSceneObject(name.huliqing.ly.object.SceneObject) 
     * @see #addSceneObject(name.huliqing.ly.object.SceneObject) 
     */
    Collection<SceneObject> getSceneObjects();
    
    /**
     * 添加容器侦听器
     * @param listener 
     */
    void addSceneListener(SceneListener listener);
    
    /**
     * 移除容器侦听器
     * @param listener 
     * @return  
     */
    boolean removeSceneListener(SceneListener listener);
    
    /**
     * 给场景添加Processor
     * @param sceneProcessor 
     */
    void addProcessor(SceneProcessor sceneProcessor);
    
    /**
     * 从场景中移除Processor
     * @param sceneProcessor
     */
    void removeProcessor(SceneProcessor sceneProcessor);
    
    /**
     * 给场景添加一个Post Filter
     * @param filter 
     */
    void addFilter(Filter filter);

    /**
     * 从场景中移除一个指定的Post Filter.
     * @param filter
     */
    void removeFilter(Filter filter);
}
