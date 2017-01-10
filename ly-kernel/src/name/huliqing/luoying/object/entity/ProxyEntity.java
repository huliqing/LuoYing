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
package name.huliqing.luoying.object.entity;

import name.huliqing.luoying.object.entity.impl.PlatformProxyEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 代理类型的实体，这种实体类型自己不做实际的任何功能，只是把功能委托给另一个实际的实体去处理。
 * 提供这样一种类型主要是为了在一些特别的情况下，特别是在运行时系统可以根据实际情况切换不同的实体类型。
 * 比如运行时根据不同的平台可以切换不同类型的实体来运行，参考 {@link PlatformProxyEntity} <br>
 * @author huliqing
 */
public abstract class ProxyEntity extends NonModelEntity {

    // 被代理的直接对象, 代理允许实现一个代理链，即可以像下面这样实现代理:
    // ProxyEntityA -> ProxyEntityB -> WaterEntity.
    // 如果ProxyEntityA是当前实体，那么ProxyEntityB就是ProxyEntityA的直接代理对象，而WaterEntity就是最终的实际代理对象
    private Entity proxyEntity;
    
    // 被代理的实际对象，一般也是代理链的最终对象。
    private Entity actualEntity;

    @Override
    public void updateDatas() {
        super.updateDatas(); 
        if (proxyEntity != null) {
            proxyEntity.updateDatas();
        }
    }
    
//    @Override
//    public Spatial getSpatial() {
//        return proxyEntity.getSpatial();
//    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        proxyEntity = getProxyEntity();
        if (proxyEntity == this) {
            throw new IllegalStateException("ProxyEntity could not point to self, entityId=" + data.getId());
        }
        proxyEntity.onInitScene(scene);
        
        // 找出实际代理的entity
        actualEntity = getActualEntityByRecursion(scene, proxyEntity);
    }
    
    private Entity getActualEntityByRecursion(Scene scene, Entity pe) {
        if (pe == this) {
            throw new IllegalStateException("ProxyEntity could not point to self, entityId=" + data.getId());
        }
        if (pe instanceof ProxyEntity) {
            return getActualEntityByRecursion(scene, ((ProxyEntity)pe).getActualEntity());
        }
        return pe;
    }

    @Override
    public void cleanup() {
        proxyEntity.cleanup();
        super.cleanup();
    }

    /**
     * 获取当前实体所代理的实际对象，代理可以是一个代理链，而这个方法返回的是代理链最后的实际代理对象。
     * @return 
     */
    public final Entity getActualEntity() {
        return actualEntity;
    }
    
    /**
     * 获取代理的直接对象，子类实现这个方法，并返回ProxyEntity所代理的直接对象。
     * @return 
     */
    public abstract Entity getProxyEntity();
}
