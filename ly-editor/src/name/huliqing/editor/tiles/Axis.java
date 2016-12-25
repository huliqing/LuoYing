/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * 用于定义轴向。
 * @author huliqing
 */
public class Axis extends Node {
    
    private final Vector3f direction = new Vector3f();
    
    public enum Type {
        x,y,z
    }
    
    private final Type type;
    
    public Axis(Type type) {
        this.type = type;
        switch (type) {
            case x:
                direction.set(1,0,0);
                break;
            case y:
                direction.set(0,1,0);
                break;
            case z:
                direction.set(0,0,1);
                break;
            default:
                throw new UnsupportedOperationException("Unknow type=" + type);
        }
    }

    public Type getType() {
        return type;
    }
    
    /**
     * 返回方向轴（已经归一化）
     * @param store
     * @return 
     */
    public Vector3f getDirection(Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        return store.set(direction);
    }
    
    /**
     * 获取世界方向，返回的向量已经归一化
     * @param store
     * @return 
     */
    public Vector3f getWorldDirection(Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        store.set(direction);
        return getWorldRotation().mult(store, store).normalizeLocal();
    }
    
    public boolean intersects(Ray ray) {
        BoundingVolume bv = getWorldBound();
        return bv != null && bv.intersects(ray);
    }
    
    public CollisionResults getCollisions(Ray ray, CollisionResults store) {
        if (store == null) {
            store = new CollisionResults();
        }
        if (!intersects(ray)) {
            return store;
        }
        collideWith(ray, store);
        return store;
    }

}
