/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.scene;

import com.jme3.math.Vector3f;
import com.jme3.util.SafeArrayList;
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
    private final SafeArrayList<Actor> actors = new SafeArrayList<Actor>(Actor.class);
    
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
            for (Actor actor : actors.getArray()) {
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
            for (Actor actor : actors.getArray()) {
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
        actors.clear();
        super.cleanup(); 
    }
    
}
