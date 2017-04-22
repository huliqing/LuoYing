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
package name.huliqing.luoying.object.skill;

import name.huliqing.luoying.data.SkillData;

/**
 *
 * @author huliqing
 */
public class SkinSkill extends SimpleAnimationSkill {

    // 武器挂起或取出时的动画时间点，这个时间点取值[0.0~1.0],也即武器出现在手上
    // 或在挂靠点上（如背上，腿侧）的时间点。这个时间点是相对于取武器技能时间而定的。
    private float hangTimePoint;
    
    @Override
    public void setData(SkillData data) {
        super.setData(data); 
       this.hangTimePoint = data.getAsFloat("hangTimePoint", 0.5f);
    }
    
    /**
     * 武器挂起或取出时的动画时间点，这个时间点取值[0.0~1.0],也即武器出
     * 现在手上或在挂靠点上（如背上，腿侧）的时间点。这个时间点是相对于取武器技能时间而定的。
     * @return 
     */
    public float getHangTimePoint() {
        return hangTimePoint;
    }
}
