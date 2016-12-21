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
    private float speed = 1.0f;
    // 开始执行动画的时间点，当速度不变(1.0)时，这个时间和delayTime是一样的。
    private float startTime;
    private Anim actualAnim;
    private boolean actualAnimStarted;
    private float timeUsed;
    
    @Override
    public void setData(DelayAnimData data) {
        this.data = data;
        actualAnim = Loader.load(data.getAnimData());
        timeUsed = data.getAsFloat("timeUsed", timeUsed);
        speed = data.getAsFloat("speed", speed);
    }

    @Override
    public DelayAnimData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        actualAnim.updateDatas();
        data.setAttribute("timeUsed", timeUsed);
        data.setAttribute("speed", speed);
//        data.setDelayTime(delayTime);  // 不改变的参数不保存
//        data.setAttribute("actualAnimStarted", actualAnimStarted);// 注：这个参数不能保存
    }

    public Anim getActualAnim() {
        return actualAnim;
    }
    
    public void initialize() {
//        LOG.log(Level.INFO, "DelayAnim initialize, id={0}, delayTime={1}, timeUsed={2}"
//                , new Object[] {data.getId(), delayTime, timeUsed});
    
        startTime = data.getDelayTime() / speed;
        if (timeUsed >= startTime) {
            startAnim();
        }
    }
    
    public void update(float tpf) {
        timeUsed += tpf;
        if (actualAnimStarted) {
            actualAnim.update(tpf);
        } else if (timeUsed > startTime){
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
    
    /**
     * 设置动画的作用目标
     * @param object 
     */
    public void setTarget(Object object) {
        actualAnim.setTarget(object);
    }
    
    /**
     * 设置动画速度
     * @param speed 
     */
    public void setSpeed(float speed) {
        this.speed = speed;
        this.startTime = data.getDelayTime() / speed;
        this.actualAnim.setSpeed(speed);
    }
    
    private void startAnim() {
//        LOG.log(Level.INFO, "DelayAnim startAnim, id={0}, delayTime={1}, timeUsed={2}"
//                , new Object[] {data.getId(), delayTime, timeUsed});
        actualAnim.setSpeed(speed);
        actualAnim.start();
        actualAnimStarted = true;
    }
    
}
