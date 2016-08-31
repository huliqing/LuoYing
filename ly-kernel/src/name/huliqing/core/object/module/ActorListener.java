/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import name.huliqing.core.object.actor.Actor;

/**
 * 角色监听器,source对象为被监听的角色。
 * @author huliqing
 */
public interface ActorListener {
    
    /**
     * 当角色被另一个角色锁定为目标对象时该方法被调用.
     * 即表示当源角色(source)被另一角色(other)当成目标对象的时候调用。
     * @param source 源角色
     * @param other 另一角色
     */
    void onActorLocked(long source, Actor other);
    
    /**
     * 当角色被从另一个角色的目标对象中释放时该方法被调用,
     * 即意思是：当源角色(source)不再被另一角色(other)当成目标对象的时候调用。
     * @param source
     * @param other 
     */
    void onActorReleased(long source, Actor other);
    
    /**
     * 当角色被击中时调用,即：当source受到attacker击中时触发该方法。
     * 注：击中并不表示减益，也可能是增益，比如受物理攻击为减益，也可能受到
     * 增益的buff效果
     * @param source 源角色（被击中者）
     * @param attacker 实施者,注：当没有任何攻击者时，attacker可能为null
     * @param hitAttribute 击中的是哪一个属性
     * @param hitValue 击中的最终属性值
     */
    void onActorHit(Actor source, Actor attacker, String hitAttribute, float hitValue);
    
    /**
     * 当角色杀死另一个目标时该方法被调用.
     * @param source 源角色,凶手
     * @param target 被杀死的角色,不能为null.
     */
    void onActorKill(Actor source, Actor target);
    
    /**
     * 当角色被另一个目标杀死时该方法被调用.
     * @param source 源角色,即被杀死的角色。
     * @param target 凶手，target可能为null.需要判断，以避免NPE
     */
    void onActorKilled(Actor source, Actor target);
}
