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

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.ly.object.effect;
//
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import name.huliqing.ly.data.EffectData;
//import name.huliqing.ly.xml.DataProcessor;
//
///**
// * 特效, 特效可以添加到EffectManager上，也可以直接添加到一个Node下面,所有效果都有一个执行时间.
// * @author huliqing
// * @param <T>
// * @version v1.3 20160806
// * @since v1.2 20150421
// */
//public abstract class ____bak20161011_Effect<T extends EffectData> extends Node implements DataProcessor<T>{
//    
//    /**
//     * 特效帧听器，用于临听特效是否结束
//     */
//    public interface EffectListener {
//        
//        /**
//         * 当特效结束时这个方法会被调用。
//         * @param effect
//         */
//        void onEffectEnd(____bak20161011_Effect effect);
//    }
//    
//    protected T data;
//    protected boolean initialized;
//    
//    @Override
//    public void setData(T data) {
//        this.data = data;
//    }
//    
//    @Override
//    public T getData() {
//        return data;
//    }
//    
//    /**
//     * 初始化特效
//     */
//    public void initialize() {
//        initialized = true;
//    }
//    
//    /**
//     * 判断特效是否已经初始化
//     * @return 
//     */
//    public boolean isInitialized() {
//        return initialized;
//    }
//    
//    @Override
//    public final void updateLogicalState(float tpf) {
//        if (!initialized) {
//            return;
//        }
//        effectUpdate(tpf);
//    }
//    
//    /**
//     * 清理效果数据.
//     */
//    public void cleanup() {
//        initialized = false;
//    }
//    
//    /**
//     * 判断特效是否已经结束，如果该方法返回true,则特效逻辑将不再执行。
//     * @return 
//     */
//    public abstract boolean isEnd();
//    
//    /**
//     * 请求结束特效，一般情况下不要直接结束特效（如：cleanup)，因为一些特效如果直接结束会非常不自然和难看，
//     * 所以在调用特效，并希望结束一个特效时应该使用这个方法来请求结束一个特效，
//     * 而具体是否结束或者如何结束一个特效由具体的子类去实现. 
//     */
//    public abstract void requestEnd();
//    
//    /**
//     * 设置特效要跟随的目标对象，当设置了这个目标之后，特效在运行时可以跟随这个目标的"位置","朝向”等，视实现类的情况而定。
//     * 设置为null来清除这个跟踪对象。
//     * @param traceObject 
//     */
//    public abstract void setTraceObject(Spatial traceObject);
//    
//    /**
//     * 获取特效的跟随目标，如果没有跟随目标则返回null.
//     * @return 
//     */
//    public abstract Spatial getTraceObject();
//    
//    /**
//     * 添加特效监听器,注：特效监听器不会自动移除，所以添加了帧听器之后需要视情况自行移除，以避免内存涉漏.
//     * @param listener 
//     */
//    public abstract void addListener(EffectListener listener);
//    
//    /**
//     * 移除特效监听器.
//     * @param listener 
//     * @return  如果成功移除了特效则返回true,否则false.
//     */
//    public abstract boolean removeListener(EffectListener listener);
//    
//    /**
//     * 更新特效逻辑
//     * @param tpf 
//     */
//    protected abstract void effectUpdate(float tpf);
//    
//}
