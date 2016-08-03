/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.effect;

import com.jme3.scene.Spatial;
import name.huliqing.core.data.EffectData;
import name.huliqing.core.enums.EffectPhase;
import name.huliqing.core.xml.DataProcessor;

/**
 * 特效接口
 * @author huliqing
 * @param <T>
 * @since v1.2 20150421
 */
public interface Effect<T extends EffectData> extends DataProcessor<T>{
    
    @Override
    T getData();
    
    /**
     * 开始执行效果
     */
    void start();
    
    /**
     * 更新效果逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 判断效果是否未在执行: 未启动、被打断、正常结束都应返回true.
     * @return 
     */
    boolean isEnd();
    
    /**
     * 清理效果数据，该方法不管在效果被“打断”或是“正常结束”都应该至少执行一
     * 次。即：只要效果启动，那么最终不管效果是否正常或非正常结束，都应该在
     * 最后执行一次cleanup.<br />
     * 在cleanup的时候需要清理添加到当前效果的所有侦听器，为下次执行准备。
     * 侦听器一般针对于特定的调用，上一次的侦听器不一定适合于下一次的效果的运行。
     * 因为效果是设计为允许复用的。
     */
    void cleanup();
    
    /**
     * 获取当前效果所处的阶段。
     * @return 
     */
    EffectPhase getCurrentPhase();
    
    /**
     * 直接跳转到指定的结束阶段,让特效自动结束,如果特效未开始，则什么也不处理。
     */
    void jumpToEnd();
    
    /**
     * @see #setTraceObject(com.jme3.scene.Spatial) 
     * @return 
     */
    Spatial getTraceObject();
    
    /**
     * 设置跟踪目标,要能够跟踪还必须打开trace选项．{@link #setTrace(boolean) }
     * @param target 
     */
    void setTraceObject(Spatial target);
    
    /**
     * 给效果的运行添加侦听器，注：所有侦听器在效果执行结束之后会被移除。
     * 每次重新开始前需要重新添加。(因为效果是允许重复利用的，前一次效果运行
     * 时的侦顺器不一定适合下一次效果运行时所用。）
     * @param listener 
     */
    void addListener(Listener listener);
    
    /**
     * 移除指定的侦听器
     * @param listener 
     * @return 如果成功移除则返回true,如果列表中不存在指定的侦听器，或移除失败
     * 则返回false
     */
    boolean removeListener(Listener listener);
    
    /**
     * 获取display object
     * @return 
     */
    Spatial getDisplay();
    
}
