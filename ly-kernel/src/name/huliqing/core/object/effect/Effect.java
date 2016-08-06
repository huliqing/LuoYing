/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.effect;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.core.data.EffectData;
import name.huliqing.core.xml.DataProcessor;

/**
 * 特效, 特效可以添加到EffectManager上，也可以直接添加到一个Node下面,所有效果都有一个执行时间，
 * 当效果执行结束后应该自动脱离场景,并清理释放资源。<br>
 * @author huliqing
 * @param <T>
 * @version v1.3 20160806
 * @since v1.2 20150421
 */
public abstract class Effect<T extends EffectData> extends Node implements DataProcessor<T>{
    
    /**
     * 特效帧听器，用于临听特效是否结束
     */
    public interface EffectListener {
        
        /**
         * 当特效结束时这个方法会被调用。
         * @param effect
         */
        void onEffectEnd(Effect effect);
    }
    
    protected T data;
    protected boolean initialized;
    
    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public T getData() {
        return data;
    }
    
    /**
     * 初始化特效
     */
    public void initialize() {
        initialized = true;
    }
    
    /**
     * 判断特效是否已经初始化
     * @return 
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 清理效果数据，这个方法一般由特效内部调用，当特效结束后自行调用这个方法来清理特效资源。
     */
    public void cleanup() {
        initialized = false;
    }
    
    /**
     * 请求结束特效，一般情况下不要直接结束特效（如：cleanup)，因为一些特效如果直接结束会非常不自然和难看，
     * 所以在调用特效，并希望结束一个特效时应该使用这个方法来请求结束一个特效，
     * 而具体是否结束或者如何结束一个特效由具体的子类去实现. 
     */
    public abstract void requestEnd();
    
    /**
     * 判断效果是否已经结束
     * @return 
     */
    public abstract boolean isEnd();
    
    /**
     * 设置特效要跟随的目标对象，当设置了这个目标之后，特效在运行时可以跟随这个目标的"位置","朝向”等，
     * 视实现类的情况而定。当特效在结束后要清理这个目标对象，释放相关资源，以避免持续保持对这个对象的引用。
     * 在重新执行这个特效时可以重新设置这个跟踪对象。
     * @param traceObject 
     */
    public abstract void setTraceObject(Spatial traceObject);

    /**
     * 添加特效监听器,注：特效监听器不会自动移除，所以添加了帧听器之后需要视情况自行移除，以避免内存涉漏，
     * 特别是对于进行了缓存的特效。
     * @param listener 
     */
    public abstract void addListener(EffectListener listener);
    
    /**
     * 移除特效监听器.
     * @param listener 
     * @return  如果成功移除了特效则返回true,否则false.
     */
    public abstract boolean removeListener(EffectListener listener);
}
