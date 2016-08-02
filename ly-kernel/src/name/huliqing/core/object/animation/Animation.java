/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.animation;

import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public interface Animation {
    
    /**
     * 设置动画的总用时限制,单位秒。该时间之后动画将停止更新逻辑。
     * 所以实现类的动画逻辑必须在该时间之内完成整个动画的效果 
     * @param useTime
     */
    public void setAnimateTime(float useTime);
    
    public float getAnimateTime();
    
    /**
     * 设置目标UI
     * @param ui 
     */
    public void setTarget(Spatial ui);
    
    public Spatial getTarget();
    
    /**
     * 开始执行动画
     */
    public void start();
    
    /**
     * 更新UI动画逻辑
     * @param tpf 
     */
    public void update(float tpf);
    
    /**
     * 是否动画已经执行完
     * @return 
     */
    public boolean isEnd();
    
    /**
     * 动画执行后清理。
     */
    public void cleanup();
    
    /**
     * 获取实时的使用时间
     * @return 
     */
    public float getTime();
}
