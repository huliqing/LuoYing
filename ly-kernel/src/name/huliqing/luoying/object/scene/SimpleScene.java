/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.scene;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 简单的场景实例，实例化AbstractScene, 并优化查询。
 * @author huliqing
 */
public class SimpleScene extends AbstractScene {

    // 用于优化查询
    private final Map<Long, Entity> entityMap = new HashMap<Long, Entity>();
    // 优化Actor角色的查询
    private final List<Actor> actors = new ArrayList<Actor>();
    
    @Override
    public void addEntity(Entity entity) {
        entityMap.put(entity.getEntityId(), entity);
        if (entity instanceof Actor) {
            actors.add((Actor) entity);
        }
        super.addEntity(entity); 
    }

    @Override
    public void removeEntity(Entity entity) {
        entityMap.remove(entity.getEntityId());
        if (entity instanceof Actor) {
            actors.remove((Actor) entity);
        }
        super.removeEntity(entity); 
    }

    @Override
    public Entity getEntity(long entityId) {
        return entityMap.get(entityId);
    }

    @Override
    public <T extends Entity> List<T> getEntities(Class<T> type, List<T> store) {
        if (store == null) {
            store = new ArrayList<T>();
        }
        // 优化Actor查询
        if (Actor.class.isAssignableFrom(type)) {
            for (Actor actor : actors) {
                if (type.isAssignableFrom(actor.getClass())) {
                    store.add((T)actor);
                }
            }
            return store;
        }
        return super.getEntities(type, store);
    }

    @Override
    public <T extends Entity> List<T> getEntities(Class<T> type, Vector3f location, float radius, List<T> store) {
        if (store == null) {
            store = new ArrayList<T>();
        }
        // 优化Actor查询
        if (Actor.class.isAssignableFrom(type)) {
            float sqRadius = (float)Math.pow(radius, 2);
            for (Actor actor : actors) {
                if (type.isAssignableFrom(actor.getClass())) {
                    if (actor.getSpatial().getWorldTranslation().distanceSquared(location) <= sqRadius) {
                        store.add((T) actor);
                    }
                }
            }
            return store;
        }
        return super.getEntities(type, location, radius, store); 
    }
    
    @Override
    public void cleanup() {
        entityMap.clear();
        super.cleanup(); 
    }
    
}
