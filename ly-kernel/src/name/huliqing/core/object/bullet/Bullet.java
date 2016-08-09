/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.bullet;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.BulletData;
import name.huliqing.core.xml.DataProcessor;

/**
 * @author huliqing
 * @param <T>
 * @param <S> 发射子弹的源
 */
public abstract class Bullet<T extends BulletData, S> extends Node implements DataProcessor<T> {
    
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
    
    protected T data;
    
    /**
     * 标记子弹是否已经初始化
     */
    protected boolean initialized;
    
    /**
     * 标记子弹是否已经耗尽。
     */
    protected boolean consumed;
    
    /**
     * 发射子弹的目标源,可能是一个角色，也可能是其它
     */
    protected S source;
    
    protected List<Listener> listeners;

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }
    
    /**
     * 开始运行子弹
     */
    public void initialize() {
        initialized = true;
        consumed = false;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
        
    @Override
    public final void updateLogicalState(float tpf) {
        // 如果已经标记为销毁，则不再处理逻辑，由BulletManager去清理和移除子弹。
        if (isConsumed()) {
            return;
        }
        
        // 让子类去更新飞行逻辑。 
        bulletUpdate(tpf);
        
        // 触发子弹飞行时的侦听器，如果bulletFlying返回true,则说明击中了一个目标。
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                if (listeners.get(i).onBulletFlying(this)) {
                    onFiredTaget();
                }
            }
        }
    }

    /**
     * 结束子弹并清理和释放资源
     */
    public void cleanup() {
        initialized = false;
    }
    
    /**
     * 销毁子弹，调用这个方法之后，子弹的逻辑将不会再执行，等着被清理、回收的节奏。一般来说，
     * 应该只有在子弹明确击中目标后或者希望立确销毁子弹的时候才应该调用这个方法，因为调用这个方法之后子弹将立即从
     * 场景中消失,在某些情况下可能会显得很突兀。在不能确定是否应该让自己立即销失的情况下应该调用
     * {@link #requestConsume() }以让子弹决定如何消息。
     * @see #requestConsume() 
     */
    public void consume() {
        consumed = true;
    }
    
    /**
     * 判断子弹是否已经消耗完，完成子弹的使命。
     * @return 
     */
    public boolean isConsumed() {
        return consumed;
    }
   
    /**
     * 请求销毁子弹，默认情况下该方法什么也不做，具体是否应该销毁子弹由不同类型的子弹的实际情况决定，该方法由子类实
     * 现决定。
     * @see #consume() 
     */
    public void requestConsume() {
        // 由子类实现。
    }
    
    /**
     * 添加一个子弹侦听器
     * @param listener 
     */
    public void addListener(Listener listener) {
        if (listeners == null) {
            listeners = new ArrayList<Listener>(1);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * 删除子弹侦听器
     * @param listener
     * @return 
     */
    public boolean removeListener(Listener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    /**
     * 获取发射该子弹的源，比如一个角色？或是一个未知的存在？
     * @return 
     */
    public S getSource() {
        return source;
    }
    
    /**
     * 设置发射子弹的源
     * @param source 
     */
    public void setSource(S source) {
        this.source = source;
    }
    
    /**
     * 判断当前子弹是否击中目标
     * @param target
     * @return 
     */
    public abstract boolean isHit(Spatial target);
    
    /**
     * 判断当前子弹是否击中目标点。
     * @param target
     * @return 
     */
    public abstract boolean isHit(Vector3f target);
    
    /**
     * 更新子弹的位置逻辑。
     * @param tpf 
     */
    protected abstract void bulletUpdate(float tpf);
    
    /**
     * 当子弹击中一个目标时该方法被调用，子类实现这个方法来处理击中效果，比如：播放击中时的特效，声效等。
     */
    protected abstract void onFiredTaget();
    
}
