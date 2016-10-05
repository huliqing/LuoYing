/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * @author huliqing
 */
@Serializable
public class BulletData extends ObjectData {
    
    private Vector3f startPoint;
    private Vector3f endPoint;
    
    private float speed = 1.0f;
    
    public BulletData() {}

    public Vector3f getStartPoint() {
        return startPoint;
    }

    /**
     * 设置子弹的开始位置点
     * @param startPoint
     */
    public void setStartPoint(Vector3f startPoint) {
        this.startPoint = startPoint;
    }

    public Vector3f getEndPoint() {
        return endPoint;
    }

    /**
     * 设置子弹的目标结束点, 结束点可以是一个静态点或者是一个动态引用，对于跟踪类型的子弹来说，
     * end点必须是一个动态引用.
     * @param endPoint 
     */
    public void setEndPoint(Vector3f endPoint) {
        this.endPoint = endPoint;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    
}
