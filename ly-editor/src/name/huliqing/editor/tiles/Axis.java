/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author huliqing
 */
public class Axis extends Node {
    
    private final Vector3f direction = new Vector3f();
    private final Vector3f worldDirection = new Vector3f();
    
    public Axis(Vector3f direction) {
        direction.set(direction).normalizeLocal();
    }
    
    public Vector3f getDirection() {
        return direction;
    }
    
    public void setDirection(Vector3f direction) {
        this.direction.set(direction);
    }
    
    /**
     * 获取世界方向，返回的向量已经归一化
     * @return 
     */
    public Vector3f getWorldDirection() {
        return getWorldRotation().mult(direction, worldDirection).normalizeLocal();
    }
}
