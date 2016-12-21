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

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

/**
 * 曲线的运动效果
 * @author huliqing
 */
public class CurveMove extends SimpleMotion {

    private float sineFactor;
    private float height;
    private Vector3f originScale;
    
    public void setHeight(float height) {
        this.height = height;
    }
    
    @Override
    protected void doInit() {
        super.doInit(); 
        // 记住原始缩放
        if (useScale) {
            if (originScale == null) {
                originScale = new Vector3f();
            }
            originScale.set(target.getLocalScale());
        }
    }
    
    @Override
    protected void doMotion(Spatial ui, float factor) {
        sineFactor = FastMath.sin(factor * FastMath.PI);
        TempVars tv = TempVars.get();
        tv.vect1.set(startPos);
        tv.vect1.interpolateLocal(endPos, factor);
        tv.vect1.setY(tv.vect1.y + height * sineFactor);
        ui.setLocalTranslation(tv.vect1);
        tv.release();
    }

    @Override
    protected void doAlpha(Spatial ui, float factor) {
        // unsupported
    }

    @Override
    public void cleanup() {
        if (useScale) {
            TempVars tv = TempVars.get();
            tv.vect1.set(originScale).multLocal(endScale);
            target.setLocalScale(tv.vect1);
            tv.release();
        }
        super.cleanup();
    }

    @Override
    protected void doScale(Spatial spatial, float factor) {
        float scale = FastMath.interpolateLinear(factor, startScale, endScale);
        TempVars tv = TempVars.get();
        tv.vect1.set(originScale).multLocal(scale);
        spatial.setLocalScale(tv.vect1);
        tv.release();
    }

    
}
