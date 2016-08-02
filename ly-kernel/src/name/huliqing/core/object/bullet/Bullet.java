/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.bullet;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.core.data.BulletData;
import name.huliqing.core.object.DataProcessor;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface Bullet<T extends BulletData> extends DataProcessor<T> {
    
    /**
     * 设置开始点和精确的结束点,结束点可以是一个静态点或者是一个动态引用，
     * 对于跟踪类型的子弹来说，end点必须是一个动态引用，否则跟踪就无意义。
     * 当子弹的碰撞shape到达end点时即视为击中，即碰撞shape与end点交叉即视为击中。
     * @param startPoint
     * @param endPoint 
     */
    void setPath(Vector3f startPoint, Vector3f endPoint);
    
    Vector3f getStartPoint();
    
    Vector3f getEndPoint();
    
    /**
     * 设置子弹速度倍率，默认1.0, 子弹的速度会在每次结束时重置为1.0.
     * @param speed 
     */
    void setSpeed(float speed);
    
    /**
     * 获取子弹速度倍率.
     * @return 
     */
    float getSpeed();
    
    /**
     * 开始运行子弹
     */
    void start();
    
    /**
     * 更新逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 判断子弹是否运行结束
     * @return 
     */
    boolean isEnd();
    
    /**
     * 结束子弹运行。
     */
    void doEnd();
    
    /**
     * 结束子弹并清理和释放资源
     */
    void cleanup();
    
    /**
     * 获取子弹实体,用于添加到场景中。
     * @return 
     */
    Spatial getDisplay();
    
    /**
     * 判断是否击中目标
     * @param target
     * @return 
     */
    boolean isHit(Spatial target);
    
    /**
     * 是否击中目标点
     * @param target
     * @return 
     */
    boolean isHit(Vector3f target);
    
    /**
     * 添加一个子弹侦听器,该侦听器会在结束后清理。
     * @param listener 
     */
    void addListener(BulletListener listener);
    
    /**
     * 删除子弹侦听器
     * @param listener
     * @return 
     */
    boolean removeListener(BulletListener listener);
    
}
