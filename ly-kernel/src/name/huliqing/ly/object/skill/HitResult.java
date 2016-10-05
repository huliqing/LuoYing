/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.skill;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.object.actor.Actor;

/**
 * 伤害检测结果
 * @author huliqing
 */
public class HitResult {
    
    // 防守成功的
    private final List<Actor> defends = new ArrayList<Actor>();
    // 被击中的
    private final List<Actor> hits = new ArrayList<Actor>();
    
    /**
     * 添加一个角色到成功防守列表中.
     * @param target 
     */
    public void addDefend(Actor target) {
        defends.add(target);
    }
    
    /**
     * 添加一个角色到被伤害列表中.
     * @param target 
     */
    public void addHit(Actor target) {
        hits.add(target);
    }
    
    /**
     * 获取防守成功的角色
     * @return 
     */
    public List<Actor> getDefends() {
        return defends;
    }
    
    /**
     * 获取伤害成功的角色
     * @return 
     */
    public List<Actor> getHits() {
        return hits;
    }
    
    /**
     * 清理列表.
     */
    public void clear() {
        defends.clear();
        hits.clear();
    }
}
