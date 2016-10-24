/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.scene;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 简单的场景实例，实例化AbstractScene, 并优化查询。
 * @author huliqing
 */
public class SimpleScene extends AbstractScene {

    // 用于优化查询
    protected final Map<Long, Entity> entityMap = new HashMap<Long, Entity>();
    
    @Override
    public void addEntity(Entity entity) {
        entityMap.put(entity.getEntityId(), entity);
        super.addEntity(entity); 
    }

    @Override
    public void removeEntity(Entity entity) {
        entityMap.remove(entity.getEntityId());
        super.removeEntity(entity); 
    }

    @Override
    public Entity getEntity(long entityId) {
        return entityMap.get(entityId);
    }

    @Override
    public void cleanup() {
        entityMap.clear();
        super.cleanup(); 
    }
    
}
