/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.skill;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 伤害检测结果
 * @author huliqing
 */
public class HitResult {
    
    // 防守成功的
    private final List<Entity> defends = new ArrayList<Entity>();
    // 被击中的
    private final List<Entity> hits = new ArrayList<Entity>();
    
    /**
     * 添加一个角色到成功防守列表中.
     * @param target 
     */
    public void addDefend(Entity target) {
        defends.add(target);
    }
    
    /**
     * 添加一个角色到被伤害列表中.
     * @param target 
     */
    public void addHit(Entity target) {
        hits.add(target);
    }
    
    /**
     * 获取防守成功的角色
     * @return 
     */
    public List<Entity> getDefends() {
        return defends;
    }
    
    /**
     * 获取伤害成功的角色
     * @return 
     */
    public List<Entity> getHits() {
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
