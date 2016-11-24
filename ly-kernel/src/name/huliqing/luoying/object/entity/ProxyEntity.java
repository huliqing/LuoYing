/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity;

import name.huliqing.luoying.object.env.PlatformProxyEnv;

/**
 * 代理类型的实体，这种实体类型自己不做实际的任何功能，只是把功能委托给另一个实际的实体去处理。
 * 提供这样一种类型主要是为了在一些特别的情况下，特别是在运行时系统可以根据实际情况切换不同的实体类型。
 * 比如运行时根据不同的平台可以切换不同类型的实体来运行，参考 {@link PlatformProxyEnv}
 * @author huliqing
 */
public interface ProxyEntity extends Entity {
    
    /**
     * 获取代理的实际Env
     * @return 
     */
    Entity getProxyEnv();
}
