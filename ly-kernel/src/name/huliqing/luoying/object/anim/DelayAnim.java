/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.anim;

import name.huliqing.luoying.data.DelayAnimData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * DelayAnim是一个特殊的动画功能的代理，它本身不是一个动画，只用于支持动画的延迟执行功能。
 * @author huliqing
 */
public final class DelayAnim implements DataProcessor<DelayAnimData> {
//    private static final Logger LOG = Logger.getLogger(DelayAnim.class.getName());

    private DelayAnimData data;
    
    // ---- inner
    private float delayTime;
    private Anim actualAnim;
    private boolean actualAnimStarted;
    private float timeUsed;
    
    @Override
    public void setData(DelayAnimData data) {
        this.data = data;
        actualAnim = Loader.load(data.getAnimData());
        delayTime = data.getDelayTime();
        timeUsed = data.getAsFloat("timeUsed", timeUsed);
    }

    @Override
    public DelayAnimData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        actualAnim.updateDatas();
        data.setAttribute("timeUsed", timeUsed);
        
        // 这个参数不保存,不去覆盖原始配置值，以支持一些外部调用时自行计算、动态设置，并且不会引起覆盖：
        // da.setDelayTime(da.getData().getDelayTime() / speed); // 注：这
//        data.setDelayTime(delayTime); 

//        data.setAttribute("actualAnimStarted", actualAnimStarted);// 注：这个参数不能保存
    }

    public Anim getActualAnim() {
        return actualAnim;
    }
    
    public void initialize() {
//        LOG.log(Level.INFO, "DelayAnim initialize, id={0}, delayTime={1}, timeUsed={2}"
//                , new Object[] {data.getId(), delayTime, timeUsed});
        if (timeUsed >= delayTime) {
            startAnim();
        }
    }
    
    public void update(float tpf) {
        timeUsed += tpf;
        if (actualAnimStarted) {
            actualAnim.update(tpf);
        } else if (timeUsed > delayTime){
            startAnim();
        }
    }
    
    public void cleanup() {
//        LOG.log(Level.INFO, "DelayAnim cleanup, id={0}, delayTime={1}, timeUsed={2}"
//                , new Object[] {data.getId(), delayTime, timeUsed});
        if (!actualAnim.isEnd()) {
            actualAnim.cleanup();
        }
        actualAnimStarted = false;
        timeUsed = 0;
    }
    
    public void setDelayTime(float delayTime) {
        this.delayTime = delayTime;
    }
    
    private void startAnim() {
//        LOG.log(Level.INFO, "DelayAnim startAnim, id={0}, delayTime={1}, timeUsed={2}"
//                , new Object[] {data.getId(), delayTime, timeUsed});
        actualAnim.start();
        actualAnimStarted = true;
    }
    
}
