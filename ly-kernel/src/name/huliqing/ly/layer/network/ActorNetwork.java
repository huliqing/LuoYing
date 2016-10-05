/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.ly.Inject;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.view.talk.Talk;

/**
 * @author huliqing
 */
public interface ActorNetwork extends Inject {
    
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
     * 杀死一个角色
     * @param actor 
     */
    void kill(Actor actor);
    
     /**
     * 设置角色的当前目标
     * @param actor
     * @param target 如果为null,则表示清除目标
     */
    void setTarget(Actor actor, Actor target);
    
    /**
     * 操作角色的某个属性.
     * @param beHit 接受hit的目标角色,或者说是被击中的角色。
     * @param hitter 攻击者，或者施放hit动作的角色。
     * @param hitAttribute hit的目标属性名称,必须是NumberAttribute属性，否则什么也不做
     * @param hitValue hit值,这个值将直接执行在target角色的属性上，也即不受角色
     * 任何其它属性的影响。
     */
    void hitNumberAttribute(Actor beHit, Actor hitter, String hitAttribute, float hitValue);
    
    /**
     * 设置角色的等级
     * @param actor
     * @param level 
     */
    void setLevel(Actor actor, int level);
    
    /**
     * 设置角色的分组
     * @param actor
     * @param group 
     */
    void setGroup(Actor actor, int group);
    
    /**
     * 设置角色的队伍分组
     * @param actor
     * @param team 
     */
    void setTeam(Actor actor, int team);
    
    /**
     * 让角色actor跟随目标targetId所指定的角色
     * @param actor
     * @param targetId 目标角色ID 
     */
    void setFollow(Actor actor, long targetId);
    
    /**
     * 打开或关闭角色的物理功能
     * @param actor
     * @param enabled 
     */
    void setPhysicsEnabled(Actor actor, boolean enabled);
    
    /**
     * 设置角色视角方向
     * @param actor
     * @param viewDirection 
     */
    void setViewDirection(Actor actor, Vector3f viewDirection);
    
    /**
     * 让角色看向指定<b>位置</b>(非方向)
     * @param actor
     * @param position 
     */
    void setLookAt(Actor actor, Vector3f position);
    
}
