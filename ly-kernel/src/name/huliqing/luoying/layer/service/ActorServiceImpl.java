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
package name.huliqing.luoying.layer.service;

import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.luoying.object.module.ChannelModule;

/**
 *
 * @author huliqing
 */
public class ActorServiceImpl implements ActorService {

    @Override
    public void inject() {
    }

    @Override
    public List<Entity> findNearestActors(Entity actor, float maxDistance, float angle, List<Entity> store) {
        if (store == null) {
            store = new ArrayList<Entity>();
        }
        if (angle <= 0)
            return store;
        List<Entity> actors = actor.getScene().getEntities(Entity.class, actor.getSpatial().getWorldTranslation(), maxDistance, null);
        float halfAngle = angle * 0.5f;
        for (Entity a : actors) {
            
            if (a == actor) 
                continue;
            
            if (!(a instanceof Actor)) 
                continue;
            
            if (angle >= 360 || getViewAngle(actor, a.getSpatial().getWorldTranslation()) < halfAngle) {
                store.add(a);
            }
        }
        return store;
    }

//    @Override
//    public float getHeight(Entity actor) {
//        BoundingBox bb = (BoundingBox) actor.getSpatial().getWorldBound();
//        return bb.getXExtent() * 2;
//    }

    // remove201609228
//    @Override
//    public Vector3f getLocalToWorld(Entity actor, Vector3f localPos, Vector3f store) {
//        if (store == null) {
//            store = new Vector3f();
//        }
//        actor.getSpatial().getWorldRotation().mult(localPos, store);
//        store.addLocal(actor.getSpatial().getWorldTranslation());
//        return store;
//    }

    @Override
    public void syncTransform(Entity actor, Vector3f location, Vector3f viewDirection) {
        if (location != null)
            setLocation(actor, location);
        
        if(viewDirection != null)
            setViewDirection(actor, viewDirection);
    }

    @Override
    public void setLocation(Entity actor, Vector3f location) {
        actor.getModule(ActorModule.class).setLocation(location);
    }
    
    @Override
    public Vector3f getLocation(Entity actor) {
        return actor.getSpatial().getWorldTranslation();
    }

    @Override
    public void setPhysicsEnabled(Entity actor, boolean enabled) {
        actor.getModule(ActorModule.class).setEnabled(enabled);
    }
    
    @Override
    public boolean isPhysicsEnabled(Entity actor) {
        return actor.getModule(ActorModule.class).isEnabled();
    }
    
    @Override
    public void setViewDirection(Entity actor, Vector3f viewDirection) {
        actor.getModule(ActorModule.class).setViewDirection(viewDirection);
    }
    
    @Override
    public Vector3f getViewDirection(Entity actor) {
        ActorModule module = actor.getModule(ActorModule.class);
        if (module != null) {
            return module.getViewDirection();
        }
        return null;
    }
    
    @Override
    public void setLookAt(Entity actor, Vector3f position) {
        ActorModule module = actor.getModule(ActorModule.class);
        if (module != null) {
            module.setLookAt(position);
        }
    }
    
    @Override
    public void setWalkDirection(Entity actor, Vector3f walkDirection) {
        ActorModule module = actor.getModule(ActorModule.class);
        if (module != null) {
            module.setWalkDirection(walkDirection);
        }
    }
    
    @Override
    public Vector3f getWalkDirection(Entity actor) {
        ActorModule module = actor.getModule(ActorModule.class);
        if (module != null) {
            return module.getWalkDirection();
        }
        return null;
    }
    
    @Override
    public void setChannelLock(Entity actor, boolean locked, String... channelIds) {
        ChannelModule module = actor.getModule(ChannelModule.class);
        if (module != null) {
            module.setChannelLock(locked, channelIds);
        }
    }

    @Override
    public boolean reset(Entity actor) {
        ChannelModule module = actor.getModule(ChannelModule.class);
        if (module != null) {
            module.reset();
            return true;
        }
        return false;
    }

    @Override
    public float getViewAngle(Entity actor, Vector3f position) {
        // 优化性能
        TempVars tv = TempVars.get();
        Vector3f view = tv.vect1.set(getViewDirection(actor)).normalizeLocal();
        Vector3f dir = tv.vect2.set(position).subtractLocal(actor.getSpatial().getWorldTranslation()).normalizeLocal();
        float dot = dir.dot(view);
        float angle = 90;
        if (dot > 0) {
            angle = (1.0f - dot) * 90;
        } else if (dot < 0) {
            angle = -dot * 90 + 90;
        } else {
//            angle = 90;
        }
        tv.release();
        return angle;
    }

    @Override
    public boolean isKinematic(Entity actor) {
        ActorModule module = actor.getModule(ActorModule.class);
        return module != null ? module.isKinematic() : false;
    }

    @Override
    public void setKinematic(Entity actor, boolean kinematic) {
        ActorModule module = actor.getModule(ActorModule.class);
        if (module != null) {
            module.setKinematic(kinematic);
        }
    }

    @Override
    public float distance(Entity actor, Entity target) {
        return actor.getSpatial().getWorldTranslation().distance(target.getSpatial().getWorldTranslation());
    }

    @Override
    public float distanceSquared(Entity actor, Entity target) {
        return actor.getSpatial().getWorldTranslation().distanceSquared(target.getSpatial().getWorldTranslation());
    }

    @Override
    public float distance(Entity actor, Vector3f position) {
        return actor.getSpatial().getWorldTranslation().distance(position);
    }

}
