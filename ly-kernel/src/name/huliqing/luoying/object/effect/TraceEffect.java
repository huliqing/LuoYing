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

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * TraceEffect主要目的是用于让效果有“跟随”功能， 这个效果本身没有任何可视特效，只是作为一个特效代理，
 * 它会代理一个实际存在的特效，并让这个特效拥有跟随功能。
 * @author huliqing
 */
public class TraceEffect extends Effect {
    
    /**  实际代理的效果*/
    private EffectData proxyEffectData;
    
     /** 特效跟随位置的类型 */
    private TraceType traceLocation;
    
    /** 特效跟随旋转的类型  */
    private TraceType traceRotation;
    
    /** 特效的初始偏移位置（跟随时用） */
    private Vector3f traceLocationOffset;
    
    /** 特效的初始偏移旋转（跟随时用） */
    private Quaternion traceRotationOffset;
    
    /** 特效跟随位置时的偏移类型 */
    private TraceOffsetType traceLocationType;
    
    // ---- inner
    /** 跟踪的目标对象,必须必须有一个目标对象才有可能跟随 */
    private Spatial traceObject;
    /**  实际代理的效果实例 */
    private Effect proxyEffect;

    @Override
    public void setData(EffectData data) {
        super.setData(data);
        // 实际代理的特效
        proxyEffectData = data.getAsObjectData("effect");
        // 跟随
        traceLocation = TraceType.identity(data.getAsString("traceLocation", TraceType.no.name()));
        traceRotation = TraceType.identity(data.getAsString("traceRotation", TraceType.no.name()));
        traceLocationOffset = data.getAsVector3f("traceLocationOffset");
        traceRotationOffset = data.getAsQuaternion("traceRotationOffset");
        traceLocationType = TraceOffsetType.identify(data.getAsString("traceLocationType", TraceOffsetType.origin.name()));
    }

    @Override
    public void updateDatas() {
        if (proxyEffect != null && proxyEffect.isInitialized()) {
            proxyEffect.updateDatas();
        }
        super.updateDatas();
    }

    @Override
    public void initialize() {
        super.initialize();
        // 载入实际代理的特效
        if (proxyEffectData != null) {
            proxyEffect = Loader.load(proxyEffectData);
            this.animNode.attachChild(proxyEffect);
            proxyEffect.initialize();
        }
        
        // 初始化跟随
        if (traceObject != null) {
            if (traceLocation == TraceType.once || traceLocation == TraceType.always) {
                doUpdateTracePosition();
            }
            if (traceRotation == TraceType.once || traceRotation == TraceType.always) {
                doUpdateTraceRotation();
            }
        }
    }

    @Override
    public void cleanup() {
        if (proxyEffect != null && proxyEffect.isInitialized()) {
            proxyEffect.cleanup();
        }
        super.cleanup();
    }
    
    @Override
    protected void effectUpdate(float tpf) {
        super.effectUpdate(tpf);
        if (!initialized) {
            return;
        }
        
        // 更新位置
         if (traceObject != null) {
            if (traceLocation == TraceType.always) {
                doUpdateTracePosition();
            }
            if (traceRotation == TraceType.always) {
                doUpdateTraceRotation();
            }
        }
    }
    
    /**
     * 直接设置要跟随的对象
     * @param traceObject 
     */
    public void setTraceObject(Spatial traceObject) {
        this.traceObject = traceObject;
    }
    
    private void doUpdateTracePosition() {
        // add type offset
        Vector3f pos = getLocalTranslation();
        
        if (traceLocationType == TraceOffsetType.origin) {
            pos.set(traceObject.getWorldTranslation());
            
        } else if (traceLocationType == TraceOffsetType.origin_bound_center) {
            pos.set(traceObject.getWorldTranslation());
            BoundingVolume bv = traceObject.getWorldBound();
            if (bv != null) {
                pos.setY(bv.getCenter().getY());
            }
            
        } else if (traceLocationType == TraceOffsetType.origin_bound_top) {
            GeometryUtils.getBoundTopPosition(traceObject, pos);
            pos.setX(traceObject.getWorldTranslation().x);
            pos.setZ(traceObject.getWorldTranslation().z);
            
        } else if (traceLocationType == TraceOffsetType.bound_center) {
            pos.set(traceObject.getWorldBound().getCenter());
            
        } else if (traceLocationType == TraceOffsetType.bound_top) {
            GeometryUtils.getBoundTopPosition(traceObject, pos);
            
        }
        // 注：tracePositionOffset是以被跟随的目标对象的本地坐标为基准的,
        // 所以必须mult上目标对象的旋转
        if (traceLocationOffset != null) {
            TempVars tv = TempVars.get();
            traceObject.getWorldRotation().mult(traceLocationOffset, tv.vect2);
            pos.addLocal(tv.vect2);
            tv.release();
        }
        setLocalTranslation(pos);
//        LOG.log(Level.INFO, "AbstractEffect doUpdateTracePosition, pos={0}", new Object[] {pos});
    }
    
    private void doUpdateTraceRotation() {
        Quaternion rot = getLocalRotation();
        rot.set(traceObject.getWorldRotation());
        if (traceRotationOffset != null) {
            rot.multLocal(traceRotationOffset);
        }
        setLocalRotation(rot);
    }
    
    
    /**
     * 跟随的位置类型
     *
     * @author huliqing
     */
    private enum TraceOffsetType {

        /**
         * 以原点作为跟随位置(默认方式)
         */
        origin,
        /**
         * 以原点但是y值使用的是包围盒中心点处的值作为跟随位置。
         */
        origin_bound_center,
        /**
         * 以原点但是y值使用的是包围盒顶部最高位置点处的y值作为跟随位置
         */
        origin_bound_top,
        // 暂不支持bound_bottom，好像没有意义
        //    /** 以包含围盒的“底部”中心点作为跟随位置 */
        //    bound_bottom,

        /**
         * 以包围盒的“中心点”作为跟随位置
         */
        bound_center,
        /**
         * 以包围盒的“顶部”中心点作为跟随位置
         */
        bound_top;

        public static TraceOffsetType identify(String name) {
            TraceOffsetType[] types = values();
            for (TraceOffsetType type : types) {
                if (type.name().equals(name)) {
                    return type;
                }
            }
            throw new UnsupportedOperationException("Unknow TraceOffsetType:" + name);
        }
    }
    
    /**
     * 跟随类型
     *
     * @author huliqing
     */
    private enum TraceType {

        /**
         * 不跟随
         */
        no,
        /**
         * 跟随一次
         */
        once,
        /**
         * 始终、持续跟随
         */
        always;

        public static TraceType identity(String name) {
            for (TraceType tt : values()) {
                if (tt.name().equals(name)) {
                    return tt;
                }
            }
            throw new UnsupportedOperationException("不支持的TraceType, name=" + name);
        }
    }
}
