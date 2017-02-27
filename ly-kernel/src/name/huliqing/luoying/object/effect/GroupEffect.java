/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.effect;

import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.object.Loader;

/**
 * 效果组
 * @author huliqing
 */
public class GroupEffect extends Effect {
//    private static final Logger LOG = Logger.getLogger(GroupEffect.class.getName());
    
    private EffectWrap[] effects;
    
    @Override
    public void setData(EffectData data) {
        super.setData(data);
        // effects的格式： "effect1,effect2|0.3, effect3..."
        String[] aArr = data.getAsArray("effects");
        if (aArr != null && aArr.length > 0) {
            effects = new EffectWrap[aArr.length];
            for (int i = 0; i < aArr.length; i++) {
                String[] bArr = aArr[i].split("\\|");
                EffectWrap effect = new EffectWrap();
                effect.effectId = bArr[0];
                effect.startTime = bArr.length > 1 ? Float.parseFloat(bArr[1]) : 0;
                effects[i] = effect;
            }
        }
    }
    
    @Override
    public void initialize() {
        super.initialize();
        updateAnimSpeed();
        
         for (EffectWrap ew : effects) {
            if (ew.effect != null) {
                ew.effect.initialize();
                animNode.attachChild(ew.effect);
            }
        }
    }

    @Override
    public void setSpeed(float speed) {
        super.setSpeed(speed);
        updateAnimSpeed();
    }
    
    private void updateAnimSpeed() {
        for (EffectWrap ew : effects) {
            ew.updateSpeed(speed);
        }
    }
    
    @Override
    protected void effectUpdate(float tpf) {
        super.effectUpdate(tpf);
        for (EffectWrap ew : effects) {
            ew.checkToStart(timeUsed);
        }
    }

    @Override
    public void requestEnd() {
        // 请求让所有已经运行的子效果结束
        for (EffectWrap ew : effects) {
            if (ew.started) {
//                if (Config.debug) {
//                    LOG.log(Level.INFO, "GroupEffect requestEnd, effectTrueTimeUsed={0}, effectId={1}, effectTrueStartTime={2}"
//                            , new Object[] {timeUsed, ew.effectId, ew.trueStartTime});
//                }
                ew.effect.requestEnd();
            }
        }
    }

    @Override
    public final void cleanup() {
        if (effects != null) {
            for (EffectWrap ew : effects) {
                ew.cleanup();
            }
        }
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
                effect = Loader.load(effectId);
                // 1.同步speed属性
                // 与group保持一致的速度,这样当设置GroupEffect的速度的时候可以同时影响子效果的速度
                effect.setSpeed(speed);
//                // 不要再设置子效果的跟随
//                effect.setTraceObject(animRoot);
                effect.initialize();
                
                // 把子效果的跟踪目标设置为animRoot，这样当GroupEffect添加了动画控制时，可以同时影响到子效果的变换。
                animNode.attachChild(effect);
                
                started = true;
            }
        }
        
        public void updateSpeed(float speed) {
            trueStartTime = startTime / speed;
            if (effect != null) {
                effect.setSpeed(speed);
            }
        }
        
        public void cleanup() {
            if (effect != null && effect.isInitialized()) {
                effect.cleanup();
            }
            started = false;
        }
    }
}
