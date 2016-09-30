/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.hitchecker;

import name.huliqing.core.data.HitCheckerData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ActorModule;

/**
 * @author huliqing
 * @param <T>
 */
public class SimpleHitChecker<T extends HitCheckerData> extends AbstractHitChecker<T> {
    
    private enum Group {
        /** ignore不管分组 */
        i,
        /** same必须是相同分组 */
        s,
        /** difference必须是不同分组 */
        d;
        
        static Group identify(String name) {
            Group[] values = values();
            for (Group g : values) {
                if (g.name().equals(name)) {
                    return g;
                }
            }
            throw new UnsupportedOperationException("group name=" + name);
        }
    }
    
    private enum Checker {
        /** ignore不检查 */
        i,
        /** yes */
        y,
        /** no */
        n;
        
        static Checker identify(String name) {
            for (Checker l : values()) {
                if (l.name().equals(name)) {
                    return l;
                }
            }
            throw new UnsupportedOperationException("Checker name=" + name);
        }
    }
    
    // 分组类型。
    private Group group;
    // 生命体状态
    private Checker living;
    // 生命值状态
    private Checker life;

    @Override
    public void setData(T data) {
        super.setData(data);
        group = Group.identify(data.getAsString("group"));
        living = Checker.identify(data.getAsString("living"));
        life = Checker.identify(data.getAsString("life"));
    }
    
    @Override
    public boolean canHit(Actor source, Actor target) {
        ActorModule targetActorModule = target.getModule(ActorModule.class);
        if (targetActorModule == null)
            return false;
        
        // 注意：因为source有可能为null,举例来说，比如：magic有可能是没有施放者的
        // 也就没有source
        if (source != null) {
            int sourceGroup = source.getModule(ActorModule.class).getGroup();
            // 判断是否符合分组
            if (group == Group.s && sourceGroup != targetActorModule.getGroup()) {
                return false;
            } else if (group == Group.d && sourceGroup == targetActorModule.getGroup()) {
                return false;
            } 
        }
        
        // 判断是否符合生命体状态
        if (living == Checker.y && !targetActorModule.isBiology()) {
            return false;
        } else if (living == Checker.n && targetActorModule.isBiology()) {
            return false;
        }
        
        // 判断生命值状态是否匹配
        if (life == Checker.y && targetActorModule.isDead()) {
            return false;
        } else if (life == Checker.n && !targetActorModule.isDead()) {
            return false;
        }
        
        return true;
    }
    
}
