/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object;

/**
 * 间隔性逻辑物体，可指定逻辑间隔
 * @author huliqing
 */
public abstract class IntervalLogic extends AbstractPlayObject {
    
    /**
     * 逻辑运行的时间间隔,单位秒,默认情况下为1秒,即每1秒运行一次逻辑.如果逻辑
     * 需要实时精准执行,可将该值设置为0.
     */
    protected float interval = 1;
    
    private float timeUsed;
    
    public IntervalLogic() {
        super();
    }
    
    public IntervalLogic(float interval) {
        super();
        this.interval = interval;
    }

    public float getInterval() {
        return interval;
    }

    public void setInterval(float interval) {
        this.interval = interval;
    }

    @Override
    public final void update(float tpf) {
        timeUsed += tpf;
        if (timeUsed >= interval) {
            doLogic(tpf);
            timeUsed = 0;
        }
    }
    
    /**
     * 实现逻辑，该方法不要在外部调用，外部更新调用需要用update(tpf)方法.
     * 否则frequency设置将无效。
     * @param tpf 
     */
    protected abstract void doLogic(float tpf);
}
