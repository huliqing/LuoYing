/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.ActorData;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.enums.HurtFace;
import name.huliqing.fighter.enums.Sex;
import name.huliqing.fighter.manager.talk.Talk;
import name.huliqing.fighter.object.actor.ActorListener;
import name.huliqing.fighter.object.actor.SkillListener;

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
  
    // remove 0221
//    /**
//     * 给角色添加物品
//     * @param actor 要添加物品的角色
//     * @param objectId 添加的物品ID
//     * @param count 添加的数量
//     */
//    boolean rewardItem(Actor actor, String objectId, int count);
    
    /**
     * 生成一个随机名字
     * @param sex 性别
     * @return 
     */
    String createRandomName(Sex sex);
  
    // remove20160504
//    /**
//     * 判断角色前面是否有障碍物
//     * @param actor
//     * @return 
//     * @deprecated use {@link #hasObstacleActor(name.huliqing.fighter.actor.Actor, java.util.List) }
//     */
//    boolean hasObstacle(Actor actor);
    
    /**
     * 判断角色前面是否有障碍物（Actor)，为提高性能只判断Actor类，其它的不处理。
     * @param self
     * @param others 其它的角色，不包含self
     * @return 
     */
    boolean hasObstacleActor(Actor self, List<Actor> others); 
  
    // remove 0221
//    /**
//     * 给角色添加物品
//     * @param actor
//     * @param objectId
//     * @param count 
//     */
//    void addItem(Actor actor, String objectId, int count);
    
    /**
     * 获取角色身上的物品(除技能），如果没有该物品则返回null.
     * @param actor
     * @param objectId 物品ID
     * @return 
     */
    ProtoData getItem(Actor actor, String objectId);
    
    /**
     * 获取角色的所有物品(除技能外）
     * @param actor
     * @param store
     * @return 
     */
    List<ProtoData> getItems(Actor actor, List<ProtoData> store);
    
    /**
     * 给定一个时间点，判断从目标时间点之后是否角色的技能有更新（包含技能增加
     * ，减少，升级等）。
     * @param actor
     * @param timePoint
     * @return 
     */
    boolean isSkillUpdated(Actor actor, long timePoint);
  
    /**
     * 检查目标target在角色self的哪一个方向
     * @param self
     * @param target
     * @return 
     */
    HurtFace checkFace(Spatial self, Actor target);
    
    /**
     * 判断目标是否是一个角色,如果是，则返回Actor,否则返回null.
     * @param spatial
     * @return 
     */
    Actor getActor(Spatial spatial);
    
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
     * 判断角色是否开启了自动AI
     * @param actor
     * @return 
     */
    boolean isAutoAi(Actor actor);

    /**
     * 开启或关闭角色的自动AI
     * @param actor
     * @param autoAi 
     */
    void setAutoAi(Actor actor, boolean autoAi);

    /**
     * 判断角色是否自动侦察敌人
     * @param actor
     * @return 
     */
    boolean isAutoDetect(Actor actor);
    
    /**
     * 设置角色是否自动侦察敌人
     * @param actor 
     * @param autoDetect
     */
    void setAutoDetect(Actor actor, boolean autoDetect);
    
    /**
     * 判断角色是否死亡
     * @param actor
     * @return 
     */
    boolean isDead(Actor actor);

    /**
     * 判断目标是否为敌人
     * @param actor
     * @return 
     */
    boolean isEnemy(Actor actor, Actor target);
    
    /**
     * 设置是否打开或关闭角色的物理属性
     * @param actor
     * @param enabled 
     */
    void setPhysics(Actor actor, boolean enabled);
    
    /**
     * 设置角色的视角方向 
     * @param actor
     * @param viewDirection 
     */
    void setViewDirection(Actor actor, Vector3f viewDirection);
    
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
     * @param hitAttribute hit的目标属性id
     * @param hitValue hit值,这个值将直接执行在target角色的属性上，也即不受角色
     * 任何其它属性的影响。
     */
    void hitAttribute(Actor target, Actor source, String hitAttribute, float hitValue);
    
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
    
    /**
     * 从攻击死亡角色身上获取经验奖励
     * @param attacker 攻击角色
     * @param dead 死亡角色
     * @return 
     */
    int getXpReward(Actor attacker, Actor dead);
    
    /**
     * 给指定的角色添加XP,如果添加XP后角色等级提升，则返回等级提升数。否则返
     * 回0.
     * @param actor
     * @param xp 
     */
    int applyXp(Actor actor, int xp);
    
    /**
     * 获取角色下一等级需要的XP值。
     * @param actor
     * @return 
     */
    int getNextLevelXp(Actor actor);
    
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
     * 添加技能侦听器
     * @param actor
     * @param skillListener 
     */
    void addSkillListener(Actor actor, SkillListener skillListener);
    
    /**
     * 移除指定的技能侦听器
     * @param actor
     * @param skillListener 
     * @return 
     */
    boolean removeSkillListener(Actor actor, SkillListener skillListener);
    
    /**
     * 设置角色名称
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
     * 获取角色的生命值。
     * @param actor
     * @return 
     */
    int getLife(Actor actor);
    
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
     * 获取角色的性别
     * @param actor
     * @return 
     */
    Sex getSex(Actor actor);
    
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
     * 获取角色的种族
     * @param actor
     * @return 
     */
    String getRace(Actor actor);
    
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
     * 获取角色当前可用的天赋点数
     * @param actor
     * @return 
     */
    int getTalentPoints(Actor actor);
    
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
}
