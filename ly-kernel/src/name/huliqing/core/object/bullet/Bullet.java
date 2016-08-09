/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.bullet;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.core.data.BulletData;
import name.huliqing.core.xml.DataProcessor;

/**
 * @author huliqing
 * @param <T>
 * @param <S> 发射子弹的源
 */
public abstract class Bullet<T extends BulletData, S> extends Node implements DataProcessor<T> {
    
    protected T data;
    
    /**
     * 发射子弹的目标源,可能是一个角色，也可能是其它
     */
    protected S source;
    
    protected boolean initialized;
    
    /**
     * 标记子弹是否已经耗尽。
     */
    protected boolean consumed;

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
        if (!isInitialized() || isConsumed()) {
            return;
        }
        bulletUpdate(tpf);
    }

    /**
     * 结束子弹并清理和释放资源
     */
    public void cleanup() {
        initialized = false;
    }
    
    /**
     * 标记子弹已经完成使命，调用这个方法之后，子弹的逻辑将不会再执行，等着被清理、回收的节奏。
     * 一般来说，子弹在击中目标后会自动耗尽，不需要调用这个方法来清理。只有在一些特殊情况下，
     * 应用需要手动结束当前正在运行的子弹时才需要调用这个方法。
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
     * 更新子弹逻辑
     * @param tpf 
     */
    protected abstract void bulletUpdate(float tpf);
    
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
     * 添加一个子弹侦听器,该侦听器会在结束后清理。
     * @param listener 
     */
    public abstract void addListener(BulletListener listener);
    
    /**
     * 删除子弹侦听器
     * @param listener
     * @return 
     */
    public abstract boolean removeListener(BulletListener listener);
    
}
