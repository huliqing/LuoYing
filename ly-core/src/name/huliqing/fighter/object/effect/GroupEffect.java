/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.effect;

import com.jme3.util.SafeArrayList;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 * 效果组。该组下的子效果可以同时开始执行
 * ，并且各个子效果可以设置延迟开始执行的时间。默认情况下，该组下的效果会在
 * 一开始时同时开始执行。该用该效果需要注意以下几点：<br />
 * 1.当前效果不会执行任何实质内容，只会负责启动各个子效果。
 * 2.只有各个子效果都结束后，当前效果才会算结束。
 * 3.当前的效果各阶段的时间设置将不会影响到子效果的设置。
 * 4.注:设置GroupEffect的速度将会直接设置覆盖到所有的子效果的速度上,并
 * 且也影响子效果的延迟执行时间.
 * @author huliqing
 */
public class GroupEffect extends AbstractEffect {

    private final SafeArrayList<WrapEffect> childrenEffect = new SafeArrayList<WrapEffect>(WrapEffect.class);
    
    private enum State {
        wait,
        running,
        stop;
    }
    
    @Override
    public void setData(EffectData data) {
        super.setData(data);
        // effects的格式： "effect1,effect2|0.3, effect3..."
        String[] effects = data.getAsArray("effects");
        for (String effectStr : effects) {
            String[] effectArr = effectStr.split("\\|");
            Effect eff = Loader.loadEffect(effectArr[0]);
            float delay = 0;
            if (effectArr.length >= 2) {
                delay = ConvertUtils.toFloat(effectArr[1], 0);
            }
            WrapEffect we = new WrapEffect(eff, delay);
            childrenEffect.add(we);
        }
    }
    
    @Override
    protected void doInit() {
        super.doInit();
        for (WrapEffect we : childrenEffect.getArray()) {
            // 把速度加乘覆盖到子效果中,注:速度每次cleanup都会重置为1.0
            we.effect.getData().setSpeed(data.getSpeed() * we.effect.getData().getSpeed());
            we.delaySpeed = data.getSpeed();
            // 标记为等待执行中。
            we.state = State.wait;
        }
    }

    @Override
    protected void updatePhaseAll(float tpf) {
        for (WrapEffect we : childrenEffect.getArray()) {
            we.checkToStart(data.getTimeUsed());
        }
    }

    @Override
    protected boolean confirmEnd() {
        // 2.如果有任何一个还在执行，则不应该停止
        for (WrapEffect we : childrenEffect.getArray()) {
            // 如果还有子效果在等待执行，则不能结束
            if (we.state == State.wait) {
                return false;
            }
            // 还有已经执行但未结束的
            if (!we.effect.isEnd()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void jumpToEnd() {
        // 标记为已经中断
        for (WrapEffect we : childrenEffect.getArray()) {
            // 把未开始执行的效果状态转化为stop状态,即不需要执行
            if (we.state == State.wait) {
                we.state = State.stop;
            }
            // 把正在执行的效果跳转到结束阶段
            if (we.state == State.running) {
                we.effect.jumpToEnd();
            }
        }
        super.jumpToEnd();
    }
    
    @Override
    public void cleanup() {
        for (WrapEffect we : childrenEffect.getArray()) {
            we.cleanup();
        }
        super.cleanup();
    }
    
    // 包装effect
    private class WrapEffect {
        private Effect effect;
        // 延迟启动时间,单位秒
        private float delay;
        private float delaySpeed = 1;
        private State state = State.stop;
        
        public WrapEffect(Effect effect, float delay) {
            this.effect = effect;
            this.delay = delay;
        }
        
        public void checkToStart(float timeUsed) {
            // remove20160606
//            if (state == State.running || state == State.stop) {
//                return;
//            }
//            if (timeUsed >= (delay / delaySpeed)) {
//                localRoot.attachChild(effect.getDisplay());
//                effect.start();
//                state = State.running;
//            }

            if (state == State.wait) {
                if (timeUsed >= (delay / delaySpeed)) {
                    localRoot.attachChild(effect.getDisplay());
                    effect.start();
                    state = State.running;
                }
            }
        }
        
        public void cleanup() {
            state = State.stop;
            if (!effect.isEnd()) {
                effect.cleanup();
            }
        }
    }
}
