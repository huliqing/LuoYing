/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.animation.LoopMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.enums.Sex;
import name.huliqing.core.view.talk.Talk;
import name.huliqing.core.object.module.ActorListener;

/**
 *
 * @author huliqing
 */
public interface ActorService extends Inject {
    
    /**
     * 载入指定ID的角色
     * @param actorId
     * @return 
     */
    Actor loadActor(String actorId);
    
    /**
     * 载入通过给定的actorData来载入角色
     * @param actorData
     * @return 
     */
    Actor loadActor(ActorData actorData);
    
    /**
     * 生成一个随机名字
     * @param sex 性别
     * @return 
     */
    String createRandomName(Sex sex);
    
    /**
     * 判断角色前面是否有障碍物（Actor)，为提高性能只判断Actor类，其它的不处理。
     * @param self
     * @param others 其它的角色，不包含self
     * @return 
     */
    boolean hasObstacleActor(Actor self, List<Actor> others); 
  
    // remove20160903
//    /**
//     * 检查目标target在角色self的哪一个方向
//     * @param self
//     * @param target
//     * @return 
//     */
//    HurtFace checkFace(Spatial self, Actor target);
    
    /**
     * 寻找角色周围指定范围内最近的敌人,该敌人必须是活着的，如果没有敌人，则返回null.
     * @param actor
     * @param maxDistance 
     * @param except 需要排除的角色，如果不为null,则排除掉该角色。
     * @return 
     */
    Actor findNearestEnemyExcept(Actor actor, float maxDistance, Actor except);
    
    /**
     * 寻找actor周围指定范围内的所有敌军单位,这些单位必须是活着的.即有生命值的。
     * @param actor
     * @param maxDistance
     * @param store
     * @return 
     */
    List<Actor> findNearestEnemies(Actor actor, float maxDistance, List<Actor> store);
    
    /**
     * 寻找actor周围指定范围内的所有友军单位,这些友军单位必须是活着的.
     * @param actor 指定的角色.
     * @param maxDistance 指定的范围
     * @param store 存放结果
     * @return 
     */
    List<Actor> findNearestFriendly(Actor actor, float maxDistance, List<Actor> store);
    
    /**
     * 获取角色周围一定围围内的其它角色，包含死亡的角色，但不包含角色自身(actor).
     * @param actor
     * @param maxDistance
     * @param store
     * @return 
     */
    List<Actor> findNearestActors(Actor actor, float maxDistance, List<Actor> store);
    
    /**
     * 获取角色周围一定围围内的其它角色，包含死亡的角色，但不包含角色自身(actor).
     * 可以使用angle来限制角度，即表示在actor前方的一定角度范围内的角色。如果
     * angle小于或等于0 则将不会获得任何角色。
     * @param actor
     * @param maxDistance 限制距离
     * @param angle 限制角度取值: [0.0~360]
     * @param store
     * @return 
     */
    List<Actor> findNearestActors(Actor actor, float maxDistance, float angle, List<Actor> store);
    
    /**
     * 获取角色的高度值,主要获得该角色的包围盒的高度.
     * @param actor
     * @return 
     */
    float getHeight(Actor actor);
    
    /**
     * 设置同伴
     * @param owner
     * @param partner 
     */
    void setPartner(Actor owner, Actor partner);
    
    /**
     * 让角色立即说话。
     * @param actor
     * @param mess 
     * @param useTime 说话内容的显示时间,单位秒,如果小于或等于0则自动计算显示时长
     */
    void speak(Actor actor, String mess, float useTime);
    
    /**
     * 执行一个系列谈话内容,注意：该谈话内容将立即被执行。不需要调用start.
     * @param talk 
     */
    void talk(Talk talk);
    
    /**
     * 将角色本地坐标系中的点转换为世界坐标系中的点。
     * @param actor 目标角色
     * @param localPos 存在于角色坐标系中的位置
     * @param store 存放结果,store可与localPos是同一个实例
     * @return 返回localPos在世界坐标系中的位置
     */
    Vector3f getLocalToWorld(Actor actor, Vector3f localPos, Vector3f store);
    
    /**
     * 检查是否存在指定的骨骼动画,如果没有则偿试载入该动画。如果找不到动画文件
     * 则返回false, 如果已经存在动画或载入动画成功则返回true.
     * @param actor　角色
     * @param animName　动画名称
     * @return 
     */
    boolean checkAndLoadAnim(Actor actor, String animName);
    
    /**
     * 杀死一个角色
     * @param actor 
     */
    void kill(Actor actor);
    
    /**
     * 复活角色
     * @param actor 
     */
    void reborn(Actor actor);
    
    /**
     * 设置角色的当前目标
     * @param actor
     * @param target 如果为null,则表示清除目标
     */
    void setTarget(Actor actor, Actor target);
    
    /**
     * 获得角色的当前目标，如果当前场景中不存在该目标，则返回null.
     * @param actor
     * @return 
     */
    Actor getTarget(Actor actor);
    
    /**
     * 判断角色是否死亡
     * @param actor
     * @return 
     */
    boolean isDead(Actor actor);

    /**
     * 判断目标是否为敌人
     * @param actor
     * @param target
     * @return 
     */
    boolean isEnemy(Actor actor, Actor target);
    
    /**
     * 给角色设置一个颜色
     * @param actor
     * @param color 
     */
    void setColor(Actor actor, ColorRGBA color);
    
    /**
     * 操作角色的某个属性.
     * @param target 接受hit的目标角色,或者说是被击中的角色。
     * @param source 攻击者，或者施放hit动作的角色。
     * @param numberAttribute hit的目标属性名称,必须是NumberAttribute属性，否则什么也不做
     * @param hitValue hit值,这个值将直接执行在target角色的属性上，也即不受角色
     * 任何其它属性的影响。
     */
    void hitNumberAttribute(Actor target, Actor source, String numberAttribute, float hitValue);
    
    /**
     * 获取角色等级
     * @param actor
     * @return 
     */
    int getLevel(Actor actor);
    
    /**
     * 设置角色的等级
     * @param actor
     * @param level 
     */
    void setLevel(Actor actor, int level);
    
    // remove20160910
//    /**
//     * 获取指定等级需要的经验值（XP)
//     * @param actor
//     * @param level
//     * @return 
//     */
//    int getLevelXp(Actor actor, int level);
    
    /**
     * 判断目标角色是否是可移动的
     * @param actor
     * @return 
     */
    boolean isMoveable(Actor actor);
    
    /**
     * 获取角色视力，如果没有配置则返回0.
     * @param actor
     * @return 
     */
    float getViewDistance(Actor actor);
    
    /**
     * 添加角色侦听器
     * @param actor
     * @param actorListener 
     */
    void addActorListener(Actor actor, ActorListener actorListener);
    
    /**
     * 移除角色侦听器
     * @param actor
     * @param actorListener
     * @return 
     */
    boolean removeActorListener(Actor actor, ActorListener actorListener);
    
    /**
     * 设置角色名称
     * @param actor
     * @param name 
     */
    void setName(Actor actor, String name);
    
    /**
     * 获取角色的名称
     * @param actor
     * @return 
     */
    String getName(Actor actor);
    
    /**
     * 获取角色的分组
     * @param actor
     * @return 
     */
    int getGroup(Actor actor);
    
    /**
     * 设置角色的分组
     * @param actor
     * @param group 
     */
    void setGroup(Actor actor, int group);
    
    /**
     * 获取角色的队伍分组
     * @param actor
     * @return 
     */
    int getTeam(Actor actor);
    
    /**
     * 设置角色的队伍分组
     * @param actor
     * @param team 
     */
    void setTeam(Actor actor, int team);
    
    /**
     * 判断角色是否为必要的，即不能被移除出场景的。"必要"的角色可以死亡，
     * 但是不能被移除出场景。
     * @param actor
     * @return 
     */
    boolean isEssential(Actor actor);
    
    /**
     * 设置角色是否为“必要的”，“必要”是指角色可以死亡，但不能移除出场景。
     * 非必要的角色在死亡之后可能被场景的清理器移除出场景,部分比如任务
     * 需要的角色不能被移除出场景。
     * @param actor
     * @param essential 
     */
    void setEssential(Actor actor, boolean essential);
    
    /**
     * 判断角色是否是生物
     * @param actor
     * @return 
     */
    boolean isLiving(Actor actor);
    
    /**
     * 设置角色的目标拥有者
     * @param actor
     * @param ownerId 
     */
    void setOwner(Actor actor, long ownerId);
    
    /**
     * 获取角色的拥有者（主人）ID
     * @param actor
     * @return 
     */
    long getOwner(Actor actor);
    
    /**
     * 让角色actor跟随目标targetId所指定的角色
     * @param actor
     * @param targetId 目标角色ID 
     */
    void setFollow(Actor actor, long targetId);
    
    /**
     * 获取角色当前所跟随的目标的唯一ID,如果返回的值小于或等于0则表示无跟随目标.
     * @param actor
     * @return 
     */
    long getFollow(Actor actor);
    
    /**
     * 同步角色的变换信息
     * @param actor 同步的角色
     * @param location 同步位置
     * @param viewDirection 同步视角方向
     */
    void syncTransform(Actor actor, Vector3f location, Vector3f viewDirection);
    
    /**
     * 同步角色动画。这个方法在同步服务端与客户端首次载入角色时的角色动画有用。注：数组长度都是一一对应的。
     * @param actor
     * @param channelIds
     * @param animNames
     * @param loopModes
     * @param speeds
     * @param times 
     */
    void syncAnimation(Actor actor, String[] channelIds, String[] animNames, byte[] loopModes, float[] speeds, float[] times);
    
    /**
     * 设置角色位置
     * @param actor
     * @param location 
     */
    void setLocation(Actor actor, Vector3f location);
    
    /**
     * 获取角色当前位置
     * @param actor
     * @return 
     */
    Vector3f getLocation(Actor actor);
    
    /**
     * 打开或关闭角色的物理功能
     * @param actor
     * @param enabled 
     */
    void setPhysicsEnabled(Actor actor, boolean enabled);
    
    /**
     * 判断目标角色是否打开了物理功能
     * @param actor
     * @return 
     */
    boolean isPhysicsEnabled(Actor actor);
    
    /**
     * 设置角色视角方向
     * @param actor
     * @param viewDirection 
     */
    void setViewDirection(Actor actor, Vector3f viewDirection);
    
    /**
     * 获取角色视角方向
     * @param actor
     * @return 
     */
    Vector3f getViewDirection(Actor actor);
    
    /**
     * 让角色看向指定<b>位置</b>(非方向)
     * @param actor
     * @param position 
     */
    void setLookAt(Actor actor, Vector3f position);
    
    /**
     * 设置角色步行方向
     * @param actor
     * @param walkDirection 
     */
    void setWalkDirection(Actor actor, Vector3f walkDirection);
    
    /**
     * 获取角色步行方向
     * @param actor
     * @return 
     */
    Vector3f getWalkDirection(Actor actor);
    
    /**
     * @param actor
     * @param animName
     * @param loop
     * @param useTime
     * @param startTime 
     * @param channelIds
     */
    void playAnim(Actor actor, String animName, LoopMode loop, float useTime, float startTime, String... channelIds);
    
    /**
     * 锁定或解锁指定的动画通道，当一个动画通道被锁定之后将不能再执行任何动画，包
     * 括reset，除非重新进行解锁
     * @param actor
     * @param locked true锁定通道，false解锁通道
     * @param channelIds 指定的通道列表，如果为null或指定的通道不存在则什么也不做。
     */
    void setChannelLock(Actor actor, boolean locked, String... channelIds);
    
    /**
     * 恢复动画，有时候当部分通道被打断执行了其它动画之后需要重新回到原来的
     * 动画上。如当角色在走路的时候所有通道都在执行“走路”动画，这时如果执
     * 行抽取武器的动画时，可能手部通道会打断走路所需要的一些通道动画。当抽取
     * 武器完毕之后，手部通道需要重新回到“走路”的动画中以便协调角色走路时的
     * 动画。
     * @param actor
     * @param animName
     * @param loop
     * @param useTime
     * @param startTime
     * @param channelIds 
     */
    void restoreAnimation(Actor actor, String animName, LoopMode loop, float useTime, float startTime, String... channelIds);
    
    /**
     * 把骨骼动画定位在当前所播放动画的第一帧处．
     * 可使用该方法来使角色停止活动。如：当角色没有“死亡”动画时，
     * 角色在死后需要停止活动，则可使用该方法来停止正在执行的动画。
     * @param actor
     * @return 
     * @see #resetToAnimationTime(Actor, String, float) 
     */
    boolean reset(Actor actor);
    
    /**
     * 把骨骼动画定位在某一个动画中的某一个时间点(帧)．
     * @param actor
     * @param animation 动画名称
     * @param timePoint 定位的时间插值点，取值[0.0~1.0] 
     */
    void resetToAnimationTime(Actor actor, String animation, float timePoint);
    
    /**
     * 判断角色是否为玩家角色，可能是客户端也可以是主机玩家
     * @param actor
     * @return 
     */
    boolean isPlayer(Actor actor);
    
    /**
     * 标记目标为玩家角色
     * @param actor
     * @param player 
     */
    void setPlayer(Actor actor, boolean player);
    
    /**
     * 获取目标位置与当前角色的正前方的夹角度数.简单的说,即当前角色要转多少度才能正视
     * 到position位
     * @param actor
     * @param position
     * @return 返回值为角度,不是弧度
     */
    float getViewAngle(Actor actor, Vector3f position);
    
    float getMass(Actor actor);
    
    boolean isKinematic(Actor actor);
    
    void setKinematic(Actor actor, boolean kinematic);
    
    /**
     * 判断角色与目标位置的距离
     * @param actor
     * @param position
     * @return 
     */
    float distance(Actor actor, Vector3f position);
    
    /**
     * 判断两个角色的距离
     * @param actor
     * @param target
     * @deprecated 
     * @return 
     */
    float distance(Actor actor, Actor target);
 
    /**
     * @deprecated 
     * @param actor
     * @param target
     * @return 
     */
    float distanceSquared(Actor actor, Actor target);
    
}
