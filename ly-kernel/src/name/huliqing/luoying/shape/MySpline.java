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
package name.huliqing.luoying.shape;

import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import java.util.List;

/**
 * 多一个getSplinePoint
 * @author huliqing
 */
public class MySpline extends Spline {
    
    /**
     * 获取spline上的点坐标
     * @param distance spline上的长度,从startPoint算起
     * @param store 存放结果
     * @return 
     */
    public Vector3f getSplinePoint(float distance, Vector3f store) {
        float sum = 0;
        int i = 0;
        for (Float len : getSegmentsLength()) {
            if (sum + len >= distance) {
                interpolate((distance - sum) / len, i, store);
                return store;
            }
            sum += len;
            i++;
        }
        List<Vector3f> cps = getControlPoints();
        store.set(cps.get(cps.size() - 1));
        return store;
    }
}
