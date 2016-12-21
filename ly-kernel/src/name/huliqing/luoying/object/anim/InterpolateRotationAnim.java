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

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.AnimData;

/**
 *
 * @author huliqing
 */
public class InterpolateRotationAnim extends AbstractAnim<Spatial> {

    private final Quaternion start = new Quaternion();
    private final Quaternion end = new Quaternion();
    private final Quaternion temp = new Quaternion();

    private boolean changed;
    
    @Override
    public void setData(AnimData data) {
        super.setData(data);
        Quaternion tempStart = data.getAsQuaternion("start");
        Quaternion tempEnd = data.getAsQuaternion("end");
        if (tempStart != null) {
            start.set(tempStart);
        }
        if (tempEnd != null) {
            end.set(tempEnd);
        }
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        if (changed) {
            data.setAttribute("start", start);
            data.setAttribute("end", end);
        }
    }
    
    public void setStart(Quaternion start) {
        this.start.set(start);
        changed = true;
    }
    public void setStart(Vector3f direction, Vector3f up) {
        this.start.lookAt(direction, up);
        changed = true;
    }
    
    public void setEnd(Quaternion end) {
        this.end.set(end);
        changed = true;
    }
    
    public void setEnd(Vector3f direction, Vector3f up) {
        this.end.lookAt(direction, up);
        changed = true;
    }
    
    @Override
    protected void doAnimInit() {
        target.setLocalRotation(start);
    }

    @Override
    protected void doAnimUpdate(float interpolation) {
        temp.slerp(start, end, interpolation);
        target.setLocalRotation(temp);
    }
    
}
