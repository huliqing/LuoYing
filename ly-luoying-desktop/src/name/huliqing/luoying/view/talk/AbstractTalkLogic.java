/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.talk;

/**
 *
 * @author huliqing
 */
public abstract class AbstractTalkLogic implements TalkLogic{
    
    // 该逻辑所需要执行的时间,单位秒
    protected float useTime;
    // 当前已经使用的时间，单位秒
    protected float time;
    
    protected boolean started = false;
    
    protected boolean network = false;
    
    public AbstractTalkLogic() {}

    @Override
    public void start() {
        if (started)
            return;
        
        started = true;
        time = 0;
        
        doInit();
    }

    @Override
    public void update(float tpf) {
        if (!started) 
            return;
        
        time += tpf;
        if (time >= useTime) {
            cleanup();
        } else {
            doTalkLogic(tpf);                
        }
    }

    @Override
    public boolean isEnd() {
        return !started;
    }

    @Override
    public void cleanup() {
        started = false;
    }

    public float getUseTime() {
        return useTime;
    }

    public void setUseTime(float useTime) {
        this.useTime = useTime;
    }

    @Override
    public void setNetwork(boolean network) {
        this.network = network;
    }
    
    /**
     * 初始化
     */
    protected abstract void doInit();
    
    /**
     * 处理谈话逻辑
     * @param tpf 
     */
    protected abstract void doTalkLogic(float tpf);

}
