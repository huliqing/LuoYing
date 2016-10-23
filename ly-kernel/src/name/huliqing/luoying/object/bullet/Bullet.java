/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.bullet;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 * @param <T>
 */
public interface Bullet<T extends EntityData> extends Entity<T> {
    
    /**
     * 子弹侦听器，用于监听子弹的飞行过程。
     */
    public interface Listener {
        
        /**
         * 在子弹的飞行过程中，该方法会持续被调用,可以在这个方法中来检测子弹是否击中一个目标，
         * 如果击中了一个目标，则返回true,否则返回false. 如果击中了一个目标，并希望让子弹结束，
         * 可以调用 {@link #consume() }方法来销毁子弹。
         * @param bullet 
         * @return  
         */
        boolean onBulletFlying(Bullet bullet);

    }
    
    /**
     * 设置子弹的开始射击位置
     * @param startPoint 
     */
    void setStart(Vector3f startPoint);
    
    /**
     * 设置子弹的目标射击位置
     * @param endPoint 
     */
    void setEnd(Vector3f endPoint);
    
    /**
     * 设置子弹的速度
     * @param speed 
     */
    void setSpeed(float speed);
    
    /**
     * 销毁子弹，调用这个方法之后，子弹的逻辑将不会再执行，等着被清理、回收的节奏。一般来说，
     * 应该只有在子弹明确击中目标后或者希望立确销毁子弹的时候才应该调用这个方法，因为调用这个方法之后子弹将立即从
     * 场景中消失,在某些情况下可能会显得很突兀。在不能确定是否应该让自己立即销失的情况下应该调用
     * {@link #requestConsume() }以让子弹决定如何消息。
     * @see #requestConsume() 
     */
    void consume();
    
    /**
     * 判断子弹是否已经消耗完，完成子弹的使命。
     * @return 
     */
    boolean isConsumed();
    
//    /**
//     * 请求销毁子弹，默认情况下该方法什么也不做，具体是否应该销毁子弹由不同类型的子弹的实际情况决定，该方法由子类实
//     * 现决定。
//     * @see #consume() 
//     */
//    void requestConsume();
    
    /**
     * 添加一个子弹侦听器
     * @param listener 
     */
    void addListener(Listener listener);
    
    /**
     * 删除子弹侦听器
     * @param listener
     * @return 
     */
    boolean removeListener(Listener listener);
    
    /**
     * 获取发射该子弹的源，比如一个角色？或是一个未知的存在？
     * @return 
     */
    Entity getSource();
    
    /**
     * 设置发射子弹的源
     * @param source 
     */
    void setSource(Entity source);
    
    /**
     * 判断当前子弹是否击中目标
     * @param target
     * @return 
     */
    boolean isHit(Spatial target);
    
    /**
     * 判断当前子弹是否击中目标点。
     * @param target
     * @return 
     */
    boolean isHit(Vector3f target);
    
}
