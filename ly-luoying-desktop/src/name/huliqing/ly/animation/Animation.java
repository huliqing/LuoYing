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
package name.huliqing.ly.animation;

import com.jme3.scene.Spatial;

/**
 * @deprecated use Anim instead
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
