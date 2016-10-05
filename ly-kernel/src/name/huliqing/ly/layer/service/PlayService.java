/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import com.jme3.ai.navmesh.NavMeshPathfinder;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.layer.network.PlayNetwork;
import name.huliqing.ly.state.PlayState;
import name.huliqing.ly.object.NetworkObject;
import name.huliqing.ly.object.PlayObject;
import name.huliqing.ly.object.anim.Anim;
import name.huliqing.ly.object.bullet.Bullet;
import name.huliqing.ly.object.effect.Effect;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.object.view.View;

/**
 * @author huliqing
 */
public interface PlayService extends PlayNetwork {
    
    /**
     * 添加PlayObject到场景中。
     * @param playObject 
     */
    void addPlayObject(PlayObject playObject);
    
    /**
     * 将playObject从场景中移除
     * @param playObject 
     */
    void removePlayObject(PlayObject playObject);
    
    /**
     * 添加一个效果到场景
     * @deprecated 后续要重构并使用 addPlayObject代替 
     * @param effect 
     */
    void addEffect(Effect effect);
    
    /**
     * 添加一个子弹到场景
     * @deprecated 后续要重构并使用 addPlayObject代替 
     * @param bullet 
     */
    void addBullet(Bullet bullet);

    /**
     * 添加快捷方式
     * @param actor
     * @param data 
     */
    void addShortcut(Actor actor, ObjectData data);
    
    /**
     * 添加动画到场景中
     * @deprecated 后续要重构并使用 addPlayObject代替 
     * @param animation 
     */
    void addAnimation(Anim animation);
    
    /**
     * 从场景中移除动画
     * @deprecated 后续要重构并使用 addPlayObject代替 
     * @param animation 
     */
    void removeAnimation(Anim animation);
    
    /**
     * @deprecated 
     * 添加物体到场景.
     * @param object 
     * @param gui is in gui
     */
    void addObject(Object object, boolean gui);
    
    /**
     * 添加物体到场景
     * @param object 
     */
    void addObject(Object object);
    
    /**
     * 添加物体到GUI上
     * @param object 
     */
    void addObjectGui(Object object);
    
    /**
     * 获取当前场景中所有的角色,这些角色不分敌我、死活，只要是在场景中就算。
     * @see PlayState#getActors
     * @return 
     */
    List<Actor> findAllActor();
    
    /**
     * 通过ID查找角色，注意，这里的id是角色类型id,场景中可能存在多个相同id
     * 的角色，当存在多个角色时，返回第一个找到的就可以。
     * @param id
     * @return 如果不存在该角色返回null
     * @see #findActor(int) 
     */
    Actor findActor(String id);
    
    /**
     * 通过角色的唯一ID,从当前场景中查询角色,如果找不到对应的角色,则返回null.
     * 与通过id类型查找不同，这里的actorUniqueId是场景中角色唯一存在的。即使相
     * 同id的角色也不可能存在相同的uniqueId.
     * @param actorUniqueId
     * @return 如果不存在该角色返回null
     * @see #findActor(java.lang.String) 
     */
    Actor findActor(long actorUniqueId);
    
    /**
     * 获取当前场景中的所有视图
     * @return 
     */
    List<View> findAllViews();
    
    /**
     * 通过唯一ID查找当前界面上的视图
     * @param uniqueId
     * @return 
     */
    View findView(long uniqueId);
    
    /**
     * 从场景中获取同步物体
     * @param objectId
     * @return 
     */
    NetworkObject findSyncObject(long objectId);
    
    /**
     * 生成寻路信息，如果场景不支持寻路功能，则返回null.
     * @return 
     */
    NavMeshPathfinder createPathfinder();
    
    /**
     * 获取当前游戏地面模型
     * @return 
     */
    Spatial getTerrain();
    
    /**
     * 获取当前场景地面高度值
     * @param x
     * @param z
     * @return 
     */
    float getTerrainHeight(float x, float z);
    
    /**
     * 获取目标角色前面(viewDirection>一定距离的世界坐标位置,返回的位置应该
     * 在地面之上。
     * @param actor
     * @param distance
     * @param store
     * @return 
     */
    Vector3f getActorForwardPosition(Actor actor, float distance, Vector3f store);
    
    /**
     * 获取玩家角色。
     * @return 
     */
    Actor getPlayer();
    
    /**
     * 在当前场景上获取一个随机地点，该地点需要距离地形的高度进行计算，不能
     * 掉进地下。
     * @param store
     * @return 
     */
    Vector3f getRandomTerrainPoint(Vector3f store);
    
    /**
     * 获取场景信息，如果不存在场景则返回null.
     * @return 
     */
    Scene getScene();
    
    /**
     * 判断角色是否还在场景中
     * @param actor
     * @return 
     */
    boolean isInScene(Actor actor);
    
    /**
     * 获取当前场景中的主要目标对象，如果没有则返回null.
     * @return 
     */
    Actor getTarget();
    
    /**
     * 设置当前场景中的主要目标对象.
     * @param target 
     */
    void setTarget(Actor target);
    
    /**
     * 将一个坐标点移动到当前游戏场景的地面上，如果该点已经在地面上，则不作
     * 任何处理。也就是该方法可能修改position的Y值
     * @param position 必须是世界坐标点 
     * @return  
     */
    Vector3f moveUpToTerrain(Vector3f position);
    
    /**
     * 获取当前游戏主屏幕宽度
     * @return 
     */
    float getScreenWidth();
    
    /**
     * 获取当前游戏主屏幕高度
     * @return 
     */
    float getScreenHeight();
    
    /**
     * 把目标角色设置为当前场景的主玩家角色。
     * @param actor 
     */
    void setMainPlayer(Actor actor);
    
    /**
     * 判断目标物体是否在场景中。
     * @param spatial
     * @return 
     */
    boolean isInScene(Spatial spatial);
    
    /**
     * 保存该关卡为完成状态,调用该方法则保存并标记当前关卡为“完成”
     * @deprecated 
     * @param storyNum
     */
    void saveCompleteStage(int storyNum);
    
    /**
     * 获取Application
     * @return 
     */
    Application getApplication();

    /**
     * 获取当前游戏的gameId,如果不是正在游戏，则返回null.
     * @return 
     */
    String getGameId();
}
