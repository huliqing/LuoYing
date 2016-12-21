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
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import name.huliqing.luoying.sss.GameState.PlayListener;
//import name.huliqing.ly.object.Loader;
//import name.huliqing.ly.object.IntervalLogic;
//
///**
// * @author huliqing
// */
//public class EffectCache extends IntervalLogic implements PlayListener {
////    private static final Logger LOG = Logger.getLogger(EffectCache.class.getName());
//    private final static EffectCache INSTANCE = new EffectCache();
//    
//    // 空闲列表
//    private final List<Effect> freeStore = new LinkedList<Effect>();
//    // 忙碌的列表,正在运行的子弹
//    private final List<Effect> busyStore = new LinkedList<Effect>();
//    
//    private EffectCache() {
//        super(3); // 频率不用太高
//    }
//    
//    public static EffectCache getInstance() {
//        return INSTANCE;
//    }
//    
//    public Effect getEffect(String effectId) {
//        // 先偿试从缓存中取
//        if (!freeStore.isEmpty()) {
//            for (Iterator<Effect> it = freeStore.iterator(); it.hasNext(); ) {
//                Effect effect = it.next();
//                if (effect.getData().getId().equals(effectId)) {
//                    it.remove();
//                    return effect;
//                }
//            }
//        }
//        // 载入一个新的
//        return Loader.loadEffect(effectId);
//    }
//    
//    @Override
//    protected void doLogic(float tpf) {
//        // 检测busy列表中是否存在已经结束的
//        // 如果已经有结束的则添加到空闲列表中
//        for (Iterator<Effect> it = busyStore.iterator(); it.hasNext(); ) {
//            Effect effect = it.next();
//            if (effect.isEnd()) {
//                it.remove();
//                freeStore.add(effect);
//            }
//        }
//        
////        if (Config.debug) {
////            LOG.log(Level.INFO, "EffectCache, busy effects={0}, free effects={1}"
////                    , new Object[] {busyStore.size(), freeStore.size()});
////        }
//    }
//
//    @Override
//    public void onObjectAdded(Object object) {
//        if (!(object instanceof Effect))
//            return;
//        // 如果场景中添加了bullet，则把它存入busy列表。
//        busyStore.add((Effect) object);
//    }
//
//    @Override
//    public void onObjectRemoved(Object spatial) {
//        // 子弹移除时不判断，因为子弹可能会自行脱离场景。即不经过playState的removeObject.
//    }
//
//    @Override
//    public void onExit() {
//        freeStore.clear();
//        busyStore.clear();
//    }
//    
//}
