/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 角色监听器
 * @author huliqing
 */
public interface ActorListener {
    
    // remove20161105
//    /**
//     * 当角色对另一个目标进行了攻击时这个方法被调用, 这里要注意：攻击(hit)这里不一定是减益的，也可能是增益的hit,
//     * 比如角色A对目标角色B的属性healthAttribute进行了+50(hitValue)的hit，
//     * 这等于是对角色B增加了50点生命值(假如：healthAttribute代表角色的生命值属性)。
//     * @param sourceHitter 源角色，被侦听的角色
//     * @param beHit 受到攻击的角色。
//     * @param hitAttribute 攻击所针对的属性名称
//     * @param hitValue 针对指定属性的攻击值,可正可负。
//     * @param killedByHit 目标(beHit)是否由于<b>这次攻击</b>而死亡, 如果角色已经死亡，
//     * 但<b>不是</b>这次攻击造成的，则该参数为false.
//     */
//    void onActorHitTarget(Entity sourceHitter, Entity beHit, String hitAttribute, float hitValue, boolean killedByHit);
//    
//    /**
//     * 当角色被击中时调用,即：当角色sourceBeHit受到hitter击中时触发该方法。
//     * 注：击中并不表示减益，也可能是增益，比如受物理攻击为减益，也可能受到增益的buff效果
//     * @param sourceBeHit 源角色（被侦听的角色）
//     * @param hitter 实施hit的角色，注：hitter可能为null，因hitter有可能不是一个实际存在的角色
//     * @param hitAttribute 击中的是哪一个属性
//     * @param hitValue 击中的最终属性值，这个值可正可负
//     * @param killedByHit 目标(beHit)是否由于<b>这次攻击</b>而死亡，如果角色已经死亡，
//     * 但<b>不是</b>这次攻击造成的，则该参数为false.
//     */
//    void onActorHitByTarget(Entity sourceBeHit, Entity hitter, String hitAttribute, float hitValue, boolean killedByHit);
    
    /**
     * 当角色被另一个角色锁定为目标对象时该方法被调用.
     * 即表示当源角色(sourceBeLocked)被另一角色(other)当成目标对象的时候调用。
     * @param sourceBeLocked 源角色
     * @param other 锁定目标的角色
     */
    void onActorTargetLocked(Entity sourceBeLocked, Entity other);
    
    /**
     * 当角色被释放锁定时该方法被调用。
     * 即意思是：当源角色(sourceBeReleased)不再被另一角色(other)当成目标对象的时候调用。
     * @param sourceBeReleased
     * @param other 
     */
    void onActorTargetReleased(Entity sourceBeReleased, Entity other);
   
    /**
     * 当角色对另一个目标(beHit)进行了攻击时这个方法被调用.
     * @param hitter 源角色(被侦听的角色)
     * @param beHit 这个参数表示攻击到了哪一个目标对象。
     * @param hitAttribute 攻击所针对的属性
     * @param hitValue 这个值是偿试应用到指定属性上的值，但最终应用到属性上的值，应用了多少应该从属性中获得的为准，
     * 因为一些属性类型可能会限制应用到属性上的值，比如限制取值范围的一些Number类型的值。
     * @param oldValue 击中前的属性值, <b>注意：这个值只是在hitValue应用前从属性{@link Attribute#getValue() }中获得的
     * 值，对于一些值为复合型的属性，这个oldValue可能仍然指定新的值的引用。</b>
     * @param killed 目标(beHit)是否由于这次攻击而死亡,
     */
    void onActorHitTarget(Entity hitter, Entity beHit, Attribute hitAttribute, Object hitValue, Object oldValue, boolean killed);
    
    /**
     * 当角色被某个目标(hitter)击中时调用
     * @param beHit 被击中的角色（当前角色，即侦听的角色）
     * @param hitter 实施hit的目标角色，注：hitter可能为null，因hitter有可能不是一个实际存在的角色
     * @param hitAttribute 击中的是哪一个属性
     * @param hitValue 这个值是偿试应用到指定属性上的值，但最终应用到属性上的值，应用了多少应该从属性中获得的为准，
     * 因为一些属性类型可能会限制应用到属性上的值，比如限制取值范围的一些Number类型的值。
     * @param oldValue 被击中前的属性值, <b>注意：这个值只是在hitValue应用前从属性{@link Attribute#getValue() }中获得的
     * 值，对于一些值为复合型的属性，这个oldValue可能仍然指定新的值的引用。</b>
     * @param killed 是否被目标对象伤害致死。
     */
    void onActorHitByTarget(Entity beHit, Entity hitter, Attribute hitAttribute, Object hitValue, Object oldValue, boolean killed);
    
    
}
