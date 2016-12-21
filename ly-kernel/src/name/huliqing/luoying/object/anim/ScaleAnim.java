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

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.luoying.data.AnimData;

/**
 * 缩放动画
 * @author huliqing
 */
public final class ScaleAnim extends AbstractAnim<Spatial> {
    // 记住原始缩放值
    private final Vector3f originScale = new Vector3f();
    // 缩放值 
    private Vector3f startScale = new Vector3f(1,1,1);
    private Vector3f endScale = new Vector3f(2,2,2);
    
    // 是否复原缩放值，在动画结束后
    private boolean restore;
    // 缩放点偏移,这个点是以当前缩放对象的本地坐标系为准计算的。
    // 当设置了localScaleOffset时，目标对象将以这个位置点作为缩放原点进行缩放。
    // 否则将以目标对象的本地原点作为缩放点。
    private Vector3f localScaleOffset;
    private final Vector3f scaleStartPos = new Vector3f();
    private final Vector3f scaleEndPos = new Vector3f();
    
    @Override
    public void setData(AnimData data) {
        super.setData(data);
        this.startScale = data.getAsVector3f("startScale", startScale);
        this.endScale = data.getAsVector3f("endScale", endScale);
        this.restore = data.getAsBoolean("restore", restore);
        this.localScaleOffset = data.getAsVector3f("scaleOffset");
    }
    
    public void setLocalScaleOffset(Vector3f localScaleOffset) {
        this.localScaleOffset = localScaleOffset;
    }
    
    public boolean isRestore() {
        return restore;
    }

    /**
     * 设置是否在动画完成后复原原始缩放比例
     * @param restoreScale 
     */
    public void setRestore(boolean restoreScale) {
        this.restore = restoreScale;
    }

    public Vector3f getStartScale() {
        return startScale;
    }

    public void setStartScale(Vector3f startScale) {
        this.startScale.set(startScale);
    }
    
    public void setStartScale(float scale) {
        this.startScale.set(scale, scale, scale);
    }

    public Vector3f getEndScale() {
        return endScale;
    }

    public void setEndScale(Vector3f endScale) {
        this.endScale.set(endScale);
    }
    
    public void setEndScale(float scale) {
        this.endScale.set(scale, scale, scale);
    }
    
    @Override
    protected void doAnimInit() {
        originScale.set(target.getLocalScale());
        
        // 初始化缩放中心偏移的情况
        if (localScaleOffset != null) {
            Vector3f local = target.getLocalTranslation();
            Vector3f scaleCenter = local.add(localScaleOffset);
            Vector3f dirDistance = local.subtract(scaleCenter).normalizeLocal().multLocal(localScaleOffset.length());
            
            scaleStartPos.set(dirDistance).multLocal(startScale).addLocal(scaleCenter);
            scaleEndPos.set(dirDistance).multLocal(endScale).addLocal(scaleCenter);
        }
        
        // 初始化为startScale,必须的。
        target.setLocalScale(startScale);
    }
    
    @Override
    protected void doAnimUpdate(float interpolation) {
        
        TempVars tv = TempVars.get();
        Vector3f scale = tv.vect1;
        FastMath.extrapolateLinear(interpolation, startScale, endScale, scale);
        target.setLocalScale(scale);
        
        // 位置偏移,当设置了缩放偏移时需要处理缩放过程中的目标对象位置
        if (localScaleOffset != null) {
            FastMath.extrapolateLinear(interpolation, scaleStartPos, scaleEndPos, tv.vect2);
            target.setLocalTranslation(tv.vect2);
        }
        
        tv.release();
        
    }

    @Override
    public void cleanup() {
        if (restore) {
            target.setLocalScale(originScale);
            if (localScaleOffset != null) {
                target.setLocalTranslation(scaleEndPos);
            }
        }
        super.cleanup();
    }

    
}
