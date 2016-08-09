/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.effect;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.EffectData;
import name.huliqing.core.loader.Loader;

/**
 * 效果组
 * @author huliqing
 */
public class GroupEffect extends AbstractEffect {
    
    private List<EffectWrap> effects;
    
    // 当该参数为true时需要检查并结束当前效果,并且不再启动新的子效果。
    private boolean endCheck;
    
    private boolean end;
    
    @Override
    public void setData(EffectData data) {
        super.setData(data);
        // effects的格式： "effect1,effect2|0.3, effect3..."
        String[] aArr = data.getAsArray("effects");
        if (aArr != null) {
            effects = new ArrayList<EffectWrap>(aArr.length);
            for (String a : aArr) {
                String[] bArr = a.split("\\|");
                EffectWrap effect = new EffectWrap();
                effect.effectId = bArr[0];
                effect.startTime = bArr.length > 1 ? Float.parseFloat(bArr[1]) : 0;
                effects.add(effect);
            }
        }
    }
    
    @Override
    public void initialize() {
        super.initialize();
        end = false;
        
        if (effects != null) {
            for (int i = 0; i < effects.size(); i++) {
                EffectWrap ew = effects.get(i);
                ew.trueStartTime = ew.startTime / data.getSpeed();
            }
        }
    }

    @Override
    protected void effectUpdate(float tpf) {
        super.effectUpdate(tpf);
        
        // endCheck为true则检查是否可以结束当前效果组。注：当endCheck为true时未开始执行的子效果将不再执行。
        // 也就是不再去启动未开始的效果。当所有子效果都结束时则可以将当前效果组(GroupEffect)结束。
        if (endCheck) {
            int countEnd = 0;
            EffectWrap ew;
            for (int i = 0; i < effects.size(); i++) {
                ew = effects.get(i);
                if (!ew.started || (ew.started && ew.effect.isEnd())) {
                    countEnd++;
                }
            }
            // 结束当前所有特效
            end = countEnd >= effects.size();
            return;
        }
        
        // 检查并启动子效果。
        EffectWrap ew;
        int countStarted = 0;
        for (int i = 0; i < effects.size(); i++) {
            ew = effects.get(i);
            ew.checkToStart(trueTimeUsed);
            countStarted += ew.started ? 1 : 0;
        }
        
        // 当所有子效果启动后开始检查是否可以结束当前效果。
        endCheck = countStarted >= effects.size();
    }

    @Override
    protected boolean checkForEnd() {
        // GroupEffect的结束时间不依赖于设置，而依赖于所有子特效是否结束。
        return end;
    }

    @Override
    public void requestEnd() {
        super.requestEnd();
        
        // 请求让所有已经运行的子效果结束
        for (EffectWrap ew : effects) {
            if (ew.started) {
                ew.effect.requestEnd();
            }
        }
        
        // 标记endCheck,这可以让哪些还未开始执行的子效果不再检查是否要需要执行。
        endCheck = true;
    }

    @Override
    public void cleanup() {
        if (effects != null) {
            for (int i = 0; i < effects.size(); i++) {
                effects.get(i).cleanup();
            }
        }
        endCheck = false;
        super.cleanup(); 
    }
    
    private class EffectWrap {
        // 效果id,这是在xml上的原始设置
        private String effectId;
        // 效果的开始时间，这是在xml上的原始设置
        private float startTime;
        // 实例化后的效果
        private Effect effect;
        // 实际的实际开始时间，这个时间受效果的速度影响
        private float trueStartTime = 1;
        // 标记效果是否已经开始
        private boolean started;
        
        public void checkToStart(float effectTimeUsed) {
            if (started) return;
            if (effectTimeUsed >= trueStartTime) {
                if (effect == null) {
                    effect = Loader.loadEffect(effectId);
                    // 注意：子效果是直接放在GroupEffect下的，不要放在EffectManager中，
                    // 这会依赖EffectManger,导致GroupEffect不能放在其它Node节点下, 
                    // 所有类型的Effect都应该是可以单独放在任何Node下进行运行的。
                    // 把子效果的跟踪目标设置为animRoot，这样当GroupEffect添加了动画控制时，可以同时影响到子效果的变换。
                    animRoot.attachChild(effect);
                    
//                // 不要再设置子效果的跟随
//                effect.setTraceObject(animRoot);
                }
                
                // 与group保持一致的速度,这样当设置GroupEffect的速度的时候可以同时影响子效果的速度
                effect.getData().setSpeed(data.getSpeed());

                // 记得在所有设置完毕后才调用initialize,因为子效果是由GroupEffect特别管理的，这里隔离了与EffectManager的关
                // 系，initialize也可以交由效果内部调用，但是会慢一帧, 这会造成一些视角稍微滞后。
                effect.initialize();
                started = true;
            }
        }
        
        public void cleanup() {
            if (effect != null) {
                effect.cleanup();
            }
            started = false;
        }
    }
}
